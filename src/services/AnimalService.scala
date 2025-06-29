package services

import models.Animal
import utils.DatabaseConnection

import java.sql.{CallableStatement, PreparedStatement, ResultSet, SQLException, Statement}
import java.util.{UUID, Date as UtilDate}
import java.sql.Date as SqlDate
import scala.collection.JavaConverters.{asJavaIterableConverter, seqAsJavaListConverter}
import scala.util.{Try, Using}

object AnimalService {
  def getAllAnimal: java.util.List[Animal] = {
    try {
      DatabaseConnection.withConnection { conn =>
        var stmt: Statement = null
        var rs: ResultSet = null

        try {
          stmt = conn.createStatement()
          rs = stmt.executeQuery(
            "SELECT animal_id, nombre_animal, raza, fecha_nacimiento, peso_kg, fecha_ingreso, especie_nombre " +
              "FROM animal a JOIN especie ON a.especie_fk = especie.especie_id"
          )

          val animales = new scala.collection.mutable.ListBuffer[Animal]()
          while (rs.next()) {
            animales += Animal(
              rs.getString("animal_id"),
              rs.getString("nombre_animal"),
              rs.getString("especie_nombre"),
              rs.getString("raza"),
              rs.getDate("fecha_nacimiento"),
              rs.getDouble("peso_kg"),
              rs.getDate("fecha_ingreso")
            )
          }
          animales.toList.asJava
        } finally {
          if (rs != null) rs.close()
          if (stmt != null) stmt.close()
        }
      }
    } catch {
      case e: SQLException =>
        throw new SQLException(s"Error al obtener todos los animales: ${e.getMessage}", e)
      case e: Exception =>
        throw new Exception(s"Error inesperado al obtener animales: ${e.getMessage}", e)
    }
  }

  @throws(classOf[SQLException])
  def createAnimal(
                    nombre_animal: String,
                    especie_id: Int,
                    raza: String,
                    fecha_nacimiento: UtilDate, // Cambiado a java.util.Date
                    peso_kg: Double,
                    fecha_ingreso: UtilDate // Cambiado a java.util.Date
                  ): Animal = {
    try {
      DatabaseConnection.withConnection { conn =>
        val query = "{call insert_animal(?, ?, ?, ?::Date, ?::numeric, ?::Date)}"
        var stmt: CallableStatement = null

        try {
          stmt = conn.prepareCall(query)
          stmt.setString(1, nombre_animal)
          stmt.setInt(2, especie_id)
          stmt.setString(3, raza)

          // Convertir java.util.Date a java.sql.Date
          stmt.setDate(4, new SqlDate(fecha_nacimiento.getTime))
          stmt.setBigDecimal(5, new java.math.BigDecimal(peso_kg))
          stmt.setDate(6, new SqlDate(fecha_ingreso.getTime))

          val rs = stmt.executeQuery()
          if (rs.next()) {
            Animal(
              rs.getString("animal_id"),
              rs.getString("nombre_animal"),
              rs.getString("nombre_especie"),
              rs.getString("raza"),
              rs.getDate("fecha_nacimiento"),
              rs.getDouble("peso_kg"),
              rs.getDate("fecha_ingreso")
            )
          } else {
            throw new SQLException("El procedimiento de inserción no devolvió resultados")
          }
        } finally {
          if (stmt != null) stmt.close()
        }
      }
    } catch {
      case e: SQLException =>
        throw new SQLException(s"Error al crear animal: ${e.getMessage}", e)
      case e: Exception =>
        throw new Exception(s"Error inesperado al crear animal: ${e.getMessage}", e)
    }
  }

