package com.dam.echirinosv3.data.model

data class Incidencia(
    var idDpto: Int = 0, // PK
    var fecha: String = "", // PK yyyyMMdd
    var id: String = "",
    var descripcion: String = "",
    var tipo: TipoInc = TipoInc.RMI,
    var idAula: Int? = null,
    var estado: Boolean = false,
    var resolucion: String = ""
)

enum class TipoInc { RMI, RMA }
enum class EstadoInc { RESUELTA, NORESUELTA }


