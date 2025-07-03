package models

case class TransporteTable(
                            transporteId: Int,
                            vehiculo: String,
                            modalidadTransporte: String,
                            servicioIdFk: Int,
                            servicioDesc: String
                          )
