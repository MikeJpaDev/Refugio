package services

import models.ProveedorAlimento
import utils.DatabaseConnection

import java.sql.{CallableStatement, PreparedStatement, ResultSet, SQLException}
import java.util.UUID
import scala.jdk.CollectionConverters.SeqHasAsJava
import scala.util.{Failure, Try, Using}

object ProvAlimentoService {
  def getAllAlimentos: java.util.List[ProveedorAlimento] = {
    try {
      DatabaseConnection.withConnection { conn =>
        val query = "SELECT * FROM v_aliment"
        var stmt: PreparedStatement = null
        var rs: ResultSet = null

        try {
          stmt = conn.prepareStatement(query)
          rs = stmt.executeQuery()

          val proveedores = new scala.collection.mutable.ListBuffer[ProveedorAlimento]()
          while (rs.next()) {
            proveedores += ProveedorAlimento(
              rs.getString("proveedor_id"),
              rs.getString("nombre_proveedor"),
              rs.getString("direccion"),
              rs.getString("telefono"),
              rs.getString("email"),
              rs.getString("provincia_nombre"),
              rs.getString("responsable"),
              rs.getString("tipo_alimento")
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
        throw new SQLException(s"Error obteniendo proveedores de alimento: ${e.getMessage}", e)
      case e: Exception =>
        throw new Exception(s"Error inesperado obteniendo proveedores de alimento: ${e.getMessage}", e)
    }
  }

  def createProveedorAlimento(
                               nombre_proveedor: String,
                               direccion: String,
                               telefono: String,
                               email: String,
                               provincia: Int,
                               responsable: String,
                               tipo_alimento: String
                             ): Unit = {
    try {
      DatabaseConnection.withConnection { conn =>
        val query = "SELECT * FROM public.insert_proveedor_alimenticio(?, ?, ?, ?, ?, ?, ?)"
        var stmt: PreparedStatement = null

        try {
          stmt = conn.prepareStatement(query)
          stmt.setString(1, nombre_proveedor)
          stmt.setString(2, direccion)
          stmt.setString(3, telefono)
          stmt.setString(4, email)
          stmt.setInt(5, provincia)
          stmt.setString(6, responsable)
          stmt.setString(7, tipo_alimento)

          val rs = stmt.executeQuery()
          if (!rs.next()) {
            throw new SQLException("No se pudo crear el proveedor de alimento: el procedimiento no devolvió resultados")
          }
        } finally {
          if (stmt != null) stmt.close()
        }
      }
    } catch {
      case e: SQLException =>
        throw new SQLException(s"Error creando proveedor de alimento: ${e.getMessage}", e)
      case e: Exception =>
        throw new Exception(s"Error inesperado creando proveedor de alimento: ${e.getMessage}", e)
    }
  }

  def updateProveedorAlimento(
                               proveedorId: String,
                               nombre: String,
                               direccion: String,
                               telefono: String,
                               email: String,
                               provinciaId: Int,
                               responsable: String,
                               tipoAlimento: String
                             ): Unit = {
    try {
      val uuid = UUID.fromString(proveedorId)
      DatabaseConnection.withConnection { conn =>
        val query = "{call public.update_proveedor_alimento(?, ?, ?, ?, ?, ?, ?, ?)}"
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
          stmt.setString(8, tipoAlimento)

          val updatedRows = stmt.executeUpdate()
          if (updatedRows == 0) {
            throw new SQLException(s"No se encontró el proveedor de alimento con ID $proveedorId para actualizar")
          }
        } finally {
          if (stmt != null) stmt.close()
        }
      }
    } catch {
      case e: IllegalArgumentException =>
        throw new IllegalArgumentException(s"Formato de UUID inválido: $proveedorId", e)
      case e: SQLException =>
        throw new SQLException(s"Error actualizando proveedor de alimento: ${e.getMessage}", e)
      case e: Exception =>
        throw new Exception(s"Error inesperado actualizando proveedor de alimento: ${e.getMessage}", e)
    }
  }

  def getProvAlimentoById(proveedorId: String): ProveedorAlimento = {
    try {
      val uuid = UUID.fromString(proveedorId)
      DatabaseConnection.withConnection { conn =>
        val query = "SELECT * FROM get_alimento_by_id(?)"
        var stmt: PreparedStatement = null
        var rs: ResultSet = null

        try {
          stmt = conn.prepareStatement(query)
          stmt.setObject(1, uuid)
          rs = stmt.executeQuery()

          if (rs.next()) {
            ProveedorAlimento(
              rs.getString("proveedor_id"),
              rs.getString("nombre_proveedor"),
              rs.getString("direccion"),
              rs.getString("telefono"),
              rs.getString("email"),
              rs.getString("provincia_nombre"),
              rs.getString("responsable"),
              rs.getString("tipo_alimento")
            )
          } else {
            throw new SQLException(s"Proveedor de alimento con ID $proveedorId no encontrado")
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
        throw new SQLException(s"Error obteniendo proveedor de alimento: ${e.getMessage}", e)
      case e: Exception =>
        throw new Exception(s"Error inesperado obteniendo proveedor de alimento: ${e.getMessage}", e)
    }
  }
}
