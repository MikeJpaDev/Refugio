import models.Veterinario
import services.{AnimalService, ProveedorService, VeterinarioService}
import utils.{DatabaseConnection, Utils}

import java.sql.Date
import scala.util.{Failure, Success}

@main
def main(): Unit =
  val allAnimal = AnimalService.getAllAnimal;
  val proveedores = ProveedorService.getAllProveedor();
  allAnimal.foreach(println)
  println("hola")
  proveedores.foreach(println)
  // Crear fechas correctamente
  val fechaNacimiento = Utils.crearFecha("12-05-2020") // Asumiendo que retorna java.sql.Date
  val fechaIngreso = new java.sql.Date(System.currentTimeMillis())

  println(fechaNacimiento)
  println(fechaIngreso)
  // Llamar al servicio y manejar el Try
  val resultado = AnimalService.CreateAnimal(
    "Carletto",
    1,
    "Caballo",
    fechaNacimiento,
    12.23,
    fechaIngreso
  )

  // Manejar el resultado
  resultado match {
    case Success(animal) =>
      println(
        s"""
        Animal creado exitosamente:
        ID: ${animal.animal_id}
        Nombre: ${animal.nombre_animal}
        Fecha ingreso: ${animal.fecha_ingreso}
      """)

    case Failure(ex) =>
      println(s"Error al crear el animal: ${ex.getMessage}")
      ex.printStackTrace()
  }

  val resultadoDelete = AnimalService.deleteAnimal("0e9e854c-c22c-49ee-8026-e362b0627725")
  println(resultadoDelete)

  println(AnimalService.getAnimalById("ef243c5b-7612-4354-a599-7e8d759f74f8"))
  val vet = VeterinarioService.getAllVet
  vet.foreach(println)
  println("nuevo vet")
  println(VeterinarioService.createVet("Arturo", "afganistan", "76400063", "asdasdasd", 1, "asasdasd", "asddd", "sxccc", 1))