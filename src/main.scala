import services.AnimalService
import utils.DatabaseConnection

@main
def main(): Unit =
  val allAnimal = AnimalService.getAllAnimal;
  allAnimal.foreach(println)
  println("hola")

