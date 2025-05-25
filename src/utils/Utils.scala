package utils

import java.sql.Date
import java.text.SimpleDateFormat

object Utils {
  def crearFecha(fechaStr: String): java.sql.Date = {
    val formato = new java.text.SimpleDateFormat("dd-MM-yyyy")
    new java.sql.Date(formato.parse(fechaStr).getTime)
  }
}
