package utils

import models.{Proveedor, Veterinario}

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
      (nombre.isEmpty || v.nombre_proveedor.toLowerCase.contains(nombre.toLowerCase)) &&
        (direccion.isEmpty || v.direccion.toLowerCase.contains(direccion.toLowerCase)) &&
        (tel.isEmpty || v.telefono.contains(tel)) &&
        (email.isEmpty || v.email.toLowerCase.contains(email.toLowerCase))
    }
    
    filtrados.asJava
  }

  def provFilter(
                  lista: java.util.List[Proveedor],
                  nombre: String,
                  direccion: String,
                  tel: String,
                  email: String
               ): java.util.List[Proveedor] = {

    val scalaList = lista.asScala.toList

    val filtrados = scalaList.filter { v =>
      (nombre.isEmpty || v.nombre_proveedor.toLowerCase.contains(nombre.toLowerCase)) &&
        (direccion.isEmpty || v.direccion.toLowerCase.contains(direccion.toLowerCase)) &&
        (tel.isEmpty || v.telefono.contains(tel)) &&
        (email.isEmpty || v.email.toLowerCase.contains(email.toLowerCase))
    }

    filtrados.asJava
  }
}
