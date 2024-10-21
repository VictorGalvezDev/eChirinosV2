package com.dam.echirinosv3.ui.screens.incs

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
import com.dam.echirinosv3.data.model.TipoInc
import com.dam.echirinosv3.data.repository.IncsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class IncsVM(private val incsRepository: IncsRepository) : ViewModel() {

    lateinit var uiIncsState: StateFlow<IncsState>
    var uiIncBus by mutableStateOf(IncBusState())
        private set
    var uiIncMto by mutableStateOf(IncMtoState())
        private set
    var uiInfoState: IncsInfoState by mutableStateOf(IncsInfoState.Loading)
        private set
    var uiFiltroState by mutableStateOf(IncsFiltroState())
        private set


    /* Funciones Estado del Buscador
       **************************************************************/
    fun setIncSelected(pos: Int) {
        uiIncBus = uiIncBus.copy(
            incSelected = pos,
            showDlgBorrar = false
        )
        uiIncMto = if (pos != -1) {
            val inc = uiIncsState.value.inc[pos]
            uiIncMto.copy(
                idDpto = inc.idDpto.toString(),
                fecha = inc.fecha,
                id = inc.id,
                descripcion = inc.descripcion,
                tipo = inc.tipo,
                idAula = if (inc.idAula != null) inc.idAula.toString() else "",
                estado = inc.estado,
                resolucion = inc.resolucion,
                datosObligatorios = true
            )
        } else {
            uiIncMto.copy(
                idDpto = "",
                fecha = "",
                id = "",
                descripcion = "",
                tipo = TipoInc.RMI,
                idAula = "",
                estado = false,
                resolucion = "",
                datosObligatorios = false
            )
        }
    }

    fun setShowDlgBorrar(show: Boolean) {
        uiIncBus = uiIncBus.copy(
            showDlgBorrar = show
        )
    }

    fun setFechaFiltro(filtro: String) {
        uiIncBus = uiIncBus.copy(fechaFiltro = filtro)
    }

    fun setIdDptoFiltro(filtro: String) {
        uiIncBus = uiIncBus.copy(idDptoFiltro = filtro)
    }

    fun setEstadoFiltro(filtro: String) {
        uiIncBus = uiIncBus.copy(estadoFiltro = filtro)
    }

    /* Funciones Estado del Mantenimiento
*********************************************************/
    fun setDptoId(dptoId: String) {
        uiIncMto = uiIncMto.copy(
            idDpto = dptoId,
            datosObligatorios = (dptoId != "" && uiIncMto.fecha != "" && uiIncMto.id != "" && uiIncMto.descripcion != "")
        )
    }

    fun setFecha(fecha: String) {
        uiIncMto = uiIncMto.copy(
            fecha = fecha,
            datosObligatorios = (uiIncMto.idDpto != "" && fecha != "" && uiIncMto.id != "" && uiIncMto.descripcion != "")
        )
    }

    fun setId(id: String) {
        uiIncMto = uiIncMto.copy(
            id = id,
            datosObligatorios = (uiIncMto.idDpto != "" && uiIncMto.fecha != "" && id != "" && uiIncMto.descripcion != "")
        )
    }

    fun setDescripcion(descripcion: String) {
        uiIncMto = uiIncMto.copy(
            descripcion = descripcion,
            datosObligatorios = (uiIncMto.idDpto != "" && uiIncMto.fecha != "" && uiIncMto.id != "" && descripcion != "")
        )
    }

    fun setTipo(tipo: TipoInc) {
        uiIncMto = uiIncMto.copy(
            tipo = tipo,
        )
    }

    fun setAula(aula: String) {
        uiIncMto = uiIncMto.copy(
            idAula = aula,
        )
    }

    fun setEstado(estado: Boolean) {
        uiIncMto = uiIncMto.copy(
            estado = estado,
        )
    }

    fun setResolucion(resolucion: String) {
        uiIncMto = uiIncMto.copy(
            resolucion = resolucion,
        )
    }

    /* Funciones Lógica Incidencias
   *********************************************************************/
    fun filtrar() {
        uiIncsState =
            incsRepository.getAllIncsByFiltro(
                uiIncBus.idDptoFiltro,
                uiIncBus.fechaFiltro,
                uiIncBus.estadoFiltro.toInt()
            )
                .map { IncsState(it) }
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                    initialValue = IncsState()
                )
    }


    fun setUIFechaFiltro(filtro: String) {
        uiFiltroState = uiFiltroState.copy(fecha = filtro)
    }

    fun setUIidDptoFiltro(filtro: String) {
        uiFiltroState = uiFiltroState.copy(idDpto = filtro)
    }

    fun setUIEstado(filtro: String) {
        uiFiltroState = uiFiltroState.copy(estado = filtro)
    }

    fun resetFiltro() {
        uiFiltroState =
            uiFiltroState.copy(
                fecha = uiIncBus.fechaFiltro,
                idDpto = uiIncBus.idDptoFiltro,
                estado = uiIncBus.estadoFiltro
            )
    }

    fun resetInfoState() {
        uiInfoState = IncsInfoState.Loading
    }

    fun resetDatos() {
        uiIncBus = uiIncBus.copy(
            incSelected = -1,
            showDlgBorrar = false
        )
        uiIncMto = uiIncMto.copy(
            idDpto = "",
            fecha = "",
            id = "",
            descripcion = "",
            tipo = TipoInc.RMI,
            idAula = "",
            estado = false,
            resolucion = "",
            datosObligatorios = false
        )
    }

    fun alta() {
        if (!uiIncMto.datosObligatorios) {
            uiInfoState = IncsInfoState.Error
            return
        }
        viewModelScope.launch {
            uiInfoState = try {
                incsRepository.insertInc(uiIncMto.toInc())
                IncsInfoState.Success
            } catch (e: SQLiteException) {
                IncsInfoState.Error
            }
        }
    }


    fun editar() {
        if (!uiIncMto.datosObligatorios) {
            uiInfoState = IncsInfoState.Error
            return
        }
        viewModelScope.launch {
            uiInfoState = try {
                incsRepository.updateInc(uiIncMto.toInc())
                IncsInfoState.Success
            } catch (e: SQLiteException) {
                IncsInfoState.Error
            }
        }
    }

    fun baja() {
        if (!uiIncMto.datosObligatorios) {
            uiInfoState = IncsInfoState.Error
            return
        }
        viewModelScope.launch {
            uiInfoState = try {
                incsRepository.deleteInc(uiIncMto.toInc())
                IncsInfoState.Success
            } catch (e: SQLiteException) {
                IncsInfoState.Error
            }
        }
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[APPLICATION_KEY] as MainApplication)
                val incsRepository = application.container.incsRepository
                IncsVM(incsRepository = incsRepository)
            }
        }
    }
}