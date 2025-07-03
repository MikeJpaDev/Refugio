package reports

import java.sql.Date

case class ReportAlim(
                       nombre_proveedor: String,
                       direccion: String,
                       provincia: String,
                       tipoAlimento: String,
                       fechaInicio: Date,
                       fechaFin: Date,
                       fechaConc: Date,
                       descripc: String
                     )
