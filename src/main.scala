import models.Veterinario
import services.{AnimalService, ProvAlimentoService, ProvComplementarioService, ProveedorService, VeterinarioService}
import utils.{DatabaseConnection, Utils}

import java.sql.Date
import scala.util.{Failure, Success}

@main
def main(): Unit =
  //val vet = ProvComplementarioService.getAllComplementarios
  //vet.foreach(println)
  println("nuevo vet")
  println(ProvComplementarioService.createComplementario("ArturoElDuro", "afganistan", "76400063", "asdgghasdasdasdasd", 1, "asasdasd", "asddd"))
  println(ProvComplementarioService.updateComplementario("c967d5ab-f971-4b64-a5f4-fa4ffdbb0c35", "ArturoElDuroElSuperDUro", "afganistan", "76400063", "asdgghasdasdasdasd", 1, "asasdasd", "asddd"))
  println(ProvComplementarioService.getComplementarioById("c967d5ab-f971-4b64-a5f4-fa4ffdbb0c35"))