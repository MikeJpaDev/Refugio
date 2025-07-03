package models

import java.sql.Date

case class Adopcion(
                   adopcionId: String,
                   animalId: String,
                   nombreAnimal: String,
                   fecha:Date,
                   precio: Double
                   )
