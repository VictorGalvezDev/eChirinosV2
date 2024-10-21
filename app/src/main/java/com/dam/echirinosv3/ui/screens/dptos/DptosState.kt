package com.dam.echirinosv3.ui.screens.dptos

import com.dam.echirinosv3.data.model.Departamento


data class DptosState(
    var departamentos: List<Departamento> = listOf(),
)

data class DptosBusState(
    val showDlgBorrar: Boolean = false,
    val dptoSelected: Int = -1
)

data class DptosMtoState(
    val id: String = "",
    val nombre: String = "",
    val clave: String = "",
    val datosObligatorios: Boolean = false
)

sealed interface DptosInfoState {
    data object Loading : DptosInfoState
    data object Success : DptosInfoState
    data object Error : DptosInfoState
}


fun DptosMtoState.toDpto(): Departamento = Departamento(
    id = id.toInt(),
    nombre = nombre,
    clave = clave
)

//fun Departamento.toDptosMtoState(): DptosMtoState = DptosMtoState(
//    id = id.toString(),
//    nombre = nombre,
//    clave = clave,
//    datosObligatorios = false
//)


