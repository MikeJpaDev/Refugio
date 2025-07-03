package reports

import java.sql.Date

case class ReportVetAct(
                         nombre_proveedor: String,
                         fecha: Date,
                         provincia: String,
                         especialidad: String,
                         clinica: String,
                         email: String,
                         modalidad: String
                       )
