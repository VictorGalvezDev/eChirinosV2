package com.dam.echirinosv3.ui.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.dam.echirinosv3.MainApplication
import com.dam.echirinosv3.data.model.Departamento
import com.dam.echirinosv3.data.model.Preferencias
import com.dam.echirinosv3.data.repository.MainRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch

class MainVM(private val mainRepository: MainRepository) : ViewModel() {
    var uiMainState by mutableStateOf(MainState())
        private set
    var uiLoginState by mutableStateOf(LoginState())
        private set
    var uiPrefState by mutableStateOf(PrefsState())
        private set
    var prefState by mutableStateOf(Preferencias())
        private set

    /* Funciones Lógica Main
   **********************************************************************/
    fun setShowDlgSalir(show: Boolean) {
        uiMainState = uiMainState.copy(showDlgSalir = show)
    }

    fun setShowMenu(show: Boolean) {
        uiMainState = uiMainState.copy(showMenu = show)
    }

    /* Funciones Lógica UI
**********************************************************************/

    fun setIdDpto(idDpto: String) {
        uiLoginState = uiLoginState.copy(
            idDpto = idDpto,
            datosObligatorios = (idDpto != "" && uiLoginState.clave != "")
        )
    }

    fun setClave(clave: String) {
        uiLoginState = uiLoginState.copy(
            clave = clave,
            datosObligatorios = (uiLoginState.idDpto != "" && clave != "")
        )
    }

    fun setLogin(dpto: Departamento?): Boolean {
        if (!uiLoginState.datosObligatorios) return false
        if (dpto == null) {
            return false
        } else if (dpto.clave == uiLoginState.clave) {
            uiMainState = uiMainState.copy(
                login = dpto
            )
            return true
        }
        return false
    }

    fun resetDatos() {
        uiLoginState = uiLoginState.copy(
            idDpto = "",
            clave = "",
            datosObligatorios = false
        )
    }


    /* Funciones Preferencias
**********************************************************************/
    fun savePreferences() {
        viewModelScope.launch {
            setPrefState(uiPrefState.loginOnStart, uiPrefState.defaultTimeSplash)
            mainRepository.savePreferences(
                Preferencias(
                    showLoginOnStart = prefState.showLoginOnStart,
                    defaultTimeSplash = prefState.defaultTimeSplash
                )
            )
        }
    }

    suspend fun getPrefereces() {
        viewModelScope.async {
            mainRepository.getPreferences().take(1).collect {
                uiPrefState = uiPrefState.copy(
                    loginOnStart = it.showLoginOnStart,
                    defaultTimeSplash = it.defaultTimeSplash.toString()
                )
                prefState = prefState.copy(
                    showLoginOnStart = it.showLoginOnStart,
                    defaultTimeSplash = it.defaultTimeSplash
                )
            }
        }.await()
    }

    fun setLoginOnStart(loginOnStart: Boolean) {
        uiPrefState = uiPrefState.copy(loginOnStart = loginOnStart)
    }

    fun setDefaultTtTimeSplash(defaultTimeSplash: String) {
        uiPrefState = uiPrefState.copy(defaultTimeSplash = defaultTimeSplash)
    }

    fun setPrefState(login: Boolean, time: String) {
        prefState = prefState.copy(
            showLoginOnStart = login,
            defaultTimeSplash = if (time.isNotEmpty()) time.toInt() else 3
        )
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MainApplication)
                val mainRepository = application.container.mainRepository
                MainVM(mainRepository = mainRepository)
            }
        }
    }
}
