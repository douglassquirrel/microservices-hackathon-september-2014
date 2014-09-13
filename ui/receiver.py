
class Receiver(object):
    topics = ['#']

    def __init__(self):

        from pika import BlockingConnection, ConnectionParameters

        RABBIT_HOST = '54.76.117.95'
        RABBIT_PORT = 5672
        EXCHANGE = 'combo'

        conn = BlockingConnection(ConnectionParameters(host=RABBIT_HOST,
                                                       port=RABBIT_PORT))
        self.channel = conn.channel()
        self.channel.exchange_declare(exchange=EXCHANGE, type='topic')

        for topic in self.topics:
            result = self.channel.queue_declare(exclusive=True)
            queue = result.method.queue
            self.channel.queue_bind(exchange=EXCHANGE, queue=queue, routing_key=topic)
            self.channel.basic_consume(self.receive, queue=queue, no_ack=True)

    def start(self):
        self.channel.start_consuming()

    def receive(self, ch, method, properties, body):
        print body
        raise NotImplementedError

if __name__ == '__main__':
    r = Receiver()
    r.start()


