package services

import models.Veterinario
import reports.{ReportAlim, ReportComp, ReportVetAct, ReportVets}
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

  def getRepAlim: java.util.List[ReportAlim] = {
    try {
      DatabaseConnection.withConnection { conn =>
        val query = "SELECT * FROM reporte_alim"
        var stmt: PreparedStatement = null
        var rs: ResultSet = null

        try {
          stmt = conn.prepareStatement(query)
          rs = stmt.executeQuery()

          val veterinarios = new scala.collection.mutable.ListBuffer[ReportAlim]()
          while (rs.next()) {
            veterinarios += ReportAlim(
              rs.getString("nombre_proveedor"),
              rs.getString("direccion"),
              rs.getString("provincia_nombre"),
              rs.getString("tipo_alimento"),
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

  def getRepComp: java.util.List[ReportComp] = {
    try {
      DatabaseConnection.withConnection { conn =>
        val query = "SELECT DISTINCT * FROM report_prov_comp"
        var stmt: PreparedStatement = null
        var rs: ResultSet = null

        try {
          stmt = conn.prepareStatement(query)
          rs = stmt.executeQuery()

          val veterinarios = new scala.collection.mutable.ListBuffer[ReportComp]()
          while (rs.next()) {
            veterinarios += ReportComp(
              rs.getDate("fecha_inicio"),
              rs.getDate("fecha_fin"),
              rs.getDate("fecha_conciliacion"),
              rs.getString("descripcion"),
              rs.getString("coalesce"),
              rs.getDouble("sum"),
              rs.getString("provincia_nombre")
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

  def getVetAct(prov: Int, clin: Int): java.util.List[ReportVetAct] = {
    try {
      DatabaseConnection.withConnection { conn =>
        val query = "SELECT * FROM listado_veterinarios_activos(?, ?);"
        var stmt: PreparedStatement = null
        var rs: ResultSet = null

        try {
          stmt = conn.prepareStatement(query)
          if (clin == -1)
            stmt.setNull(1, java.sql.Types.INTEGER)
          else {
            stmt.setInt(1, clin)
          }
          if (prov == -1)
            stmt.setNull(2, java.sql.Types.INTEGER)
          else {
            stmt.setInt(2, prov)
          }
        }
        rs = stmt.executeQuery()

        val veterinarios = new scala.collection.mutable.ListBuffer[ReportVetAct]()
        while (rs.next()) {
          veterinarios += ReportVetAct(
            rs.getString("nombre"),
            rs.getDate("fecha"),
            rs.getString("provincia"),
            rs.getString("especialidad"),
            rs.getString("clinica"),
            rs.getString("correo"),
            rs.getString("modalidad")
          )
        }
        Utils.convertirScalaAJavaList(veterinarios.toList)
      }
    } catch {
      case e: SQLException =>
        throw new SQLException(s"Error obteniendo veterinarios: ${e.getMessage}", e)
      case e: Exception =>
        throw new Exception(s"Error inesperado obteniendo veterinarios: ${e.getMessage}", e)
    }
  }
}