  def updateAnimal(
                    animal: Animal,
                    especieFk: Int
                  ): Animal = {
    try {
      val uuid = UUID.fromString(animal.animal_id)
      DatabaseConnection.withConnection { conn =>
        val query = "{call public.update_animal(?, ?, ?, ?, ?::DATE, ?::NUMERIC, ?::DATE)}"
        var stmt: CallableStatement = null

        try {
          stmt = conn.prepareCall(query)
          stmt.setObject(1, uuid)
          stmt.setString(2, animal.nombre_animal)
          stmt.setInt(3, especieFk)
          stmt.setString(4, animal.raza)

          // Usar directamente java.sql.Date del modelo
          stmt.setDate(5, animal.fecha_nacimiento)
          stmt.setBigDecimal(6, new java.math.BigDecimal(animal.peso_kg))
          stmt.setDate(7, animal.fecha_ingreso)

          val rs = stmt.executeQuery()
          if (rs.next()) {
            Animal(
              rs.getString("animal_id"),
              rs.getString("nombre_animal"),
              rs.getString("especie_nombre"),
              rs.getString("raza"),
              rs.getDate("fecha_nacimiento"),
              rs.getDouble("peso_kg"),
              rs.getDate("fecha_ingreso")
            )
          } else {
            throw new SQLException(s"No se encontró el animal con ID ${animal.animal_id} para actualizar")
          }
        } finally {
          if (stmt != null) stmt.close()
        }
      }
    } catch {
      case e: IllegalArgumentException =>
        throw new IllegalArgumentException(s"ID de animal inválido: ${animal.animal_id}", e)
      case e: SQLException =>
        throw new SQLException(s"Error al actualizar animal: ${e.getMessage}", e)
      case e: Exception =>
        throw new Exception(s"Error inesperado al actualizar animal: ${e.getMessage}", e)
    }
  }

  def deleteAnimal(animalId: String): Animal = {
    try {
      val uuid = UUID.fromString(animalId)
      DatabaseConnection.withConnection { conn =>
        val query = "{call public.delete_animal(?)}"
        var stmt: CallableStatement = null

        try {
          stmt = conn.prepareCall(query)
          stmt.setObject(1, uuid)

          val rs = stmt.executeQuery()
          if (rs.next()) {
            Animal(
              rs.getString("animal_id"),
              rs.getString("nombre_animal"),
              rs.getString("especie_nombre"),
              rs.getString("raza"),
              rs.getDate("fecha_nacimiento"),
              rs.getDouble("peso_kg"),
              rs.getDate("fecha_ingreso")
            )
          } else {
            throw new SQLException(s"No se encontró el animal con ID $animalId para eliminar")
          }
        } finally {
          if (stmt != null) stmt.close()
        }
      }
    } catch {
      case e: IllegalArgumentException =>
        throw new IllegalArgumentException(s"ID de animal inválido: $animalId", e)
      case e: SQLException =>
        throw new SQLException(s"Error al eliminar animal: ${e.getMessage}", e)
      case e: Exception =>
        throw new Exception(s"Error inesperado al eliminar animal: ${e.getMessage}", e)
    }
  }

  def getAnimalById(animal_id: String): Animal = {
    try {
      val uuid = UUID.fromString(animal_id)
      DatabaseConnection.withConnection { conn =>
        val query = "SELECT * FROM animal a JOIN especie e ON a.especie_fk = e.especie_id WHERE a.animal_id = ?"
        var stmt: PreparedStatement = null
        var rs: ResultSet = null

        try {
          stmt = conn.prepareStatement(query)
          stmt.setObject(1, uuid)
          rs = stmt.executeQuery()

          if (rs.next()) {
            Animal(
              rs.getString("animal_id"),
              rs.getString("nombre_animal"),
              rs.getString("especie_nombre"),
              rs.getString("raza"),
              rs.getDate("fecha_nacimiento"),
              rs.getDouble("peso_kg"),
              rs.getDate("fecha_ingreso")
            )
          } else {
            throw new SQLException(s"Animal con ID $animal_id no encontrado")
          }
        } finally {
          if (rs != null) rs.close()
          if (stmt != null) stmt.close()
        }
      }
    } catch {
      case e: IllegalArgumentException =>
        throw new IllegalArgumentException(s"ID de animal inválido: $animal_id", e)
      case e: SQLException =>
        throw new SQLException(s"Error al obtener animal por ID: ${e.getMessage}", e)
      case e: Exception =>
        throw new Exception(s"Error inesperado al obtener animal: ${e.getMessage}", e)
    }
  }
}
