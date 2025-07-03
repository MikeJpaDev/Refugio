import com.formdev.flatlaf.FlatDarkLaf
import com.formdev.flatlaf.themes.FlatMacLightLaf
import models.Veterinario
import org.mindrot.jbcrypt.BCrypt
import services.{ActividadService, AnimalService, ContratoService, ProvAlimentoService, ProvComplementarioService, ProveedorService, Reportes, ServicioService, TransporteService, UserService, VeterinarioService}
import utils.{DatabaseConnection, Utils}

import java.awt.Font
import java.sql.{Date, Timestamp}
import javax.swing.UIManager
import javax.swing.plaf.FontUIResource
import scala.util.{Failure, Success}

@main
def main(): Unit =
  println(Reportes.getRepVet)