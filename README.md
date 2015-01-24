This is WebSocket Sample App.
=================================

This project is sample application of WebSocket.


# How to check behavior

1. Access here [http://www.websocket.org/echo.html](http://www.websocket.org/echo.html)
2. Input endpoint url `ws://limitless-tor-2644.herokuapp.com/socket` to `Location` and `Connect`.
3. Send any messages.

## Messages

### Janken

You can play janken.
If you send `Gu`, `Choki` and `Pa`, this application return result of Janken.

### Close connection.

If you send `close`, the connection will close.

### Other message

The application return a message you send.
