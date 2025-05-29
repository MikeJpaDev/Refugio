package services
import models.Veterinario
import utils.DatabaseConnection

import scala.util.{Try, Using}

object VeterinarioService {
  def getAllVet: List[Veterinario] = DatabaseConnection.withConnection {conn =>
    val query = "SELECT * FROM v_vets"
    Using(conn.prepareStatement(query)) {stmt =>
      val rs = stmt.executeQuery()
      Iterator.continually(rs)
        .takeWhile(_.next())
        .map(rs => Veterinario(
          rs.getString("proveedor_id"),
          rs.getString("nombre_proveedor"),
          rs.getString("direccion"),
          rs.getString("telefono"),
          rs.getString("email"),
          rs.getString("provincia_nombre"),
          rs.getString("responsable"),
          rs.getString("especialidad"),
          rs.getString("modalidad"),
          rs.getString("nombre_clinica")
        )).toList
    }.getOrElse(List.empty)
  }

  def createVet(
                 nombre_proveedor: String,
                 direccion: String,
                 telefono: String,
                 email: String,
                 provincia: Int,
                 responsable: String,
                 especialidad: String,
                 modalidad: String,
                 clinica: Int
               ): Try[Unit] = DatabaseConnection.withConnection { conn =>
    val query = "SELECT * FROM public.insert_proveedor_veterinario(?, ?, ?, ?, ?, ?, ?, ?, ?)"

    Using(conn.prepareStatement(query)) { stmt =>
      stmt.setString(1, nombre_proveedor)
      stmt.setString(2, direccion)
      stmt.setString(3, telefono)
      stmt.setString(4, email)
      stmt.setInt(5, provincia)
      stmt.setString(6, responsable)
      stmt.setString(7, especialidad)
      stmt.setString(8, modalidad)
      stmt.setInt(9, clinica)
      
      stmt.executeQuery().close()
    }
  }
}
