package com.github.kuchitama.socketjanken.client

import java.util.concurrent.{ TimeUnit, CountDownLatch }

import org.eclipse.jetty.websocket.api.util.WSURI
import org.eclipse.jetty.websocket.api.{ StatusCode, Session }
import org.eclipse.jetty.websocket.api.annotations.{ OnWebSocketMessage, OnWebSocketConnect, OnWebSocketClose, WebSocket }
import org.eclipse.jetty.websocket.client.{ ClientUpgradeRequest, WebSocketClient }

import scala.io.Source

object SocketJankenClient {
  lazy val endPoint = s"ws://limitless-tor-2644.herokuapp.com/socket"
  lazy val uri = WSURI.toWebsocket(endPoint)

  def main(args: Array[String]): Unit = {
    import scala.util.control.Breaks._

    val client = new WebSocketClient()
    try {
      client.start()
      val socket = new JankenSocket({session =>
        val stdin = Source.stdin
        breakable{
          print("> ")
          for(input <- stdin.getLines()) {
            session.getRemote().sendStringByFuture(input).get(2, TimeUnit.SECONDS)
            if (input == "close") break
            print("> ")
          }
        }
      })
      val request = new ClientUpgradeRequest()

      println(s"start to connect ${uri}")
      client.connect(socket, uri, request)

      socket.awaitClose()
    } finally {
      client.stop()
    }
  }

  /**
   * Socket to Janken
   */
  @WebSocket(maxTextMessageSize = 64 * 1024)
  private class JankenSocket(block: Session => Unit) {
    private val latch = new CountDownLatch(1)

    // TODO varェ…
    private var session: Option[Session] = None

    def awaitClose():Unit = {
      this.latch.await()
    }

    private def close(): Unit = {
      session.foreach(_.close(StatusCode.NORMAL, "done."))
    }

    @OnWebSocketClose
    def onClose(statusCode: Int, reason: String): Unit = {
      println(s"connection closed: ${statusCode} - ${reason}")
      session = None
      latch.countDown()
    }

    @OnWebSocketConnect
    def onConnect(session: Session): Unit = {
      this.session = Some(session)
      println(s"connected: ${session}")

      block(session)

      close()
    }

    @OnWebSocketMessage
    def onMessage(message: String): Unit = {
      // outut message
      println(s"got message: ${message}")
    }
  }
}
