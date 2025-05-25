package models

import java.sql.Date
import java.util.UUID

case class Contrato(
                     contratoId: Int,
                     proveedorId: UUID,
                     fechaInicio: Date,
                     fechaFin: Date,
                     fechaConciliacion: Date,
                     descripcion: String,
                     servicios: List[Servicio] = Nil
                   )
