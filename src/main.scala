import models.Veterinario
import services.{AnimalService, ContratoService, ProvAlimentoService, ProvComplementarioService, ProveedorService, ServicioService, VeterinarioService}
import utils.{DatabaseConnection, Utils}

import java.sql.Date
import scala.util.{Failure, Success}

@main
def main(): Unit =
  val cont = AnimalService.deleteAnimal("14d6e21d-06e5-4472-8c6c-f0f7ea39e8ec")
  //cont.foreach(println)
  println(cont)