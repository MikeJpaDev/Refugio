package utils

import models.Veterinario

import javax.swing.JList
import scala.collection.JavaConverters.{asScalaBufferConverter, seqAsJavaListConverter}

object Filters {
  def vetFilter(
                 lista: java.util.List[Veterinario],
                 nombre: String,
                 direccion: String,
                 tel: String,
                 email: String
               ): java.util.List[Veterinario] = {

    val scalaList = lista.asScala.toList
    
    val filtrados = scalaList.filter { v =>
      (nombre.isEmpty || v.nombre_proveedor.contains(nombre)) &&
        (direccion.isEmpty || v.direccion.contains(direccion)) &&
        (tel.isEmpty || v.telefono.contains(tel)) &&
        (email.isEmpty || v.email.contains(email))
    }
    
    filtrados.asJava
  }
}
