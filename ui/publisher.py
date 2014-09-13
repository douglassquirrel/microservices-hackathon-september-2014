
class Publisher(object):

    def __init__(self, queue='combo', topic='game'):
        import pika
        self.topic = topic

        parameters = pika.URLParameters('amqp://54.76.117.95:5672')

        connection = pika.BlockingConnection(parameters)
        self.channel = connection.channel()
        self.channel.queue_declare(queue=queue)

    def publish(self, topic, message):
        from json import dumps
        # properties = pika.BasicProperties(content_type="text/plain", delivery_mode=2)
        topic = topic or self.topic
        self.channel.basic_publish('combo', topic, dumps(message))


