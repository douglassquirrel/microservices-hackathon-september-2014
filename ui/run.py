
import logging, os
log = logging.getLogger()
log.addHandler(logging.FileHandler(os.path.join(os.path.dirname(__file__), 'rabbit.log')))

from ui import UI

ui = UI()
ui.run()

