import receiver
import json

class BoardPrinter(receiver.Receiver):
    topic = 'board.*'
    score = 'Not implemented yet'
    id = None

    def set_id(self, id):
        self.id = id

    def receive(self, channel, method, props, body):
        try:
            print "received", method.exchange, method.routing_key
            board = self.read_board(body)
            if board is None:
                return
            self.clear_screen()
            self.print_board(board)
        except:
            print "RECEIVE EXCEPTION"
            raise

    def read_board(self, board_str):
        try:
            board = json.loads(board_str)
        except:
            print "board_str", `board_str`
            return
        board_id = board.get('id')
        if board_id != self.id:
            return
        if 'grid' in board:
            return board['grid']
        return None

    def clear_screen(self):
        for i in range(300):
            print ""
        return

    def print_board(self, board):
        width = len(board[0])*3
        print "+" + "-" * width + "+"
        for row in board:
            print "|",
            pad = ""
            for item in row:
                print pad + (item or ' '),
                pad = " "
            print "|",
            print ""
        print "+" + "-" * width + "+"
        self.print_footer()

    def print_footer(self):
        print ""
        print "*** Current score:", self.score

    def run(self):
        self.start()

if __name__ == '__main__':
    printer = BoardPrinter()
    printer.run()

