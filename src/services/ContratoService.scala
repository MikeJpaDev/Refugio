package services
import models.{Contrato, Servicio}
import utils.DatabaseConnection

import java.sql.{Date, SQLException}
import java.util.UUID
import scala.util.{Try, Using}

object ContratoService {
  def getAllContratos: List[Contrato] = DatabaseConnection.withConnection { conn =>
    val query = "SELECT * FROM contrato"

    Using(conn.prepareStatement(query)) { stmt =>
      val rs = stmt.executeQuery()
      Iterator.continually(rs)
        .takeWhile(_.next())
        .map(rs =>
          val contrato = rs.getInt("contrato_id")
          val  proveedor = rs.getString("proveedor_id")
          Contrato(
            contrato,
            proveedor,
            rs.getDate("fecha_inicio"),
            rs.getDate("fecha_fin"),
            rs.getDate("fecha_conciliacion"),
            rs.getString("descripcion"),
            ServicioService.getAllServicioByContrato(contrato, proveedor).getOrElse(List.empty)
        )).toList
    }.getOrElse(List.empty)
  }

  def createContrato(
                      proveedorId: String,
                      fechaInicio: Date,
                      fechaFin: Date,
                      fechaConciliacion: Date,
                      descripcion: String
                    ): Try[Contrato] = DatabaseConnection.withConnection { conn =>
    val query = "{call public.insert_contrato(?, ?, ?, ?, ?)}"

    Try(UUID.fromString(proveedorId)).flatMap { uuid =>
      Using(conn.prepareCall(query)) { stmt =>
        stmt.setObject(1, uuid)
        stmt.setDate(2, fechaInicio)
        stmt.setDate(3, fechaFin)
        stmt.setDate(4, fechaConciliacion)
        stmt.setString(5, descripcion)

        val rs = stmt.executeQuery()
        if (rs.next()) {
          Contrato(
            rs.getInt("contrato_id"),
            rs.getString("proveedor_id"),
            rs.getDate("fecha_inicio"),
            rs.getDate("fecha_fin"),
            rs.getDate("fecha_conciliacion"),
            rs.getString("descripcion")
          )
        } else {
          throw new SQLException("Error creando contrato")
        }
      }
    }
  }
}
