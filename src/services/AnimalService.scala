package services

import models.Animal
import utils.DatabaseConnection

import java.sql.SQLException
import java.util.{Date, UUID}
import scala.util.{Try, Using}

object AnimalService {
  def getAllAnimal: List[Animal] = DatabaseConnection.withConnection { conn =>
    val statement = conn.createStatement()
    val resultSet = statement.executeQuery("SELECT animal_id, nombre_animal, raza, fecha_nacimiento, peso_kg, fecha_ingreso, especie_nombre FROM " +
      "animal a JOIN especie ON a.especie_fk = especie.especie_id;")

    Iterator.continually(resultSet)
      .takeWhile(_.next())
      .map(rs => Animal(
        rs.getString("animal_id"),
        rs.getString("nombre_animal"),
        rs.getString("especie_nombre"),
        rs.getString("raza"),
        rs.getDate("fecha_nacimiento"),
        rs.getDouble("peso_kg"),
        rs.getDate("fecha_ingreso")
      )).toList
  }

  def CreateAnimal(
                    nombre_animal: String,
                    especie_id: Int,
                    raza: String,
                    fecha_nacimiento: java.sql.Date,
                    peso_kg: Double,
                    fecha_ingreso: java.sql.Date): Try[Animal] = DatabaseConnection.withConnection { conn =>
    val query = "{call insert_animal(?, ?, ? , ?::Date, ?:: numeric, ?::Date)}"
    Using(conn.prepareCall(query)) { stmt =>
      stmt.setString(1, nombre_animal)
      stmt.setInt(2, especie_id)
      stmt.setString(3, raza)
      stmt.setDate(4, fecha_nacimiento)
      stmt.setBigDecimal(5, new java.math.BigDecimal(peso_kg))
      stmt.setDate(6, fecha_ingreso)

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
      }
      else {
        throw new SQLException("No se pudo crear el animal")
      }
    }
  }


  // En el objeto AnimalService
  def updateAnimal(
                    animal: Animal,
                    especieFk: Int
                  ): Try[Animal] = DatabaseConnection.withConnection { conn =>
    val query = "{call public.update_animal(?, ?, ?, ?, ?::DATE, ?::NUMERIC, ?::DATE)}"

    Using(conn.prepareCall(query)) { stmt =>
      stmt.setObject(1, java.util.UUID.fromString(animal.animal_id))
      stmt.setString(2, animal.nombre_animal)
      stmt.setInt(3, especieFk)
      stmt.setString(4, animal.raza)
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
        throw new SQLException("No se pudo actualizar el animal")
      }
    }
  }

  def deleteAnimal(animalId: String): Try[Animal] = DatabaseConnection.withConnection { conn =>
    val query = "{call public.delete_animal(?)}"

    Using(conn.prepareCall(query)) { stmt =>
      stmt.setObject(1, java.util.UUID.fromString(animalId)) // Conversión a UUID

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
        throw new SQLException("No se encontró el animal a eliminar")
      }
    }
  }

  def getAnimalById(animal_id: String): Try[Animal] = DatabaseConnection.withConnection { conn =>
    val query = "SELECT * FROM animal a JOIN especie e ON a.especie_fk = e.especie_id WHERE a.animal_id = ?"
    Try(UUID.fromString(animal_id)).flatMap {uuid =>
      Using(conn.prepareStatement(query)) { stmt =>
        stmt.setObject(1,uuid)
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
        }
        else
          throw new SQLException("No se encontro")
      }
    }
  }
}
