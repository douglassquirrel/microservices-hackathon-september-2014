import receiver
import json

class BoardPrinter(receiver.Receiver):
    topics = ['board.*', 'game.score', 'game.ended']
    score = 0
    board = []
    id = None
    status = None

    def set_id(self, id):
        self.id = id

    def receive(self, channel, method, props, body):
        try:
            content = json.loads(body)
            if content.get('id') != self.id:
                return
            topic = method.routing_key
            if topic.startswith('board.'):
                board = self.read_board(body)
                if board is None:
                    return
                self.print_board(board)
            elif topic == 'game.ended':
                self.status = "GAME OVER"
            elif 'score' in content:
                self.score = content['score']
            self.reprint()
        except Exception, e:
            print "EXCEPTION!!!!"
            import traceback
            traceback.print_exc()

    def read_board(self, board_str):
        try:
            board = json.loads(board_str)
        except:
            print "board_str", `board_str`
            return
        if 'grid' in board:
            return board['grid']
        return None

    def clear_screen(self):
        for i in range(300):
            print ""
        return

    def print_board(self, board):
        self.board = board
        self.reprint()

    def reprint(self):
        board = self.board
        self.clear_screen()
        width = 0
        if board:
            width = len(board[0])*3
            print "\r",
            print "+" + "-" * width + "+"
            for row in board:
                print "\r",
                print "|",
                pad = ""
                for item in row:
                    print pad + (item or ' '),
                    pad = " "
                print "|",
                print ""
            print "\r",
            print "+" + "-" * width + "+"
        else:
            self.prn("No board!")
        self.print_footer()
        print "\r",

    def prn(self, txt):
        print "\r",
        print txt

    def print_footer(self):
        self.prn("")
        self.prn("*** Current score:"+ str(self.score))
        if self.status:
            self.prn("    "+ str(self.status))
        self.prn("(game id = %s)" % (self.id, ))

    def run(self):
        self.start()

if __name__ == '__main__':
    printer = BoardPrinter()
    printer.run()

