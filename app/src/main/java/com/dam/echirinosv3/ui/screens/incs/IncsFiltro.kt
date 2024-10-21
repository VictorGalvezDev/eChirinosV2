package com.dam.echirinosv3.ui.screens.incs

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.dam.echirinosv3.R
import com.dam.echirinosv3.ui.components.DlgSeleccionFecha
import com.dam.echirinosv3.ui.main.MainVM
import com.dam.echirinosv3.ui.screens.dptos.DptosVM
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IncsFiltro(
    mainVM: MainVM,
    dptosVM: DptosVM,
    incsVM: IncsVM,
    onNavUp: () -> Unit,
    modifier: Modifier = Modifier
) {

    val uiDptosState by dptosVM.uiDptosState.collectAsState()
    val uiFiltroState = incsVM.uiFiltroState
    val uiMainState = mainVM.uiMainState
    var expanded by rememberSaveable {
        mutableStateOf(false)
    }

    val datePickerState = rememberDatePickerState()
    var showDatePicker by rememberSaveable {
        mutableStateOf(false)
    }

    Column(
        modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {

            TextField(
                value = dptosVM.getNombre(uiFiltroState.idDpto),
                onValueChange = {},
                enabled = uiMainState.login!!.id == 0,
                readOnly = true,
                trailingIcon = {
                    if (uiMainState.login.id == 0) ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = expanded
                    )
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            if (uiMainState.login.id == 0) {
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(text = {}, onClick = {
                        incsVM.setUIidDptoFiltro("")
                        expanded = false
                    })
                    uiDptosState.departamentos.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(text = item.nombre) },
                            onClick = {
                                incsVM.setUIidDptoFiltro(item.id.toString())
                                expanded = false
                            }
                        )
                    }
                }
            }
        }
        Spacer(modifier.padding(10.dp))
        TextField(
            value = LocalDate.parse(uiFiltroState.fecha)
                .format(DateTimeFormatter.ofPattern("dd/MM/uuuu")),
            onValueChange = {},
            label = { Text(text = stringResource(id = R.string.fecha_mantenimiento)) },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { showDatePicker = true }) {
                    Icon(
                        imageVector = Icons.Filled.CalendarMonth,
                        contentDescription = null
                    )
                }
            }
        )
        Spacer(modifier.padding(10.dp))
        Box(modifier.border(BorderStroke(1.dp, Color.Black))) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .selectableGroup(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(text = stringResource(id = R.string.estado_filtro_noresuelto))
                RadioButton(
                    selected = (uiFiltroState.estado == "0"),
                    onClick = { incsVM.setUIEstado("0") },
                )
                Spacer(modifier.weight(1f))
                Text(text = stringResource(id = R.string.estado_filtro_resuelto))
                RadioButton(
                    selected = (uiFiltroState.estado == "1"),
                    onClick = { incsVM.setUIEstado("1") },
                )
                Spacer(modifier.weight(1f))
                Text(text = stringResource(id = R.string.estado_filtro_todos))
                RadioButton(
                    selected = (uiFiltroState.estado == "-1"),
                    onClick = { incsVM.setUIEstado("-1") },
                )
            }

        }

        Spacer(modifier = Modifier.padding(20.dp))

        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
            Button(onClick = {
                incsVM.setIdDptoFiltro(uiFiltroState.idDpto)
                incsVM.setFechaFiltro(uiFiltroState.fecha)
                incsVM.setEstadoFiltro(uiFiltroState.estado)
                incsVM.filtrar()
                onNavUp()
            }) {
                Text(text = stringResource(id = R.string.filtrar))
            }
        }


        DlgSeleccionFecha(
            botonAceptarText = R.string.but_aceptar,
            botonCancelarText = R.string.but_cancelar,
            datePickerState = datePickerState,
            showDatePicker = showDatePicker,
            onClickOk = {
                incsVM.setUIFechaFiltro(it)
                showDatePicker = false
            },
            onDimissRequestAndCancel = { showDatePicker = false }
        )
    }

}