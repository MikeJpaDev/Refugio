package services

import models.ProveedorAlimento
import utils.DatabaseConnection

import java.sql.SQLException
import java.util.UUID
import scala.util.{Failure, Try, Using}

object ProvAlimentoService {
  def getAllAlimentos: List[ProveedorAlimento] = DatabaseConnection.withConnection { conn =>
    val query = "SELECT * FROM v_aliment"

    Using(conn.prepareStatement(query)) { stmt =>
      val rs = stmt.executeQuery()
      Iterator.continually(rs)
        .takeWhile(_.next())
        .map(rs => ProveedorAlimento(
          rs.getString("proveedor_id"),
          rs.getString("nombre_proveedor"),
          rs.getString("direccion"),
          rs.getString("telefono"),
          rs.getString("email"),
          rs.getString("provincia_nombre"),
          rs.getString("responsable"),
          rs.getString("tipo_alimento")
        )).toList
    }.getOrElse(List.empty)
  }

  def createProveedorALimento(
                      nombre_proveedor: String,
                      direccion: String,
                      telefono: String,
                      email: String,
                      provincia: Int,
                      responsable: String,
                      tipo_alimento: String
                    ): Try[Unit] = DatabaseConnection.withConnection { conn =>
    val query = "SELECT * FROM public.insert_proveedor_alimenticio(?, ?, ?, ?, ?, ?, ?)"

    Using(conn.prepareStatement(query)) { stmt =>
      stmt.setString(1, nombre_proveedor)
      stmt.setString(2, direccion)
      stmt.setString(3, telefono)
      stmt.setString(4, email)
      stmt.setInt(5, provincia)
      stmt.setString(6, responsable)
      stmt.setString(7, tipo_alimento)

      stmt.executeQuery().close()
    }
  }

  def updateProveedorAlimento(
                      proveedorId: String,
                      nombre: String,
                      direccion: String,
                      telefono: String,
                      email: String,
                      provinciaId: Int,
                      responsable: String,
                      tipoAlimento: String
                    ): Try[Unit] = {
    Try(UUID.fromString(proveedorId)).flatMap { uuid =>
      DatabaseConnection.withConnection { conn =>
        val query = "{call public.update_proveedor_alimento(?, ?, ?, ?, ?, ?, ?, ?)}"

        Using(conn.prepareCall(query)) { stmt =>
          stmt.setObject(1, uuid)
          stmt.setString(2, nombre)
          stmt.setString(3, direccion)
          stmt.setString(4, telefono)
          stmt.setString(5, email)
          stmt.setInt(6, provinciaId)
          stmt.setString(7, responsable)
          stmt.setString(8, tipoAlimento)

          stmt.executeUpdate()
        }.map(_ => ())
      }
    }.recoverWith {
      case _: IllegalArgumentException =>
        Failure(new SQLException(s"UUID inválido: $proveedorId"))
      case e: SQLException =>
        Failure(new SQLException(s"Error actualizando proveedor alimento: ${e.getMessage}"))
    }
  }

  def getProvAlimentoById(proveedorId: String): Try[ProveedorAlimento] = {
    Try(UUID.fromString(proveedorId)).flatMap { uuid =>
      DatabaseConnection.withConnection { conn =>
        val query = "SELECT * FROM get_alimento_by_id(?)"

        Using(conn.prepareStatement(query)) { stmt =>
          stmt.setObject(1, uuid)
          
          val rs = stmt.executeQuery()

          if (rs.next()) {
            ProveedorAlimento(
              rs.getString("proveedor_id"),
              rs.getString("nombre_proveedor"),
              rs.getString("direccion"),
              rs.getString("telefono"),
              rs.getString("email"),
              rs.getString("provincia_nombre"),
              rs.getString("responsable"),
              rs.getString("tipo_alimento")
            )
          } else {
            throw new SQLException(s"Proveedor de alimento no encontrado")
          }
        }
      }
    }.recover {
      case _: IllegalArgumentException =>
        throw new SQLException(s"Formato de UUID inválido: $proveedorId")
    }
  }
}
