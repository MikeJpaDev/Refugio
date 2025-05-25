package services
import models.{Proveedor, ProveedorAlimento, ProveedorComplementario, Veterinario}
import utils.DatabaseConnection

object ProveedorService {
  def getAllProveedor(): List[Proveedor] = DatabaseConnection.withConnection {conn =>
    val statement = conn.createStatement()
    val resultSet = statement.executeQuery("SELECT * FROM proveedor pv JOIN provincia pr on pv.provincia_fk = pr.provincia_id JOIN tipo_proveedor tp on tp.id = pv.tipo_proveedor_fk LEFT JOIN proveedor_veterinario pvt on pvt.proveedor_id = pv.proveedor_id LEFT JOIN proveedor_complementario pc on pc.proveedor_id = pv.proveedor_id LEFT JOIN proveedor_alimento pa on pa.proveedor_id = pv.proveedor_id LEFT JOIN clinica c on pvt.clinica_fk = c.id;");

    Iterator.continually(resultSet)
      .takeWhile(_.next())
      .map(rs => {
        val tipoProv = rs.getInt("tipo_proveedor_fk")

        val camposBase = (
          rs.getString("proveedor_id"),
          rs.getString("nombre_proveedor"),
          rs.getString("direccion"),
          rs.getString("telefono"),
          rs.getString("email"),
          rs.getString("provincia_nombre"),
          rs.getString("responsable")
        )

        tipoProv match {
          case 1 => Veterinario(
            camposBase._1,
            camposBase._2,
            camposBase._3,
            camposBase._4,
            camposBase._5,
            camposBase._6,
            camposBase._7,
            rs.getString("especialidad"),
            rs.getString("modalidad"),
            rs.getString("nombre_clinica")
          )
          case 2 => ProveedorAlimento(
            camposBase._1,
            camposBase._2,
            camposBase._3,
            camposBase._4,
            camposBase._5,
            camposBase._6,
            camposBase._7,
            rs.getString("tipo_alimento")
          )
          case 3 => ProveedorComplementario(
            camposBase._1,
            camposBase._2,
            camposBase._3,
            camposBase._4,
            camposBase._5,
            camposBase._6,
            camposBase._7,
            rs.getString("tipo_complementario")
          )

          case _ => throw new IllegalArgumentException(
            s"Tipo de proveedor desconocido: $tipoProv"
          )
        }
      }).toList
  }
}
