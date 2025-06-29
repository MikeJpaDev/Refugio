package utils

import models.{Clinica, Especie, Provincia}

import java.sql.{Date, PreparedStatement, ResultSet, SQLException}
import java.text.SimpleDateFormat
import scala.collection.JavaConverters.seqAsJavaListConverter
import scala.collection.mutable.ListBuffer

object Utils {
  def crearFecha(fechaStr: String): java.sql.Date = {
    val formato = new java.text.SimpleDateFormat("dd-MM-yyyy")
    new java.sql.Date(formato.parse(fechaStr).getTime)
  }
  
  def getAllProvincias() : java.util.List[Provincia] = {
    try {
      DatabaseConnection.withConnection { conn =>
        val query = "SELECT * FROM provincia"
        var stmt: PreparedStatement = null
        var rs: ResultSet = null

        try {
          stmt = conn.prepareStatement(query)
          rs = stmt.executeQuery()

          val provincia = new ListBuffer[Provincia]
          while (rs.next()) {
            provincia += Provincia(
              rs.getString("provincia_nombre"),
              rs.getInt("provincia_id")
            )
          }
          provincia.toList.asJava
        } finally {
          if (rs != null) rs.close()
          if (stmt != null) stmt.close()
        }
      }
    }
    catch {
      case e: SQLException =>
        throw new SQLException(s"Error obteniendo actividades: ${e.getMessage}", e)
      case e: Exception =>
        throw new Exception(s"Error inesperado obteniendo actividades: ${e.getMessage}", e)
    }
  }

  def getAllEspecies(): java.util.List[Especie] = {
    try {
      DatabaseConnection.withConnection { conn =>
        val query = "SELECT * FROM especie"
        var stmt: PreparedStatement = null
        var rs: ResultSet = null

        try {
          stmt = conn.prepareStatement(query)
          rs = stmt.executeQuery()

          val especies = new ListBuffer[Especie]
          while (rs.next()) {
            especies += Especie(
              rs.getString("especie_nombre"),
              rs.getInt("especie_id")
            )
          }
          especies.toList.asJava
        } finally {
          if (rs != null) rs.close()
          if (stmt != null) stmt.close()
        }
      }
    }
    catch {
      case e: SQLException =>
        throw new SQLException(s"Error obteniendo especies: ${e.getMessage}", e)
      case e: Exception =>
        throw new Exception(s"Error inesperado obteniendo especies: ${e.getMessage}", e)
    }
  }

  def getAllClinicas(): java.util.List[Clinica] = {
    try {
      DatabaseConnection.withConnection { conn =>
        val query = "SELECT * FROM clinica"
        var stmt: PreparedStatement = null
        var rs: ResultSet = null

        try {
          stmt = conn.prepareStatement(query)
          rs = stmt.executeQuery()

          val clinicas = new ListBuffer[Clinica]
          while (rs.next()) {
            clinicas += Clinica(
              rs.getInt("id"),
              rs.getString("nombre_clinica")
            )
          }
          clinicas.toList.asJava
        } finally {
          if (rs != null) rs.close()
          if (stmt != null) stmt.close()
        }
      }
    }
    catch {
      case e: SQLException =>
        throw new SQLException(s"Error obteniendo especies: ${e.getMessage}", e)
      case e: Exception =>
        throw new Exception(s"Error inesperado obteniendo especies: ${e.getMessage}", e)
    }
  }

  def convertirScalaAJavaList[A](listaScala: List[A]): java.util.List[A] = {
    listaScala.asJava.asInstanceOf[java.util.List[A]]
  }
}
