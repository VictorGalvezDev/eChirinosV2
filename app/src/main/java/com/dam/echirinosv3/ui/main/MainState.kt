package com.dam.echirinosv3.ui.main

import com.dam.echirinosv3.data.model.Departamento

data class MainState(
    val showDlgSalir: Boolean = false,
    val showMenu: Boolean = false,
    val login: Departamento? = null,
)

data class LoginState(
    val idDpto: String = "",
    val clave: String = "",
    val datosObligatorios: Boolean = false
)

data class PrefsState(
    val loginOnStart: Boolean = true,
    val defaultTimeSplash: String = ""
)
