var mg_server = 'amqp://54.76.117.95:5672';
var context = require('rabbit.js').create(mg_server);
var topic_exchange = 'combo';

context.on('ready', function() {
	
	var startSubscriber = context.socket('SUB', {routing: 'topic'});

    startSubscriber.connect(topic_exchange,'game_started', function(){
    	console.log('subscribe to game started has completed');
    });

}); 