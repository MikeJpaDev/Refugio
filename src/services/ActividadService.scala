package services
import models.{Actividad, ActividadTable}
import utils.{DatabaseConnection, Utils}

import java.sql.{PreparedStatement, ResultSet, SQLException, Timestamp}
import java.util.UUID
import scala.collection.mutable.ListBuffer
import scala.util.Using

object ActividadService {
  def getAllActividad: java.util.List[Actividad] = {
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
          Utils.convertirScalaAJavaList(actividad.toList)
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

  def getAllActividadTable: java.util.List[ActividadTable] = {
    try {
      DatabaseConnection.withConnection { conn =>
        val query = "SELECT * FROM v_actividad_prov"
        var stmt: PreparedStatement = null
        var rs: ResultSet = null

        try {
          stmt = conn.prepareStatement(query)
          rs = stmt.executeQuery()

          val actividad = new ListBuffer[ActividadTable]
          while (rs.next()) {
            actividad += ActividadTable(
              rs.getString("actividad_id"),
              rs.getString("nombre_animal"),
              rs.getString("animal_id_fk"),
              rs.getInt("servicio_id_fk"),
              rs.getString("descripcion"),
              rs.getTimestamp("horario"),
              rs.getString("proveedor_id"),
              rs.getString("nombre_proveedor"),
              rs.getString("tipo_servicio")
            )
          }
          Utils.convertirScalaAJavaList(actividad.toList)
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

  @throws(classOf[SQLException])
  def createActividad(
                       animalId: String,
                       servicioId: Int,
                       description: String,
                       horario: Timestamp
                     ): Actividad = {
    try {
      val uuid = UUID.fromString(animalId)
      DatabaseConnection.withConnection { conn =>
        val query = "{call public.create_actividad(?, ?, ?, ?:: Timestamp)}"
        Using.resource(conn.prepareCall(query)) { stmt =>
          stmt.setObject(1, uuid)
          stmt.setInt(2, servicioId)
          stmt.setString(3, description)
          stmt.setTimestamp(4, horario)

          val rs = stmt.executeQuery()
          if (rs.next()) {
            Actividad(
              rs.getString("actividad_id"),
              AnimalService.getAnimalById(rs.getString("animal_id_fk")).nombre_animal,
              rs.getString("animal_id_fk"),
              rs.getInt("servicio_id_fk"),
              rs.getString("descripcion"),
              rs.getTimestamp("horario")
            )
          } else {
            throw new SQLException("No se pudo crear la actividad: sin datos de retorno")
          }
        }
      }
    } catch {
      case e: SQLException =>
        throw new SQLException(s"Error creando actividad: ${e.getMessage}", e)
    }
  }


  def deleteActividad(actividadId: String): Boolean = {
    try {
      val uuid = UUID.fromString(actividadId)
      DatabaseConnection.withConnection { conn =>
        val query = "{call public.delete_actividad(?)}"
        Using.resource(conn.prepareCall(query)) { stmt =>
          stmt.setObject(1, uuid)
          val rs = stmt.executeQuery()
          if (rs.next()) rs.getBoolean(1)
          else throw new InternalError("No se pudo retornar el valor")
        }
      }
    } catch {
      case e: SQLException =>
        throw new SQLException(s"Error eliminando actividad: ${e.getMessage}", e)
      case e: IllegalArgumentException =>
        throw new IllegalArgumentException(s"Error de entrada de datos: ${e.getMessage}")
      case e: Exception =>
        throw new InternalError(s"Error Inesperado: ${e.getMessage}")
    }
  }

  def getActividadById(actividadId: String): Actividad = {
    try {
      val uuid = UUID.fromString(actividadId)
      DatabaseConnection.withConnection { conn =>
        val query = "{call public.get_actividad_by_id(?)}"
        Using.resource(conn.prepareCall(query)) { stmt =>
          stmt.setObject(1, uuid)
          val rs = stmt.executeQuery()

          if (rs.next()) {
            Actividad(
              rs.getString("actividad_id"),
              AnimalService.getAnimalById(rs.getString("animal_id_fk")).nombre_animal,
              rs.getString("animal_id_fk"),
              rs.getInt("servicio_id_fk"),
              rs.getString("descripcion"),
              rs.getTimestamp("horario")
            )
          } else {
            throw InternalError("Error al obtener la entidad")
          }
        }
      }
    } catch {
      case e: SQLException =>
        throw new SQLException(s"Error obteniendo actividad: ${e.getMessage}", e)
    }
  }

  def updateActividad(
                       actividadId: String,
                       animalId: String,
                       servicioId: Int,
                       description: String,
                       horario: Timestamp
                     ): Actividad = {
    try {
      val uuidActividad = UUID.fromString(actividadId)
      val uuidAnimal = UUID.fromString(animalId)
      DatabaseConnection.withConnection { conn =>
        val query = "{call public.update_actividad(?, ?, ?, ?, ?)}"
        Using.resource(conn.prepareCall(query)) { stmt =>
          stmt.setObject(1, uuidActividad)
          stmt.setObject(2, uuidAnimal)
          stmt.setInt(3, servicioId)
          stmt.setString(4, description)
          stmt.setTimestamp(5, horario)

          val rs = stmt.executeQuery()
          if (rs.next()) {
            Actividad(
              rs.getString("actividad_id"),
              AnimalService.getAnimalById(rs.getString("animal_id_fk")).nombre_animal,
              rs.getString("animal_id_fk"),
              rs.getInt("servicio_id_fk"),
              rs.getString("descripcion"),
              rs.getTimestamp("horario")
            )
          }
          else throw new InternalError("Ocurrio un error al crear el objeto")
        }
      }
    } catch {
      case e: SQLException =>
        throw new SQLException(s"Error actualizando actividad: ${e.getMessage}", e)
    }
  }
}
