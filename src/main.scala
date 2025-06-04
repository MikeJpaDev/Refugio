import models.Veterinario
import org.mindrot.jbcrypt.BCrypt
import services.{ActividadService, AnimalService, ContratoService, ProvAlimentoService, ProvComplementarioService, ProveedorService, ServicioService, UserService, VeterinarioService}
import utils.{DatabaseConnection, Utils}

import java.sql.Date
import scala.util.{Failure, Success}

@main
def main(): Unit =
  val actividades = ActividadService.getAllActividad
  actividades.foreach(println)
