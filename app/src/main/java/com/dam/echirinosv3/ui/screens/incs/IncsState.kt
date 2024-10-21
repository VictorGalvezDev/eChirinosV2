package com.dam.echirinosv3.ui.screens.incs

import com.dam.echirinosv3.data.model.Incidencia
import com.dam.echirinosv3.data.model.TipoInc

data class IncsState(
    var inc: List<Incidencia> = listOf()
)

data class IncBusState(
    val showDlgBorrar: Boolean = false,
    val incSelected: Int = -1,
    val fechaFiltro: String = "",
    val idDptoFiltro: String = "",
    val estadoFiltro: String = ""
)

data class IncMtoState(
    var idDpto: String = "", // PK
    var fecha: String = "", // PK yyyyMMdd
    var id: String = "", // PK HHmmss
    var descripcion: String = "",
    var tipo: TipoInc = TipoInc.RMI,
    var idAula: String = "",
    var estado: Boolean = false,
    var resolucion: String = "",
    val datosObligatorios: Boolean = false
)

data class IncsFiltroState(
    val idDpto: String = "",
    val fecha: String = "",
    val estado: String = ""
)

sealed interface IncsInfoState {
    data object Loading : IncsInfoState
    data object Success : IncsInfoState
    data object Error : IncsInfoState
}

fun IncMtoState.toInc(): Incidencia = Incidencia(
    idDpto = idDpto.toInt(),
    fecha = fecha,
    id = id,
    descripcion = descripcion,
    tipo = tipo,
    idAula = if (idAula.isNotEmpty()) idAula.toInt() else null,
    estado = estado,
    resolucion = resolucion
)

//fun Incidencia.toIncMtoState(): IncMtoState = IncMtoState(
//    idDpto = idDpto.toString(),
//    fecha = fecha,
//    id = id,
//    descripcion = descripcion,
//    tipo = tipo,
//    idAula = idAula.toString(),
//    estado = estado,
//    resolucion = resolucion,
//    datosObligatorios = false
//)
