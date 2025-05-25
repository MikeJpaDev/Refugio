package models

import java.sql.Date

case class Animal(
animal_id: String,
nombre_animal: String,
especie_nombre:String,
raza:String,
fecha_nacimiento: Date,
peso_kg: Double,
fecha_ingreso: Date
)
