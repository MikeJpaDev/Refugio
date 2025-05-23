package utils

import com.typesafe.config.ConfigFactory

import java.sql.{Connection, DriverManager}

object DatabaseConnection {
  private val config = ConfigFactory.load().getConfig("db")
  private val url = config.getString("url")
  private val user = config.getString("user")
  private val password = config.getString("password")

  Class.forName(config.getString("driver"))

  def getConnection: Connection = {
    DriverManager.getConnection(url, user, password);
  }

  def withConnection[A](block: Connection => A): A = {
    val conn = getConnection
    try {
      block(conn)
    } finally {
      conn.close()
    }
  }

}
