package models

import java.sql.Date

case class ContratoTable(
                          contratoId: Int,
                          proveedorId: String,
                          proveedorName: String,
                          fechaInicio: Date,
                          fechaFin: Date,
                          fechaConciliacion: Date,
                          descripcion: String
                        )
