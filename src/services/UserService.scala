package services
import models.User
import org.mindrot.jbcrypt.BCrypt
import utils.DatabaseConnection

import java.sql.SQLException
import scala.util.Using

object UserService {
  def createUser(
                  username: String,
                  plainPassword: String,
                  role_id: Int
                ): User = {
    val hashedPassword = BCrypt.hashpw(plainPassword, BCrypt.gensalt())

    try {
      DatabaseConnection.withConnection { conn =>
        val query = "{call public.create_user(?, ?, ?)}"
        Using.resource(conn.prepareCall(query)) { stmt =>
          stmt.setString(1, username)
          stmt.setString(2, hashedPassword)
          stmt.setInt(3, role_id)

          val rs = stmt.executeQuery()
          if (rs.next()) {
            User(
              rs.getString("username"),
              rs.getInt("rol_id"),
              rs.getString("rol"),
              rs.getString("password")
            )
          } else {
            throw new SQLException("Error al crear usuario: sin datos de retorno")
          }
        }
      }
    } catch {
      case e: SQLException =>
        throw new SQLException(s"Error en createUser: ${e.getMessage}", e)
    }
  }
}
