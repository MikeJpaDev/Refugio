import models.Veterinario
import org.mindrot.jbcrypt.BCrypt
import services.{AnimalService, ContratoService, ProvAlimentoService, ProvComplementarioService, ProveedorService, ServicioService, UserService, VeterinarioService}
import utils.{DatabaseConnection, Utils}

import java.sql.Date
import scala.util.{Failure, Success}

@main
def main(): Unit =
  println(UserService.getUserByID("admin"))
  println(UserService.login("asd", "admin"))
