package services
import models.{Contrato, Servicio}
import utils.DatabaseConnection

import java.sql.{CallableStatement, Date, PreparedStatement, ResultSet, SQLException}
import java.util.UUID
import scala.util.{Try, Using}

object ContratoService {
  def getAllContratos: List[Contrato] = {
    try {
      DatabaseConnection.withConnection { conn =>
        val query = "SELECT * FROM contrato"
        var stmt: PreparedStatement = null
        var rs: ResultSet = null

        try {
          stmt = conn.prepareStatement(query)
          rs = stmt.executeQuery()

          val contratos = new scala.collection.mutable.ListBuffer[Contrato]()
          while (rs.next()) {
            val contratoId = rs.getInt("contrato_id")
            val proveedorId = rs.getString("proveedor_id")

            contratos += Contrato(
              contratoId,
              proveedorId,
              rs.getDate("fecha_inicio"),
              rs.getDate("fecha_fin"),
              rs.getDate("fecha_conciliacion"),
              rs.getString("descripcion"),
              ServicioService.getAllServicioByContrato(contratoId, proveedorId)
            )
          }
          contratos.toList
        } finally {
          if (rs != null) rs.close()
          if (stmt != null) stmt.close()
        }
      }
    } catch {
      case e: SQLException =>
        throw new SQLException(s"Error obteniendo contratos: ${e.getMessage}", e)
      case e: Exception =>
        throw new Exception(s"Error inesperado obteniendo contratos: ${e.getMessage}", e)
    }
  }

  def createContrato(
                      proveedorId: String,
                      fechaInicio: Date,
                      fechaFin: Date,
                      fechaConciliacion: Date,
                      descripcion: String
                    ): Contrato = {
    try {
      val uuid = UUID.fromString(proveedorId)
      DatabaseConnection.withConnection { conn =>
        val query = "{call public.insert_contrato(?, ?, ?, ?, ?)}"
        var stmt: CallableStatement = null

        try {
          stmt = conn.prepareCall(query)
          stmt.setObject(1, uuid)
          stmt.setDate(2, fechaInicio)
          stmt.setDate(3, fechaFin)
          stmt.setDate(4, fechaConciliacion)
          stmt.setString(5, descripcion)

          val rs = stmt.executeQuery()
          if (rs.next()) {
            Contrato(
              rs.getInt("contrato_id"),
              rs.getString("proveedor_id"),
              rs.getDate("fecha_inicio"),
              rs.getDate("fecha_fin"),
              rs.getDate("fecha_conciliacion"),
              rs.getString("descripcion"),
              List.empty[Servicio] // Los servicios se crean aparte
            )
          } else {
            throw new SQLException("No se pudo crear el contrato: el procedimiento no devolvió resultados")
          }
        } finally {
          if (stmt != null) stmt.close()
        }
      }
    } catch {
      case e: IllegalArgumentException =>
        throw new IllegalArgumentException(s"Formato de UUID inválido: $proveedorId", e)
      case e: SQLException =>
        throw new SQLException(s"Error creando contrato: ${e.getMessage}", e)
      case e: Exception =>
        throw new Exception(s"Error inesperado creando contrato: ${e.getMessage}", e)
    }
  }

  def updateContrato(
                      contratoId: Int,
                      proveedorId: String,
                      fechaInicio: Date,
                      fechaFin: Date,
                      fechaConciliacion: Date,
                      descripcion: String
                    ): Unit = {
    try {
      val uuid = UUID.fromString(proveedorId)
      DatabaseConnection.withConnection { conn =>
        val query = "{call public.update_contrato(?, ?, ?, ?, ?, ?)}"
        var stmt: CallableStatement = null

        try {
          stmt = conn.prepareCall(query)
          stmt.setInt(1, contratoId)
          stmt.setObject(2, uuid)
          stmt.setDate(3, fechaInicio)
          stmt.setDate(4, fechaFin)
          stmt.setDate(5, fechaConciliacion)
          stmt.setString(6, descripcion)

          val updatedRows = stmt.executeUpdate()
          if (updatedRows == 0) {
            throw new SQLException(s"No se encontró el contrato con ID $contratoId para actualizar")
          }
        } finally {
          if (stmt != null) stmt.close()
        }
      }
    } catch {
      case e: IllegalArgumentException =>
        throw new IllegalArgumentException(s"Formato de UUID inválido: $proveedorId", e)
      case e: SQLException =>
        throw new SQLException(s"Error actualizando contrato: ${e.getMessage}", e)
      case e: Exception =>
        throw new Exception(s"Error inesperado actualizando contrato: ${e.getMessage}", e)
    }
  }

  def deleteContrato(contratoId: Int, proveedorId: String): Unit = {
    try {
      val uuid = UUID.fromString(proveedorId)
      DatabaseConnection.withConnection { conn =>
        val query = "{call public.delete_contrato(?, ?)}"
        var stmt: CallableStatement = null

        try {
          stmt = conn.prepareCall(query)
          stmt.setInt(1, contratoId)
          stmt.setObject(2, uuid)

          val updatedRows = stmt.executeUpdate()
        } finally {
          if (stmt != null) stmt.close()
        }
      }
    } catch {
      case e: IllegalArgumentException =>
        throw new IllegalArgumentException(s"Formato de UUID inválido: $proveedorId", e)
      case e: SQLException =>
        throw new SQLException(s"Error eliminando contrato: ${e.getMessage}", e)
      case e: Exception =>
        throw new Exception(s"Error inesperado eliminando contrato: ${e.getMessage}", e)
    }
  }

  def getContratoById(contratoId: Int, proveedorId: String): Contrato = {
    try {
      val uuid = UUID.fromString(proveedorId)
      DatabaseConnection.withConnection { conn =>
        val query = "SELECT * FROM public.get_contrato_by_id(?, ?)"
        var stmt: CallableStatement = null
        var rs: ResultSet = null

        try {
          stmt = conn.prepareCall(query)
          stmt.setInt(1, contratoId)
          stmt.setObject(2, uuid)
          rs = stmt.executeQuery()

          if (rs.next()) {
            Contrato(
              rs.getInt("contrato_id"),
              rs.getString("proveedor_id"),
              rs.getDate("fecha_inicio"),
              rs.getDate("fecha_fin"),
              rs.getDate("fecha_conciliacion"),
              rs.getString("descripcion"),
              ServicioService.getAllServicioByContrato(contratoId, proveedorId)
            )
          } else {
            throw new SQLException(s"Contrato con ID $contratoId y proveedor ID $proveedorId no encontrado")
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
        throw new SQLException(s"Error obteniendo contrato: ${e.getMessage}", e)
      case e: Exception =>
        throw new Exception(s"Error inesperado obteniendo contrato: ${e.getMessage}", e)
    }
  }
}
