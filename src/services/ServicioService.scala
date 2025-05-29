package services
import models.Servicio
import utils.DatabaseConnection

import java.sql.SQLException
import java.util.UUID
import scala.util.{Try, Using}
object ServicioService {
  def getAllServicios: List[Servicio] = DatabaseConnection.withConnection { conn =>
    val query = "SELECT * FROM servicio"

    Using(conn.prepareStatement(query)) { stmt =>
      val rs = stmt.executeQuery()
      Iterator.continually(rs)
        .takeWhile(_.next())
        .map(rs => Servicio(
          rs.getInt("servicio_id"),
          rs.getInt("contrato_id_fk"),
          rs.getString("proveedor_id_fk"),
          rs.getDouble("precio_base"),
          rs.getDouble("recargo"),
          rs.getInt("dias_duracion"),
          rs.getString("tipo_servicio")
        )).toList
    }.getOrElse(List.empty)
  }

  def createServicio(
                      contratoId: Int,
                      proveedorId: String,
                      precioBase: Double,
                      recargo: Double,
                      diasDuracion: Int,
                      tipoServicio: String
                    ): Try[Servicio] = DatabaseConnection.withConnection { conn =>
    val query = "{call public.insert_servicio(?, ?, ?:: numeric, ?:: numeric, ?, ?)}"

    Try(UUID.fromString(proveedorId)).flatMap{ uuid =>
      Using(conn.prepareCall(query)) { stmt =>
        stmt.setInt(1, contratoId)
        stmt.setObject(2, uuid)
        stmt.setBigDecimal(3, new java.math.BigDecimal(precioBase))
        stmt.setBigDecimal(4, new java.math.BigDecimal(recargo))
        stmt.setInt(5, diasDuracion)
        stmt.setString(6, tipoServicio)

        val rs = stmt.executeQuery()
        if (rs.next()) {
          Servicio(
            rs.getInt("servicio_id"),
            rs.getInt("contrato_id_fk"),
            rs.getString("proveedor_id_fk"),
            rs.getDouble("precio_base"),
            rs.getDouble("recargo"),
            rs.getInt("dias_duracion"),
            rs.getString("tipo_servicio")
          )
        } else {
          throw new SQLException("Error creando servicio")
        }
      }
    }
  }


  def updateServicio(
                      servicioId: Int,
                      precioBase: Double,
                      recargo: Double,
                      diasDuracion: Int,
                      tipoServicio: String
                    ): Try[Servicio] = DatabaseConnection.withConnection { conn =>
    val query = "{call public.update_servicio(?, ?:: numeric, ?:: numeric, ?, ?)}"

    Using(conn.prepareCall(query)) { stmt =>
      stmt.setInt(1, servicioId)
      stmt.setBigDecimal(2, new java.math.BigDecimal(precioBase))
      stmt.setBigDecimal(3, new java.math.BigDecimal(recargo))
      stmt.setInt(4, diasDuracion)
      stmt.setString(5, tipoServicio)

      val rs = stmt.executeQuery()
      if (rs.next()) {
        Servicio(
          rs.getInt("servicio_id"),
          rs.getInt("contrato_id_fk"),
          rs.getString("proveedor_id_fk"),
          rs.getDouble("precio_base"),
          rs.getDouble("recargo"),
          rs.getInt("dias_duracion"),
          rs.getString("tipo_servicio")
        )
      } else {
        throw new SQLException("Servicio no encontrado")
      }
    }
  }

  def deleteServicio(servicioId: Int): Try[Boolean] = DatabaseConnection.withConnection { conn =>
    val query = "{call public.delete_servicio(?)}"

    Using(conn.prepareCall(query)) { stmt =>
      stmt.setInt(1, servicioId)
      val rs = stmt.executeQuery()
      rs.next()
    }
  }

  def getServicioById(servicioId: Int): Try[Servicio] = DatabaseConnection.withConnection { conn =>
    val query = "SELECT * FROM servicio WHERE servicio_id = ?"

    Using(conn.prepareStatement(query)) { stmt =>
      stmt.setInt(1, servicioId)
      val rs = stmt.executeQuery()

      if (rs.next()) {
        Servicio(
          rs.getInt("servicio_id"),
          rs.getInt("contrato_id_fk"),
          rs.getString("proveedor_id_fk"),
          rs.getDouble("precio_base"),
          rs.getDouble("recargo"),
          rs.getInt("dias_duracion"),
          rs.getString("tipo_servicio")
        )
      } else {
        throw new SQLException(s"Servicio con ID $servicioId no encontrado")
      }
    }
  }

  def getAllServicioByContrato(contratoId: Int, proveedorId: String): Try[List[Servicio]] = DatabaseConnection.withConnection { conn =>
    val query = "SELECT * FROM servicio WHERE contrato_id_fk = ? AND proveedor_id_fk = ?"

    Try(UUID.fromString(proveedorId)).flatMap{uuid =>
      Using(conn.prepareStatement(query)) { stmt =>
        stmt.setInt(1, contratoId)
        stmt.setObject(2, uuid)
        val rs = stmt.executeQuery()

        Iterator.continually(rs)
          .takeWhile(_.next())
          .map(rs => Servicio(
            rs.getInt("servicio_id"),
            rs.getInt("contrato_id_fk"),
            rs.getString("proveedor_id_fk"),
            rs.getDouble("precio_base"),
            rs.getDouble("recargo"),
            rs.getInt("dias_duracion"),
            rs.getString("tipo_servicio")
          )).toList
      }
    }
  }
}
