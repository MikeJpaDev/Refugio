package reports

import java.sql.Date

case class ReportComp(
                       fechaInicio: Date,
                       fechaFin: Date,
                       fechaConc: Date,
                       descripc: String,
                       tipoServ:String,
                       suma: Double,
                       nombreProv: String
                     )
