package services

import models.Animal
import utils.DatabaseConnection

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
}
