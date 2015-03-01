This is Client for WebSocket Sample App.
=================================

This module is client for [sample application of WebSocket](ws://limitless-tor-2644.herokuapp.com/socket).
This client read stdin and send it to the server app.

# How to run

1. clone this repository and move to `socket-janken` directory.
2. run activator `./activator`
3. change project to client. `project client`
4. run client project. `run`

## Messages

### Janken

You can play janken.
If you send `Gu`, `Choki` and `Pa`, this application return result of Janken.

### Close connection.

If you send `close`, the connection will close.

### Other message

The application return a message you send.
