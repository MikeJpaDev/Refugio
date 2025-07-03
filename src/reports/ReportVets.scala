package reports

import java.sql.Date

case class ReportVets(
                       nombre_proveedor: String,
                       direccion: String,
                       provincia: String,
                       especialidad: String,
                       clinica: String,
                       fechaInicio: Date,
                       fechaFin: Date,
                       fechaConc: Date,
                       descripc: String
                     )
