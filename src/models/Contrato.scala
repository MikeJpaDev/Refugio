package models

import java.sql.Date

case class Contrato(
                     contratoId: Int,
                     proveedorId: String,
                     fechaInicio: Date,
                     fechaFin: Date,
                     fechaConciliacion: Date,
                     descripcion: String,
                     servicios: List[Servicio] = Nil
                   )
