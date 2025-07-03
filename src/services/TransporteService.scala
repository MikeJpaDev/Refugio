package services
import models.{Transporte, TransporteTable}
import utils.DatabaseConnection

import java.sql.{PreparedStatement, ResultSet, SQLException, Statement}
import scala.collection.JavaConverters.*
import scala.util.{Try, Using}

object TransporteService {
  def getAllTransportes: java.util.List[Transporte] = {
    try {
      DatabaseConnection.withConnection { conn =>
        var stmt: Statement = null
        var rs: ResultSet = null

        try {
          stmt = conn.createStatement()
          rs = stmt.executeQuery("SELECT * FROM transporte")

          val transportes = scala.collection.mutable.ListBuffer[Transporte]()
          while (rs.next()) {
            transportes += Transporte(
              rs.getInt("transporte_id"),
              rs.getString("vehiculo"),
              rs.getString("modalidad_transporte"),
              rs.getInt("servicio_id_fk")
            )
          }
          transportes.toList.asJava
        } finally {
          if (rs != null) rs.close()
          if (stmt != null) stmt.close()
        }
      }
    } catch {
      case e: SQLException =>
        throw new SQLException(s"Error obteniendo transportes: ${e.getMessage}", e)
      case e: Exception =>
        throw new Exception(s"Error inesperado: ${e.getMessage}", e)
    }
  }

  def getAllTransportesTable: java.util.List[TransporteTable] = {
    try {
      DatabaseConnection.withConnection { conn =>
        var stmt: Statement = null
        var rs: ResultSet = null

        try {
          stmt = conn.createStatement()
          rs = stmt.executeQuery("SELECT * FROM v_transporte")

          val transportes = scala.collection.mutable.ListBuffer[TransporteTable]()
          while (rs.next()) {
            transportes += TransporteTable(
              rs.getInt("transporte_id"),
              rs.getString("vehiculo"),
              rs.getString("modalidad_transporte"),
              rs.getInt("servicio_id_fk"),
              rs.getString("tipo_servicio")
            )
          }
          transportes.toList.asJava
        } finally {
          if (rs != null) rs.close()
          if (stmt != null) stmt.close()
        }
      }
    } catch {
      case e: SQLException =>
        throw new SQLException(s"Error obteniendo transportes: ${e.getMessage}", e)
      case e: Exception =>
        throw new Exception(s"Error inesperado: ${e.getMessage}", e)
    }
  }
  
  def getTransporteById(transporteId: Int): Transporte = {
    try {
      DatabaseConnection.withConnection { conn =>
        val query = "SELECT * FROM transporte WHERE transporte_id = ?"
        var stmt: PreparedStatement = null
        var rs: ResultSet = null

        try {
          stmt = conn.prepareStatement(query)
          stmt.setInt(1, transporteId)
          rs = stmt.executeQuery()

          if (rs.next()) {
            Transporte(
              rs.getInt("transporte_id"),
              rs.getString("vehiculo"),
              rs.getString("modalidad_transporte"),
              rs.getInt("servicio_id_fk")
            )
          } else {
            throw new SQLException(s"Transporte con ID $transporteId no encontrado")
          }
        } finally {
          if (rs != null) rs.close()
          if (stmt != null) stmt.close()
        }
      }
    } catch {
      case e: SQLException =>
        throw new SQLException(s"Error obteniendo transporte: ${e.getMessage}", e)
      case e: Exception =>
        throw new Exception(s"Error inesperado obteniendo transporte: ${e.getMessage}", e)
    }
  }

  @throws(classOf[SQLException])
  def createTransporte(
                        vehiculo: String,
                        modalidadTransporte: String,
                        servicioIdFk: Int
                      ): Transporte = {
    try {
      DatabaseConnection.withConnection { conn =>
        val query =
          "INSERT INTO transporte (vehiculo, modalidad_transporte, servicio_id_fk) " +
            "VALUES (?, ?, ?) RETURNING transporte_id"
        var stmt: PreparedStatement = null
        var rs: ResultSet = null

        try {
          stmt = conn.prepareStatement(query)
          stmt.setString(1, vehiculo)
          stmt.setString(2, modalidadTransporte)
          stmt.setInt(3, servicioIdFk)

          rs = stmt.executeQuery()
          if (rs.next()) {
            val id = rs.getInt("transporte_id")
            Transporte(id, vehiculo, modalidadTransporte, servicioIdFk)
          } else {
            throw new SQLException("No se pudo obtener el ID del transporte creado")
          }
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
  
  def updateTransporte(
                        transporteId: Int,
                        vehiculo: String,
                        modalidadTransporte: String,
                        servicioIdFk: Int
                      ): Transporte = {
    try {
      DatabaseConnection.withConnection { conn =>
        val query =
          "UPDATE transporte SET vehiculo = ?, modalidad_transporte = ?, servicio_id_fk = ? WHERE transporte_id = ?"
        var stmt: PreparedStatement = null
        var rs: ResultSet = null

        try {
          stmt = conn.prepareStatement(query)
          stmt.setString(1, vehiculo)
          stmt.setString(2, modalidadTransporte)
          stmt.setInt(3, servicioIdFk)
          stmt.setInt(4, transporteId)

          val rowsAffected = stmt.executeUpdate()
          if (rowsAffected == 0) {
            throw new SQLException(s"Transporte con ID $transporteId no encontrado para actualización")
          }

          // Recuperar el registro actualizado
          val selectQuery = "SELECT * FROM transporte WHERE transporte_id = ?"
          stmt = conn.prepareStatement(selectQuery)
          stmt.setInt(1, transporteId)
          rs = stmt.executeQuery()

          if (rs.next()) {
            Transporte(
              rs.getInt("transporte_id"),
              rs.getString("vehiculo"),
              rs.getString("modalidad_transporte"),
              rs.getInt("servicio_id_fk")
            )
          } else {
            throw new SQLException(s"Transporte con ID $transporteId no encontrado después de actualizar")
          }
        } finally {
          if (rs != null) rs.close()
          if (stmt != null) stmt.close()
        }
      }
    } catch {
      case e: SQLException =>
        throw new SQLException(s"Error actualizando transporte: ${e.getMessage}", e)
      case e: Exception =>
        throw new Exception(s"Error inesperado actualizando transporte: ${e.getMessage}", e)
    }
  }
  
  def deleteTransporte(transporteId: Int): Boolean = {
    try {
      DatabaseConnection.withConnection { conn =>
        val query = "DELETE FROM transporte WHERE transporte_id = ?"
        var stmt: PreparedStatement = null

        try {
          stmt = conn.prepareStatement(query)
          stmt.setInt(1, transporteId)
          val rowsAffected = stmt.executeUpdate()
          rowsAffected > 0
        } finally {
          if (stmt != null) stmt.close()
        }
      }
    } catch {
      case e: SQLException if e.getSQLState == "23503" =>
        throw new SQLException("No se puede eliminar el transporte - existen registros vinculados", e)
      case e: SQLException =>
        throw new SQLException(s"Error eliminando transporte: ${e.getMessage}", e)
      case e: Exception =>
        throw new Exception(s"Error inesperado eliminando transporte: ${e.getMessage}", e)
    }
  }
}
