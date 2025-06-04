package services
import models.Actividad
import utils.DatabaseConnection

import java.sql.{PreparedStatement, ResultSet, SQLException}
import scala.collection.mutable.ListBuffer

object ActividadService {
  def getAllActividad: List[Actividad] = {
    try {
      DatabaseConnection.withConnection{ conn =>
        val query = "SELECT * FROM v_actividad"
        var stmt: PreparedStatement = null
        var rs: ResultSet = null

        try {
          stmt = conn.prepareStatement(query)
          rs = stmt.executeQuery()

          val actividad = new ListBuffer[Actividad]
          while (rs.next()) {
            actividad += Actividad(
              rs.getString("actividad_id"),
              rs.getString("nombre_animal"),
              rs.getString("animal_id_fk"),
              rs.getInt("servicio_id_fk"),
              rs.getString("descripcion"),
              rs.getTimestamp("horario")
            )
          }
          actividad.toList
        }  finally {
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
}
