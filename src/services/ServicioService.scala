package services
import models.Servicio
import utils.{DatabaseConnection, Utils}

import java.sql.{CallableStatement, PreparedStatement, ResultSet, SQLException}
import java.util.UUID
import scala.util.{Try, Using}
object ServicioService {
  def getAllServicios: List[Servicio] = {
    try {
      DatabaseConnection.withConnection { conn =>
        val query = "SELECT * FROM servicio"
        var stmt: PreparedStatement = null
        var rs: ResultSet = null

        try {
          stmt = conn.prepareStatement(query)
          rs = stmt.executeQuery()

          val servicios = new scala.collection.mutable.ListBuffer[Servicio]()
          while (rs.next()) {
            servicios += Servicio(
              rs.getInt("servicio_id"),
              rs.getInt("contrato_id_fk"),
              rs.getString("proveedor_id_fk"),
              rs.getDouble("precio_base"),
              rs.getDouble("recargo"),
              rs.getInt("dias_duracion"),
              rs.getString("tipo_servicio")
            )
          }
          servicios.toList
        } finally {
          if (rs != null) rs.close()
          if (stmt != null) stmt.close()
        }
      }
    } catch {
      case e: SQLException =>
        throw new SQLException(s"Error obteniendo servicios: ${e.getMessage}", e)
      case e: Exception =>
        throw new Exception(s"Error inesperado: ${e.getMessage}", e)
    }
  }

  @throws(classOf[SQLException])
  def createServicio(
                      contratoId: Int,
                      proveedorId: String,
                      precioBase: Double,
                      recargo: Double,
                      diasDuracion: Int,
                      tipoServicio: String
                    ): Servicio = {
    try {
      val uuid = UUID.fromString(proveedorId)
      DatabaseConnection.withConnection { conn =>
        val query = "{call public.insert_servicio(?, ?, ?:: numeric, ?:: numeric, ?, ?)}"
        var stmt: CallableStatement = null

        try {
          stmt = conn.prepareCall(query)
          stmt.setInt(1, contratoId)
          stmt.setObject(2, uuid)
          stmt.setBigDecimal(3, new java.math.BigDecimal(precioBase))
          stmt.setBigDecimal(4, new java.math.BigDecimal(recargo))
          stmt.setInt(5, diasDuracion)
          stmt.setString(6, tipoServicio)

          val rs = stmt.executeQuery()
          if (rs.next()) {
            Servicio(
              rs.getInt("servicio_id"),
              rs.getInt("contrato_id_fk"),
              rs.getString("proveedor_id_fk"),
              rs.getDouble("precio_base"),
              rs.getDouble("recargo"),
              rs.getInt("dias_duracion"),
              rs.getString("tipo_servicio")
            )
          } else {
            throw new SQLException("No se pudo crear el servicio: el procedimiento no devolvió resultados")
          }
        } finally {
          if (stmt != null) stmt.close()
        }
      }
    } catch {
      case e: IllegalArgumentException =>
        throw new IllegalArgumentException(s"Formato de UUID inválido: $proveedorId", e)
      case e: SQLException =>
        throw new SQLException(s"Error creando servicio: ${e.getMessage}", e)
      case e: Exception =>
        throw new Exception(s"Error inesperado creando servicio: ${e.getMessage}", e)
    }
  }

  def updateServicio(
                      servicioId: Int,
                      precioBase: Double,
                      recargo: Double,
                      diasDuracion: Int,
                      tipoServicio: String
                    ): Servicio = {
    try {
      DatabaseConnection.withConnection { conn =>
        val query = "{call public.update_servicio(?, ?:: numeric, ?:: numeric, ?, ?)}"
        var stmt: CallableStatement = null

        try {
          stmt = conn.prepareCall(query)
          stmt.setInt(1, servicioId)
          stmt.setBigDecimal(2, new java.math.BigDecimal(precioBase))
          stmt.setBigDecimal(3, new java.math.BigDecimal(recargo))
          stmt.setInt(4, diasDuracion)
          stmt.setString(5, tipoServicio)

          val rs = stmt.executeQuery()
          if (rs.next()) {
            Servicio(
              rs.getInt("servicio_id"),
              rs.getInt("contrato_id_fk"),
              rs.getString("proveedor_id_fk"),
              rs.getDouble("precio_base"),
              rs.getDouble("recargo"),
              rs.getInt("dias_duracion"),
              rs.getString("tipo_servicio")
            )
          } else {
            throw new SQLException(s"Servicio con ID $servicioId no encontrado para actualización")
          }
        } finally {
          if (stmt != null) stmt.close()
        }
      }
    } catch {
      case e: SQLException =>
        throw new SQLException(s"Error actualizando servicio: ${e.getMessage}", e)
      case e: Exception =>
        throw new Exception(s"Error inesperado actualizando servicio: ${e.getMessage}", e)
    }
  }

