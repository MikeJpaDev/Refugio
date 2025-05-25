package models

import utils.DatabaseConnection
import scala.util.Using

object EspecieCache {
  private var cache: Map[Int, String] = Map.empty

  def refreshCache(): Unit = DatabaseConnection.withConnection { conn =>
    val nuevosDatos = Using(conn.createStatement()) { stmt =>
      val rs = stmt.executeQuery("SELECT * FROM especie")
      Iterator.continually(rs)
        .takeWhile(_.next())
        .map(rs => rs.getInt("especie_id") -> rs.getString("especie_nombre"))
        .toMap
    }.get // Manejar errores según necesidad
    cache = nuevosDatos
  }

  def getNombre(especieId: Int): String =
    cache.getOrElse(especieId, throw new NoSuchElementException(s"Especie $especieId no existe"))
}
