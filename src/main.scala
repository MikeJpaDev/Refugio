import models.Veterinario
import services.{AnimalService, ContratoService, ProvAlimentoService, ProvComplementarioService, ProveedorService, ServicioService, VeterinarioService}
import utils.{DatabaseConnection, Utils}

import java.sql.Date
import scala.util.{Failure, Success}

@main
def main(): Unit =
  val vet = ContratoService.getAllContratos
  vet.foreach(println)