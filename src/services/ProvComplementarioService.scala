package services

import models.ProveedorComplementario
import utils.DatabaseConnection

import java.sql.SQLException
import java.util.UUID
import scala.util.{Failure, Try, Using}

object ProvComplementarioService {
  def getAllComplementarios: List[ProveedorComplementario] = DatabaseConnection.withConnection { conn =>
    val query = "SELECT * FROM v_complementario"

    Using(conn.prepareStatement(query)) { stmt =>
      val rs = stmt.executeQuery()
      Iterator.continually(rs)
        .takeWhile(_.next())
        .map(rs => ProveedorComplementario(
          rs.getString("proveedor_id"),
          rs.getString("nombre_proveedor"),
          rs.getString("direccion"),
          rs.getString("telefono"),
          rs.getString("email"),
          rs.getString("provincia_nombre"),
          rs.getString("responsable"),
          rs.getString("tipo_complementario")
        )).toList
    }.getOrElse(List.empty)
  }

  def createComplementario(
                            nombreProveedor: String,
                            direccion: String,
                            telefono: String,
                            email: String,
                            provinciaId: Int,
                            responsable: String,
                            tipoComplementario: String
                          ): Try[Unit] = DatabaseConnection.withConnection { conn =>
    val query = "SELECT * FROM insert_proveedor_complementario(?, ?, ?, ?, ?, ?, ?)"

    Using(conn.prepareStatement(query)) { stmt =>
      stmt.setString(1, nombreProveedor)
      stmt.setString(2, direccion)
      stmt.setString(3, telefono)
      stmt.setString(4, email)
      stmt.setInt(5, provinciaId)
      stmt.setString(6, responsable)
      stmt.setString(7, tipoComplementario)

      stmt.executeQuery().close()
    }
  }


  def updateComplementario(
                            proveedorId: String,
                            nombre: String,
                            direccion: String,
                            telefono: String,
                            email: String,
                            provinciaId: Int,
                            responsable: String,
                            tipoComplementario: String
                          ): Try[Unit] = {
    Try(UUID.fromString(proveedorId)).flatMap { uuid =>
      DatabaseConnection.withConnection { conn =>
        val query = "{call public.update_proveedor_complementario(?, ?, ?, ?, ?, ?, ?, ?)}"

        Using(conn.prepareCall(query)) { stmt =>
          stmt.setObject(1, uuid)
          stmt.setString(2, nombre)
          stmt.setString(3, direccion)
          stmt.setString(4, telefono)
          stmt.setString(5, email)
          stmt.setInt(6, provinciaId)
          stmt.setString(7, responsable)
          stmt.setString(8, tipoComplementario)

          stmt.executeUpdate()
        }.map(_ => ())
      }
    }.recoverWith {
      case _: IllegalArgumentException =>
        Failure(new SQLException(s"UUID inválido: $proveedorId"))
      case e: SQLException =>
        Failure(new SQLException(s"Error actualizando proveedor complementario: ${e.getMessage}"))
    }
  }

  def getComplementarioById(proveedorId: String): Try[ProveedorComplementario] = {
    Try(UUID.fromString(proveedorId)).flatMap { uuid =>
      DatabaseConnection.withConnection { conn =>
        val query = "SELECT * FROM get_complementario_by_id(?)"

        Using(conn.prepareStatement(query)) { stmt =>
          stmt.setObject(1, uuid)
          val rs = stmt.executeQuery()

          if (rs.next()) {
            ProveedorComplementario(
              rs.getString("proveedor_id"),
              rs.getString("nombre_proveedor"),
              rs.getString("direccion"),
              rs.getString("telefono"),
              rs.getString("email"),
              rs.getString("provincia_nombre"),
              rs.getString("responsable"),
              rs.getString("tipo_complementario")
            )
          } else {
            throw new SQLException(s"Proveedor complementario no encontrado")
          }
        }
      }
    }.recover {
      case _: IllegalArgumentException =>
        throw new SQLException(s"Formato de UUID inválido: $proveedorId")
    }
  }
}
