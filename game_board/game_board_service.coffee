mg_server = 'amqp://54.76.117.95:5672';
context = require('rabbit.js').createContext(mg_server);
topic_exchange = 'combo';
game_grids = {}

columnSize = 7
rowSize = 12

gameStartedEventHandler = (publisher)-> 
    startSubscriber = context.socket('SUB', {routing: 'topic'})
    startSubscriber.connect(topic_exchange, 'game_started', ->
        console.log('subscribed to game_started')
    )

    startSubscriber.on('data', (message)->
        console.log("game_started #{message}")
        start_message = JSON.parse(message)

        row = [1..columnSize].map(->
            '')
        grid = [1..rowSize].map(->
            row.slice())
        game_grids[start_message.id] = grid
        console.log("Board Id: #{start_message.id}\n #{JSON.stringify(grid)}");

        publisher.publish('board_created', JSON.stringify(
            {
                id: start_message.id
                grid: grid
            }
        ));
    )

newLetterEventHandler = (publisher)->
    startSubscriber = context.socket('SUB', {routing: 'topic'})
    startSubscriber.connect(topic_exchange, 'letter_created', ->
        console.log('subscribed to letter_created')
    )
    startSubscriber.on('data', (message)->
        console.log("letter_created : #{message}")
        letter_message = JSON.parse(message)
        grid = game_grids[letter_message.id]
        grid[Math.ceil(columnSize/2)][rowSize]= letter_message.letter

        publisher.publish('board_updated', JSON.stringify(
            {
                id: letter_message.id
                grid: grid
            }
        ));
    )
    
context.on('ready',->

    publisher = context.socket('PUB',{routing:'topic'});
    publisher.on('error',(err)-> console.log("Crashed : #{err}"));
    publisher.connect(topic_exchange,->
        console.log('publisher connected')
    )
    
    gameStartedEventHandler(publisher)
    newLetterEventHandler(publisher)
    
)