#!/usr/bin/env coffee
# ^runs with CoffeeScript
# 
# listens to messages on all topics and prints to stdout
#
context = require('rabbit.js').createContext('amqp://54.76.117.95:5672')
topic_exchange = 'combo'

context.on 'ready', ->
  sub = context.socket 'SUB', {routing: 'topic'}
  sub.setEncoding 'utf8'
  sub.connect topic_exchange, '*', ->
    console.log 'subscribed to all messages'
  sub.on 'data', (message) ->
    #content = JSON.parse message
    console.log message
