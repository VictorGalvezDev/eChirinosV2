package com.dam.echirinosv3.ui.screens.aulas

import com.dam.echirinosv3.data.model.Aula

data class AulasState(
    var aulas: List<Aula> = listOf(),
)

data class AulasBusState(
    val showDlgBorrar: Boolean = false,
    val aulaSelected: Int = -1,
    val aulasFiltro: String = ""
)

data class AulasMtoState(
    val dptoId: String = "",
    val id: String = "",
    val nombre: String = "",
    val datosObligatorios: Boolean = false
)

data class AulasFiltroState(
    val idDpto: String = ""
)

sealed interface AulasInfoState {
    data object Loading : AulasInfoState
    data object Success : AulasInfoState
    data object Error : AulasInfoState
}

fun AulasMtoState.toAula(): Aula = Aula(
    idDpto = dptoId.toInt(),
    id = id.toInt(),
    nombre = nombre,
)

//fun Aula.toAulaMtoState(): AulasMtoState = AulasMtoState(
//    dptoId = idDpto.toString(),
//    id = id.toString(),
//    nombre = nombre,
//    datosObligatorios = false
//)