mg_server = 'amqp://54.76.117.95:5672';
context = require('rabbit.js').createContext(mg_server);
topic_exchange = 'combo';
games = {}

columnSize = 7
rowSize = 12

publishBoardUpdate = (publisher,gameId)->
    publisher.publish('board.updated', JSON.stringify(
        {
            id: gameId
            grid: games[gameId].grid
        }
    ));

moveLetterDown = (publisher,gameId)->
    console.log("moving down #{gameId}")
    letter_position = games[gameId].letter_possition;
    grid = games[gameId].grid
    
    if(letter_position.y<rowSize-1 && grid[letter_position.y + 1][letter_position.x] == "")
        grid[letter_position.y + 1][letter_position.x] = grid[letter_position.y][letter_position.x]
        grid[letter_position.y][letter_position.x] = ''
        letter_position.y++
        publishBoardUpdate(publisher,gameId)
    else
        clearInterval(games[gameId].moveDownTimer);
        games[gameId].letter_possition = null;
        publisher.publish("letter.landed",JSON.stringify({
            id:gameId
        }));
        
moveLetterLeft = (publisher,gameId)->
    console.log("moving left #{gameId}")
    letter_position = games[gameId].letter_possition;
    grid = games[gameId].grid
    if(letter_position.x>0)
        grid[letter_position.y][letter_position.x - 1 ] = grid[letter_position.y][letter_position.x]
        grid[letter_position.y][letter_position.x] = ''
        letter_position.x--
        publishBoardUpdate(publisher,gameId)

moveLetterRight = (publisher,gameId)->
    console.log("moving right #{gameId}")
    letter_position = games[gameId].letter_possition;
    grid = games[gameId].grid
    if(letter_position.x<columnSize)
        grid[letter_position.y][letter_position.x + 1 ] = grid[letter_position.y][letter_position.x]
        grid[letter_position.y][letter_position.x] = ''
        letter_position.x++
        publishBoardUpdate(publisher,gameId)

movementEventHandler = (publisher)->
    movementSubscriber = context.socket('SUB', {routing: 'topic'})
    movementSubscriber.connect(topic_exchange, 'game.key', ->
        console.log('subscribed to game.key')
    )
    movementSubscriber.on('data', (message)->
        console.log("game.key #{message}")
        movement_message = JSON.parse(message)
        if (movement_message.action == 'left')
            moveLetterLeft(publisher,movement_message.id)
        if (movement_message.action == 'right')
            moveLetterRight(publisher,movement_message.id)
    )
    
gameStartedEventHandler = (publisher)-> 
    startSubscriber = context.socket('SUB', {routing: 'topic'})
    startSubscriber.connect(topic_exchange, 'game.started', ->
        console.log('subscribed to game.started')
    )

    startSubscriber.on('data', (message)->
        console.log("game.started #{message}")
        start_message = JSON.parse(message)

        row = [1..columnSize].map(->
            '')
        grid = [1..rowSize].map(-> row.slice())

        games[start_message.id] = { grid: grid }
        console.log("Board Id: #{start_message.id}\n #{JSON.stringify(grid)}");

        publisher.publish('board.created', JSON.stringify(
            {
                id: start_message.id
                grid: grid
            }
        ));
    )

newLetterEventHandler = (publisher)->
    startSubscriber = context.socket('SUB', {routing: 'topic'})
    startSubscriber.connect(topic_exchange, 'letter.created', ->
        console.log('subscribed to letter.created')
    )
    startSubscriber.on('data', (message)->
        console.log("letter.created : #{message}")
        letter_message = JSON.parse(message)
        grid = games[letter_message.id].grid
        y = 0
        x = Math.ceil(columnSize/2) - 1
        console.log("Current letter #{grid[y]} #{y} #{x}");
        if ( grid[y][x] ==  "")
            console.log("New letter for #{letter_message.id}")

            grid[y][x]= letter_message.letter
            games[letter_message.id].letter_possition = { x:x, y:y }
            games[letter_message.id].moveDownTimer = setInterval(-> 
                moveLetterDown(publisher,letter_message.id)
            , 1000)
            
            publishBoardUpdate(publisher,letter_message.id)
            
        else
            console.log("Game ended #{letter_message.id}")
            publisher.publish('game.ended', JSON.stringify({
                id : letter_message.id
            }));
    )
    
context.on('ready',->

    publisher = context.socket('PUB',{routing:'topic'});
    publisher.on('error',(err)-> console.log("Crashed : #{err}"));
    publisher.connect(topic_exchange,->
        console.log('publisher connected')
    )
    
    gameStartedEventHandler(publisher)
    newLetterEventHandler(publisher)
    movementEventHandler(publisher)
)