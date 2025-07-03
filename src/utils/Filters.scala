package utils

import models.{Animal, Proveedor, Veterinario}

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

  def animalFilter(
                    lista: java.util.List[Animal],
                    nombre: String,
                    especie: String,
                    raza: String,
                ): java.util.List[Animal] = {

    val scalaList = lista.asScala.toList

    val filtrados = scalaList.filter { v =>
      (nombre.isEmpty || v.nombre_animal.toLowerCase.contains(nombre.toLowerCase)) &&
        (especie.isEmpty || v.especie_nombre.toLowerCase.contains(especie.toLowerCase)) &&
        (raza.isEmpty || v.raza.contains(raza))
    }

    filtrados.asJava
  }
}
