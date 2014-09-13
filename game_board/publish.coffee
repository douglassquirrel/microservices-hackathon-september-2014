context = require('rabbit.js').createContext('amqp://54.76.117.95:5672')

topic_exchange = 'combo'

pub = context.socket 'PUBLISH'
sub = context.socket 'SUBSCRIBE'

pub.connect pub

grid = [1..12].map -> [1..6].map -> ""

message =
  id: 12
  grid: grid

context.on 'ready', ->
  publisher = context.socket 'PUB', { routing: 'topic' }
  publisher.on 'error', (err) -> 
    console.log "Crashed: #{err}"
  publisher.connect topic_exchange, ->
    console.log 'publisher connected'
    publisher.publish 'board.created', (JSON.stringify message)
