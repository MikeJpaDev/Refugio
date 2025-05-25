package models
sealed trait Proveedor {
  def proveedor_id: String
  def nombre_proveedor: String
  def direccion: String
  def telefono: String
  def email: String
  def provincia: String
  def responsable: String
}

case class Veterinario(
                        proveedor_id: String,
                        nombre_proveedor: String,
                        direccion: String,
                        telefono: String,
                        email: String,
                        provincia: String,
                        responsable: String,
                        especialidad: String,
                        modalidad: String,
                        clinica: String
                      ) extends Proveedor

case class ProveedorAlimento (
                               proveedor_id: String,
                               nombre_proveedor: String,
                               direccion: String,
                               telefono: String,
                               email: String,
                               provincia: String,
                               responsable: String,
                               tipo_alimento: String
                             ) extends Proveedor

case class ProveedorComplementario (
                               proveedor_id: String,
                               nombre_proveedor: String,
                               direccion: String,
                               telefono: String,
                               email: String,
                               provincia: String,
                               responsable: String,
                               tipo_complementario: String
                             ) extends Proveedor