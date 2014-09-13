

class Board(object):
    BOARD_HEIGHT = 30
    BOARD_WIDTH = 16

    def __init__(self):
        self.make_board(self.BOARD_WIDTH, self.BOARD_HEIGHT)

    def make_board(self, width, height):
        blank_cell = [' ']
        blank_row = [blank_cell] * width
        board = [blank_row] * height
        self.set_board(board)

    def set_board(self, board):
        self.board = board


