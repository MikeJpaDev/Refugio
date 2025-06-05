import models.Veterinario
import org.mindrot.jbcrypt.BCrypt
import services.{ActividadService, AnimalService, ContratoService, ProvAlimentoService, ProvComplementarioService, ProveedorService, ServicioService, UserService, VeterinarioService}
import utils.{DatabaseConnection, Utils}

import java.sql.{Date, Timestamp}
import scala.util.{Failure, Success}

@main
def main(): Unit =
  val actividades = ActividadService.updateActividad("49c09e75-ea56-4b96-bab2-4c6f8c638c00", "1b26cbf1-d1cd-48d9-8cc6-dc4f368f7b9d", 19, "la misma de siemore",Timestamp.valueOf("2025-12-10 20:10:00"))
  println(actividades)