  def deleteServicio(servicioId: Int): Boolean = {
    try {
      DatabaseConnection.withConnection { conn =>
        val query = "{call public.delete_servicio(?)}"
        var stmt: CallableStatement = null

        try {
          stmt = conn.prepareCall(query)
          stmt.setInt(1, servicioId)
          val rs = stmt.executeQuery()
          rs.next() // Devuelve true si se afectó al menos un registro
        } finally {
          if (stmt != null) stmt.close()
        }
      }
    } catch {
      case e: SQLException if e.getSQLState == "23503" =>
        throw new SQLException("No se puede eliminar el servicio - existen registros vinculados", e)
      case e: SQLException =>
        throw new SQLException(s"Error eliminando servicio: ${e.getMessage}", e)
      case e: Exception =>
        throw new Exception(s"Error inesperado eliminando servicio: ${e.getMessage}", e)
    }
  }

  def getServicioById(servicioId: Int): Servicio = {
    try {
      DatabaseConnection.withConnection { conn =>
        val query = "SELECT * FROM servicio WHERE servicio_id = ?"
        var stmt: PreparedStatement = null
        var rs: ResultSet = null

        try {
          stmt = conn.prepareStatement(query)
          stmt.setInt(1, servicioId)
          rs = stmt.executeQuery()

          if (rs.next()) {
            Servicio(
              rs.getInt("servicio_id"),
              rs.getInt("contrato_id_fk"),
              rs.getString("proveedor_id_fk"),
              rs.getDouble("precio_base"),
              rs.getDouble("recargo"),
              rs.getInt("dias_duracion"),
              rs.getString("tipo_servicio")
            )
          } else {
            throw new SQLException(s"Servicio con ID $servicioId no encontrado")
          }
        } finally {
          if (rs != null) rs.close()
          if (stmt != null) stmt.close()
        }
      }
    } catch {
      case e: SQLException =>
        throw new SQLException(s"Error obteniendo servicio: ${e.getMessage}", e)
      case e: Exception =>
        throw new Exception(s"Error inesperado obteniendo servicio: ${e.getMessage}", e)
    }
  }

  def getAllServicioByContrato(contratoId: Int, proveedorId: String): java.util.List[Servicio] = {
    try {
      val uuid = UUID.fromString(proveedorId)
      DatabaseConnection.withConnection { conn =>
        val query = "SELECT * FROM servicio WHERE contrato_id_fk = ? AND proveedor_id_fk = ?"
        var stmt: PreparedStatement = null
        var rs: ResultSet = null

        try {
          stmt = conn.prepareStatement(query)
          stmt.setInt(1, contratoId)
          stmt.setObject(2, uuid)
          rs = stmt.executeQuery()

          val servicios = new scala.collection.mutable.ListBuffer[Servicio]()
          while (rs.next()) {
            servicios += Servicio(
              rs.getInt("servicio_id"),
              rs.getInt("contrato_id_fk"),
              rs.getString("proveedor_id_fk"),
              rs.getDouble("precio_base"),
              rs.getDouble("recargo"),
              rs.getInt("dias_duracion"),
              rs.getString("tipo_servicio")
            )
          }
          Utils.convertirScalaAJavaList(servicios.toList)
        } finally {
          if (rs != null) rs.close()
          if (stmt != null) stmt.close()
        }
      }
    } catch {
      case e: IllegalArgumentException =>
        throw new IllegalArgumentException(s"Formato de UUID inválido: $proveedorId", e)
      case e: SQLException =>
        throw new SQLException(s"Error obteniendo servicios por contrato: ${e.getMessage}", e)
      case e: Exception =>
        throw new Exception(s"Error inesperado obteniendo servicios por contrato: ${e.getMessage}", e)
    }
  }
}
