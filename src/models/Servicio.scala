package models

import java.util.UUID

case class Servicio(
                     servicioId: Int,
                     contratoId: Int,
                     proveedorId: String,
                     precioBase: Double,
                     recargo: Double,
                     diasDuracion: Int,
                     tipoServicio: String
                   )
