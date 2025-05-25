package models

import java.util.UUID

case class Servicio(
                     servicioId: Int,
                     contratoId: Int,
                     proveedorId: UUID,
                     precioBase: Double,
                     recargo: Double,
                     diasDuracion: Int,
                     tipoServicio: String
                   )
