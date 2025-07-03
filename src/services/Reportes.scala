package services

import models.Veterinario
import reports.ReportVets
import utils.{DatabaseConnection, Utils}

import java.sql.{PreparedStatement, ResultSet, SQLException}

object Reportes {
  def getRepVet: java.util.List[ReportVets] = {
    try {
      DatabaseConnection.withConnection { conn =>
        val query = "SELECT * FROM reporte_veterinarios"
        var stmt: PreparedStatement = null
        var rs: ResultSet = null

        try {
          stmt = conn.prepareStatement(query)
          rs = stmt.executeQuery()

          val veterinarios = new scala.collection.mutable.ListBuffer[ReportVets]()
          while (rs.next()) {
            veterinarios += ReportVets(
              rs.getString("nombre_proveedor"),
              rs.getString("direccion"),
              rs.getString("provincia_nombre"),
              rs.getString("especialidad"),
              rs.getString("nombre_clinica"),
              rs.getDate("fecha_inicio"),
              rs.getDate("fecha_fin"),
              rs.getDate("fecha_conciliacion"),
              rs.getString("descripcion")
            )
          }
          Utils.convertirScalaAJavaList(veterinarios.toList)
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
}
