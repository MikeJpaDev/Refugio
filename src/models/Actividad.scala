package models

import java.sql.{Date, Timestamp}

case class Actividad(
                    actividadid: String,
                    nombreAnimal: String,
                    animalId: String,
                    servicioId: Integer,
                    descripcion: String,
                    horario: Timestamp
                    )
