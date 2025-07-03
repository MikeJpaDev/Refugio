package services

import models.ProveedorComplementario
import utils.DatabaseConnection

import java.sql.{CallableStatement, PreparedStatement, ResultSet, SQLException}
import java.util.UUID
import scala.collection.JavaConverters.seqAsJavaListConverter
import scala.util.{Failure, Try, Using}

object ProvComplementarioService {
  def getAllComplementarios: java.util.List[ProveedorComplementario] = {
    try {
      DatabaseConnection.withConnection { conn =>
        val query = "SELECT * FROM v_complementario"
        var stmt: PreparedStatement = null
        var rs: ResultSet = null

        try {
          stmt = conn.prepareStatement(query)
          rs = stmt.executeQuery()

          val proveedores = new scala.collection.mutable.ListBuffer[ProveedorComplementario]()
          while (rs.next()) {
            proveedores += ProveedorComplementario(
              rs.getString("proveedor_id"),
              rs.getString("nombre_proveedor"),
              rs.getString("direccion"),
              rs.getString("telefono"),
              rs.getString("email"),
              rs.getString("provincia_nombre"),
              rs.getString("responsable"),
              rs.getString("tipo_complementario")
            )
          }
          proveedores.toList.asJava
        } finally {
          if (rs != null) rs.close()
          if (stmt != null) stmt.close()
        }
      }
    } catch {
      case e: SQLException =>
        throw new SQLException(s"Error obteniendo proveedores complementarios: ${e.getMessage}", e)
      case e: Exception =>
        throw new Exception(s"Error inesperado obteniendo proveedores complementarios: ${e.getMessage}", e)
    }
  }

  @throws(classOf[SQLException])
  def createComplementario(
                            nombreProveedor: String,
                            direccion: String,
                            telefono: String,
                            email: String,
                            provinciaId: Int,
                            responsable: String,
                            tipoComplementario: String
                          ): Unit = {
    try {
      DatabaseConnection.withConnection { conn =>
        val query = "SELECT * FROM insert_proveedor_complementario(?, ?, ?, ?, ?, ?, ?)"
        var stmt: PreparedStatement = null

        try {
          stmt = conn.prepareStatement(query)
          stmt.setString(1, nombreProveedor)
          stmt.setString(2, direccion)
          stmt.setString(3, telefono)
          stmt.setString(4, email)
          stmt.setInt(5, provinciaId)
          stmt.setString(6, responsable)
          stmt.setString(7, tipoComplementario)

          val rs = stmt.executeQuery()
          if (!rs.next()) {
            throw new SQLException("No se pudo crear el proveedor complementario: el procedimiento no devolvió resultados")
          }
        } finally {
          if (stmt != null) stmt.close()
        }
      }
    } catch {
      case e: SQLException =>
        throw new SQLException(s"Error creando proveedor complementario: ${e.getMessage}", e)
      case e: Exception =>
        throw new Exception(s"Error inesperado creando proveedor complementario: ${e.getMessage}", e)
    }
  }

  def updateComplementario(
                            proveedorId: String,
                            nombre: String,
                            direccion: String,
                            telefono: String,
                            email: String,
                            provinciaId: Int,
                            responsable: String,
                            tipoComplementario: String
                          ): Unit = {
    try {
      val uuid = UUID.fromString(proveedorId)
      DatabaseConnection.withConnection { conn =>
        val query = "{call public.update_proveedor_complementario(?, ?, ?, ?, ?, ?, ?, ?)}"
        var stmt: CallableStatement = null

        try {
          stmt = conn.prepareCall(query)
          stmt.setObject(1, uuid)
          stmt.setString(2, nombre)
          stmt.setString(3, direccion)
          stmt.setString(4, telefono)
          stmt.setString(5, email)
          stmt.setInt(6, provinciaId)
          stmt.setString(7, responsable)
          stmt.setString(8, tipoComplementario)

          val updatedRows = stmt.executeUpdate()
        } finally {
          if (stmt != null) stmt.close()
        }
      }
    } catch {
      case e: IllegalArgumentException =>
        throw new IllegalArgumentException(s"Formato de UUID inválido: $proveedorId", e)
      case e: SQLException =>
        throw new SQLException(s"Error actualizando proveedor complementario: ${e.getMessage}", e)
      case e: Exception =>
        throw new Exception(s"Error inesperado actualizando proveedor complementario: ${e.getMessage}", e)
    }
  }

  def getComplementarioById(proveedorId: String): ProveedorComplementario = {
    try {
      val uuid = UUID.fromString(proveedorId)
      DatabaseConnection.withConnection { conn =>
        val query = "SELECT * FROM get_complementario_by_id(?)"
        var stmt: PreparedStatement = null
        var rs: ResultSet = null

        try {
          stmt = conn.prepareStatement(query)
          stmt.setObject(1, uuid)
          rs = stmt.executeQuery()

          if (rs.next()) {
            ProveedorComplementario(
              rs.getString("proveedor_id"),
              rs.getString("nombre_proveedor"),
              rs.getString("direccion"),
              rs.getString("telefono"),
              rs.getString("email"),
              rs.getString("provincia_nombre"),
              rs.getString("responsable"),
              rs.getString("tipo_complementario")
            )
          } else {
            throw new SQLException(s"Proveedor complementario con ID $proveedorId no encontrado")
          }
        } finally {
          if (rs != null) rs.close()
          if (stmt != null) stmt.close()
        }
      }
    } catch {
      case e: IllegalArgumentException =>
        throw new IllegalArgumentException(s"Formato de UUID inválido: $proveedorId", e)
      case e: SQLException =>
        throw new SQLException(s"Error obteniendo proveedor complementario: ${e.getMessage}", e)
      case e: Exception =>
        throw new Exception(s"Error inesperado obteniendo proveedor complementario: ${e.getMessage}", e)
    }
  }
}
