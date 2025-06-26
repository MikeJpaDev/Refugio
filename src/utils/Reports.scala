package utils

import net.sf.jasperreports.engine.{JasperExportManager, JasperFillManager}

import java.io.ByteArrayOutputStream
import java.util

object Reports {
  def generarReportePDF(jasperPath: String): Array[Byte] = {
    DatabaseConnection.withConnection { conn =>
      // 1. Cargar el reporte compilado (.jasper)
      val jasperStream = getClass.getResourceAsStream(jasperPath)
      if (jasperStream == null) {
        throw new RuntimeException(s"Reporte no encontrado: $jasperPath")
      }

      // 2. Parámetros del reporte (vacío si no se necesitan)
      val parameters = new util.HashMap[String, Any]()

      // 3. Llenar el reporte con datos usando tu conexión
      val jasperPrint = JasperFillManager.fillReport(jasperStream, parameters, conn)

      // 4. Exportar a PDF
      val outputStream = new ByteArrayOutputStream()
      JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream)

      outputStream.toByteArray
    }
  }
}
