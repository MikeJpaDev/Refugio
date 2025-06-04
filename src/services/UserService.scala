package services
import models.User
import org.mindrot.jbcrypt.BCrypt
import utils.DatabaseConnection

import java.sql.{PreparedStatement, ResultSet, SQLException}
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

    def getUserByID(id: String): User = {
      try {
        DatabaseConnection.withConnection{ conn =>
          val query = "SELECT username, PASSWORD, r.rol as rol_name, id FROM users s LEFT JOIN roles r ON s.rol = r.id WHERE s.username = ?"
          var stmt: PreparedStatement = null
          var rs: ResultSet = null
          try {
            stmt = conn.prepareStatement(query)
            stmt.setString(1, id)
            rs = stmt.executeQuery()

            if (rs.next()) {
              User(
                rs.getString("username"),
                rs.getInt("id"),
                rs.getString("rol_name"),
                rs.getString("password")
              )
            }
            else {
              throw new SQLException(s"usuario con username $id no encontrado")
            }
          }
          finally {
            if (rs != null) rs.close()
            if (stmt != null) stmt.close()
          }
        }
      }
      catch {
        case e: SQLException =>
          throw new IllegalArgumentException("Error de Autentificación")
        case e: Exception =>
          throw new Exception(s"Error inesperado obteniendo usuario: ${e.getMessage}", e)
      }
    }

    def login(username: String, password: String): User = {
      try {
        val user = getUserByID(username)
        if (BCrypt.checkpw(password, user.passwordHash)){
          user
        }
        else {
          throw new IllegalArgumentException("Error de Autentificación")
        }
      }
      catch
        case e: IllegalArgumentException => throw e
        case e: SQLException => throw e
        case e: Exception => throw new InternalError("Error de servidor")
    }
}
