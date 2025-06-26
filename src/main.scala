import com.formdev.flatlaf.FlatDarkLaf
import com.formdev.flatlaf.themes.FlatMacLightLaf
import models.Veterinario
import org.mindrot.jbcrypt.BCrypt
import services.{ActividadService, AnimalService, ContratoService, ProvAlimentoService, ProvComplementarioService, ProveedorService, ServicioService, UserService, VeterinarioService}
import ui.UI
import ui.jframes.VentanaPrincipal
import utils.{DatabaseConnection, Utils}

import java.awt.Font
import java.sql.{Date, Timestamp}
import javax.swing.UIManager
import javax.swing.plaf.FontUIResource
import scala.util.{Failure, Success}

@main
def main(): Unit =
  try {
    UI.setUIFont(new FontUIResource("Segoe UI", Font.PLAIN, 14))
    UIManager.setLookAndFeel(new FlatDarkLaf)
    java.awt.EventQueue.invokeLater(new Runnable() {
      override def run(): Unit = {
        new VentanaPrincipal().setVisible(true)
      }
    })
  } catch {
    case e: Exception =>
      e.printStackTrace()
  }