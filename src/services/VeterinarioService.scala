package services
import models.Veterinario
import utils.DatabaseConnection

import java.sql.{CallableStatement, PreparedStatement, ResultSet, SQLException}
import java.util.UUID
import scala.util.{Failure, Try, Using}
import scala.jdk.CollectionConverters._

object VeterinarioService {
  def getAllVet: java.util.List[Veterinario] = {
    try {
      DatabaseConnection.withConnection { conn =>
        val query = "SELECT * FROM v_vets"
        var stmt: PreparedStatement = null
        var rs: ResultSet = null

        try {
          stmt = conn.prepareStatement(query)
          rs = stmt.executeQuery()

          val veterinarios = new scala.collection.mutable.ListBuffer[Veterinario]()
          while (rs.next()) {
            veterinarios += Veterinario(
              rs.getString("proveedor_id"),
              rs.getString("nombre_proveedor"),
              rs.getString("direccion"),
              rs.getString("telefono"),
              rs.getString("email"),
              rs.getString("provincia_nombre"),
              rs.getString("responsable"),
              rs.getString("especialidad"),
              rs.getString("modalidad"),
              rs.getString("nombre_clinica")
            )
          }
          veterinarios.toList.asJava
        } finally {
          if (rs != null) rs.close()
          if (stmt != null) stmt.close()
        }
      }
    } catch {
      case e: SQLException =>
        throw new SQLException(s"Error obteniendo veterinarios: ${e.getMessage}", e)
      case e: Exception =>
        throw new Exception(s"Error inesperado obteniendo veterinarios: ${e.getMessage}", e)
    }
  }

  def createVet(
                 nombre_proveedor: String,
                 direccion: String,
                 telefono: String,
                 email: String,
                 provincia: Int,
                 responsable: String,
                 especialidad: String,
                 modalidad: String,
                 clinica: Int
               ): String = {
    try {
      DatabaseConnection.withConnection { conn =>
        val query = "SELECT * FROM public.insert_proveedor_veterinario(?, ?, ?, ?, ?, ?, ?, ?, ?)"
        var stmt: PreparedStatement = null

        try {
          stmt = conn.prepareStatement(query)
          stmt.setString(1, nombre_proveedor)
          stmt.setString(2, direccion)
          stmt.setString(3, telefono)
          stmt.setString(4, email)
          stmt.setInt(5, provincia)
          stmt.setString(6, responsable)
          stmt.setString(7, especialidad)
          stmt.setString(8, modalidad)
          stmt.setInt(9, clinica)

          val rs = stmt.executeQuery()
          if (!rs.next()) {
            throw new SQLException("No se pudo crear el veterinario: el procedimiento no devolvió resultados")
          }
          else
            rs.getString("provedor_id")
        } finally {
          if (stmt != null) stmt.close()
        }
      }
    } catch {
      case e: SQLException =>
        throw new SQLException(s"Error creando veterinario: ${e.getMessage}", e)
      case e: Exception =>
        throw new Exception(s"Error inesperado creando veterinario: ${e.getMessage}", e)
    }
  }

  def updateVeterinario(
                         proveedorId: String,
                         nombre: String,
                         direccion: String,
                         telefono: String,
                         email: String,
                         provinciaId: Int,
                         responsable: String,
                         especialidad: String,
                         modalidad: String,
                         clinicaId: Int
                       ): Unit = {
    try {
      val uuid = UUID.fromString(proveedorId)
      DatabaseConnection.withConnection { conn =>
        val query = "{call public.update_proveedor_veterinario(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}"
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
          stmt.setString(8, especialidad)
          stmt.setString(9, modalidad)
          stmt.setInt(10, clinicaId)

          val updatedRows = stmt.executeUpdate()
          if (updatedRows == 0) {
            throw new SQLException(s"No se encontró el veterinario con ID $proveedorId para actualizar")
          }
        } finally {
          if (stmt != null) stmt.close()
        }
      }
    } catch {
      case e: IllegalArgumentException =>
        throw new IllegalArgumentException(s"Formato de UUID inválido: $proveedorId", e)
      case e: SQLException =>
        throw new SQLException(s"Error actualizando veterinario: ${e.getMessage}", e)
      case e: Exception =>
        throw new Exception(s"Error inesperado actualizando veterinario: ${e.getMessage}", e)
    }
  }

  def getVeterinarioById(proveedorId: String): Veterinario = {
    try {
      val uuid = UUID.fromString(proveedorId)
      DatabaseConnection.withConnection { conn =>
        val query = "SELECT * FROM get_veterinario_by_id(?)"
        var stmt: PreparedStatement = null
        var rs: ResultSet = null

        try {
          stmt = conn.prepareStatement(query)
          stmt.setObject(1, uuid)
          rs = stmt.executeQuery()

          if (rs.next()) {
            Veterinario(
              rs.getString("proveedor_id"),
              rs.getString("nombre_proveedor"),
              rs.getString("direccion"),
              rs.getString("telefono"),
              rs.getString("email"),
              rs.getString("provincia_nombre"),
              rs.getString("responsable"),
              rs.getString("especialidad"),
              rs.getString("modalidad"),
              rs.getString("nombre_clinica")
            )
          } else {
            throw new SQLException(s"Veterinario con ID $proveedorId no encontrado")
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
        throw new SQLException(s"Error obteniendo veterinario: ${e.getMessage}", e)
      case e: Exception =>
        throw new Exception(s"Error inesperado obteniendo veterinario: ${e.getMessage}", e)
    }
  }
}
