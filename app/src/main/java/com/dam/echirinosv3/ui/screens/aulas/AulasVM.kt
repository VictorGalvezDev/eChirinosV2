package com.dam.echirinosv3.ui.screens.aulas

import android.database.sqlite.SQLiteException
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.dam.echirinosv3.MainApplication
import com.dam.echirinosv3.data.repository.AulasRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AulasVM(private val aulasRepository: AulasRepository) : ViewModel() {

    lateinit var uiAulasState: StateFlow<AulasState>

    var uiAulasBus by mutableStateOf(AulasBusState())
        private set
    var uiAulasMto by mutableStateOf(AulasMtoState())
        private set
    var uiInfoState: AulasInfoState by mutableStateOf(AulasInfoState.Loading)
        private set
    var uiFiltroState by mutableStateOf(AulasFiltroState())
        private set


    /* Funciones Estado del Buscador
       **************************************************************/
    fun setAulaSelected(pos: Int) {
        uiAulasBus = uiAulasBus.copy(
            aulaSelected = pos,
            showDlgBorrar = false,
        )
        uiAulasMto = if (pos != -1) {
            val aula = uiAulasState.value.aulas[pos]
            uiAulasMto.copy(
                dptoId = aula.idDpto.toString(),
                id = aula.id.toString(),
                nombre = aula.nombre,
                datosObligatorios = true
            )
        } else {
            uiAulasMto.copy(
                dptoId = "",
                id = "",
                nombre = "",
                datosObligatorios = false
            )
        }
    }

    fun setShowDlgBorrar(show: Boolean) {
        uiAulasBus = uiAulasBus.copy(
            showDlgBorrar = show
        )
    }

    fun setFiltro(filtro: String) {
        uiAulasBus = uiAulasBus.copy(aulasFiltro = filtro)
    }


    /* Funciones Estado del Mantenimiento
*********************************************************/
    fun setDptoId(dptoId: String) {
        uiAulasMto = uiAulasMto.copy(
            dptoId = dptoId,
            datosObligatorios = (dptoId != "" && uiAulasMto.id != "" && uiAulasMto.nombre != "")
        )
    }

    fun setId(id: String) {
        uiAulasMto = uiAulasMto.copy(
            id = id,
            datosObligatorios = (uiAulasMto.dptoId != "" && id != "" && uiAulasMto.nombre != "")
        )
    }

    fun setNombre(nombre: String) {
        uiAulasMto = uiAulasMto.copy(
            nombre = nombre,
            datosObligatorios = (uiAulasMto.dptoId != "" && uiAulasMto.id != "" && nombre != "")
        )
    }


    /* Funciones Filtrado Aulas
       *********************************************************************/

    fun filtrar() {
        uiAulasState =
            aulasRepository.getAllAulasByFiltro(uiAulasBus.aulasFiltro)
                .map { AulasState(it) }
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                    initialValue = AulasState()
                )
    }


    fun setUiFiltro(filtro: String) {
        uiFiltroState = uiFiltroState.copy(idDpto = filtro)
    }

    fun resetFiltro() {
        uiFiltroState = uiFiltroState.copy(idDpto = uiAulasBus.aulasFiltro)
    }

    /* Funciones Lógica Aulas
   *********************************************************************/
    fun resetDatos() {
        uiAulasBus = uiAulasBus.copy(
            aulaSelected = -1,
            showDlgBorrar = false
        )
        uiAulasMto = uiAulasMto.copy(
            dptoId = "",
            id = "",
            nombre = "",
            datosObligatorios = false
        )
    }

    fun resetInfoState() {
        uiInfoState = AulasInfoState.Loading
    }

    fun getNombre(idAula: String): String {

        val aula = uiAulasState.value.aulas.firstOrNull() { it.id.toString() == idAula }
        return aula?.nombre ?: ""
    }

    fun alta() {
        if (!uiAulasMto.datosObligatorios) {
            uiInfoState = AulasInfoState.Error
            return
        }
        viewModelScope.launch {
            uiInfoState = try {
                aulasRepository.insertAula(uiAulasMto.toAula())
                AulasInfoState.Success
            } catch (e: SQLiteException) {
                AulasInfoState.Error
            }
        }
    }

    fun editar() {
        if (!uiAulasMto.datosObligatorios) {
            uiInfoState = AulasInfoState.Error
            return
        }
        viewModelScope.launch {
            uiInfoState = try {
                aulasRepository.updateAula(uiAulasMto.toAula())
                AulasInfoState.Success
            } catch (e: SQLiteException) {
                AulasInfoState.Error
            }
        }
    }

    fun baja() {
        if (!uiAulasMto.datosObligatorios) {
            uiInfoState = AulasInfoState.Error
            return
        }
        viewModelScope.launch {
            uiInfoState = try {
                aulasRepository.deleteAula(uiAulasMto.toAula())
                AulasInfoState.Success
            } catch (e: SQLiteException) {
                AulasInfoState.Error
            }
        }
    }


    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as MainApplication)
                val aulasRepository = application.container.aulasRepository
                AulasVM(aulasRepository = aulasRepository)
            }
        }
    }
}