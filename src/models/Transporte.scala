package models

case class Transporte(
                       transporteId: Int,
                       vehiculo: String,
                       modalidadTransporte: String,
                       servicioIdFk: Int
                     )
