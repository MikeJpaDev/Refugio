package models

import java.sql.Timestamp

case class ActividadTable(
                           actividadid: String,
                           nombreAnimal: String,
                           animalId: String,
                           servicioId: Integer,
                           descripcion: String,
                           horario: Timestamp,
                           proveedorId: String,
                           proveedorNombre: String,
                           servicioDesc: String
                         )
