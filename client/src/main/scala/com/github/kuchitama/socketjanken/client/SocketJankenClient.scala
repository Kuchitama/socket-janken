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
      val socket = new JankenSocket()
      val request = new ClientUpgradeRequest()

      println(s"start to connect ${uri}")
      client.connect(socket, uri, request)

      socket.behave{session =>
        val stdin = Source.stdin
        breakable{
          print("> ")
          for(input <- stdin.getLines()) {
            session.getRemote().sendStringByFuture(input).get(1, TimeUnit.SECONDS)
            if (input == "close") break
            print("> ")
          }
        }
      }

      socket.awaitClose()
    } finally {
      client.stop()
    }
  }

  /**
   * Socket to Janken
   */
  @WebSocket(maxTextMessageSize = 64 * 1024)
  private class JankenSocket() {
    private val connectionLatch = new CountDownLatch(1)
    private val closeLatch = new CountDownLatch(1)

    // TODO varェ…
    private var session: Option[Session] = None

    def awaitClose():Unit = {
      this.closeLatch.await()
    }

    private def close(): Unit = {
      session.foreach(_.close(StatusCode.NORMAL, "done."))
    }

    @OnWebSocketClose
    def onClose(statusCode: Int, reason: String): Unit = {
      println(s"connection closed: ${statusCode} - ${reason}")
      session = None
      closeLatch.countDown()
    }

    @OnWebSocketConnect
    def onConnect(session: Session): Unit = {
      this.session = Some(session)
      println(s"connected: ${session}")
      connectionLatch.countDown()
    }

    def behave(block: Session => Unit) = {
      connectionLatch.await() // wait connecting

      session.map(block)

      close()
    }

    @OnWebSocketMessage
    def onMessage(message: String): Unit = {
      // output message
      println(s"got message: ${message}")
    }
  }
}
