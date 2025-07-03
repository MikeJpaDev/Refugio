package services
import models.{Actividad, Adopcion, Transporte}
import utils.{DatabaseConnection, Utils}

import java.beans.Statement
import java.sql.{Date, PreparedStatement, ResultSet, SQLException}
import scala.collection.mutable.ListBuffer

object AdopcionService {
  def getAllAdopcion: java.util.List[Adopcion] = {
    try {
      DatabaseConnection.withConnection{ conn =>
        val query = "SELECT * FROM v_adopcion;"
        var stmt: PreparedStatement = null
        var rs: ResultSet = null

        try {
          stmt = conn.prepareStatement(query)
          rs = stmt.executeQuery()

          val adopcion = new ListBuffer[Adopcion]
          while (rs.next()) {
            adopcion += Adopcion(
              rs.getString("adopcion_id"),
              rs.getString("animal_id"),
              rs.getString("nombre_animal"),
              rs.getDate("fecha_adopción"),
              rs.getDouble("precio")
            )
          }
          Utils.convertirScalaAJavaList(adopcion.toList)
        }  finally {
          if (rs != null) rs.close()
          if (stmt != null) stmt.close()
        }
      }
    }
    catch {
      case e: SQLException =>
        throw new SQLException(s"Error obteniendo adopciones: ${e.getMessage}", e)
      case e: Exception =>
        throw new Exception(s"Error inesperado obteniendo adopciones: ${e.getMessage}", e)
    }
  }

  // Crear una nueva adopción
  @throws(classOf[SQLException])
  def createAdopcion(
                      animalId: String,
                      fecha:Date,
                      precio: Double
                      ): Unit = {
    try {
      DatabaseConnection.withConnection { conn =>
        val query =
          "INSERT INTO adopcion (animal_id, fecha_adopcion, precio) " +
            "VALUES (?, ?, ?) RETURNING adopcion_id;"
        var stmt: PreparedStatement = null
        var rs: ResultSet = null

        try {
          stmt = conn.prepareStatement(query)
          stmt.setString(1, animalId)
          stmt.setDate(2, fecha)
          stmt.setDouble(3, precio)

          rs = stmt.executeQuery()
        } finally {
          if (rs != null) rs.close()
          if (stmt != null) stmt.close()
        }
      }
    } catch {
      case e: SQLException =>
        throw new SQLException(s"Error creando transporte: ${e.getMessage}", e)
      case e: Exception =>
        throw new Exception(s"Error inesperado creando transporte: ${e.getMessage}", e)
    }
  }
  
  
  // Eliminar una adopción
  def deleteAdopcion(adopcionId: String): Boolean = {
    try {
      DatabaseConnection.withConnection { conn =>
        val query = "DELETE FROM adopcion WHERE adopcion_id = ?"
        var stmt: PreparedStatement = null

        try {
          stmt = conn.prepareStatement(query)
          stmt.setString(1, adopcionId)
          val rowsAffected = stmt.executeUpdate()
          rowsAffected > 0
        } finally {
          if (stmt != null) stmt.close()
        }
      }
    } catch {
      case e: SQLException if e.getSQLState == "23503" =>
        throw new SQLException("No se puede eliminar la adopción - existen registros vinculados", e)
      case e: SQLException =>
        throw new SQLException(s"Error eliminando adopción: ${e.getMessage}", e)
      case e: Exception =>
        throw new Exception(s"Error inesperado eliminando adopción: ${e.getMessage}", e)
    }
  }
}
