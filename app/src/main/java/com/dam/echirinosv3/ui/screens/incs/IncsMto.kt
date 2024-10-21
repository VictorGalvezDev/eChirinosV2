package com.dam.echirinosv3.ui.screens.incs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.dam.echirinosv3.R
import com.dam.echirinosv3.data.model.EstadoInc
import com.dam.echirinosv3.data.model.TipoInc
import com.dam.echirinosv3.ui.screens.aulas.AulasVM

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IncsMto(
    incsVM: IncsVM,
    aulasVM: AulasVM,
    idDpto: Int,
    modifier: Modifier = Modifier,
    onShowSnackbar: (String) -> Unit = {},
    onNavUp: () -> Unit = {}
) {
    val uiIncBus = incsVM.uiIncBus
    val uiIncMto = incsVM.uiIncMto
    val uiInfoState = incsVM.uiInfoState
    val uiAulasState by aulasVM.uiAulasState.collectAsState()

    val contexto = LocalContext.current
    var expanded by rememberSaveable {
        mutableStateOf(false)
    }


    Column(modifier = modifier.verticalScroll(rememberScrollState())) {
        OutlinedTextField(
            value = uiIncMto.idDpto,
            onValueChange = { },
            enabled = false,
            label = { Text(text = stringResource(id = R.string.dptoid_mantenimiento)) },
            isError = !uiIncMto.datosObligatorios,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            singleLine = true,
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 2.dp)
        )

        OutlinedTextField(
            value = uiIncMto.fecha,
            onValueChange = { incsVM.setFecha(it) },
            enabled = false,
            label = { Text(text = stringResource(id = R.string.fecha_mantenimiento)) },
            isError = !uiIncMto.datosObligatorios,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text
            ),
            singleLine = true,
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 2.dp)
        )

        OutlinedTextField(
            value = uiIncMto.id,
            onValueChange = { incsVM.setId(it) },
            enabled = false,
            label = { Text(text = stringResource(id = R.string.id_inc_mantenimiento)) },
            isError = !uiIncMto.datosObligatorios,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text
            ),
            singleLine = true,
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 2.dp)
        )

        OutlinedTextField(
            value = uiIncMto.descripcion,
            onValueChange = { incsVM.setDescripcion(it) },
            enabled = idDpto != 0,
            label = { Text(text = stringResource(id = R.string.descripcion_mantenimiento)) },
            isError = !uiIncMto.datosObligatorios,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text
            ),
            singleLine = true,
            modifier = modifier
                .fillMaxWidth()
                .height(100.dp)
                .padding(horizontal = 10.dp, vertical = 2.dp)
        )

        Row(
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .selectableGroup(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = TipoInc.RMI.toString())
            RadioButton(
                selected = (uiIncMto.tipo == TipoInc.RMI),
                onClick = { incsVM.setTipo(TipoInc.RMI) },
                enabled = idDpto != 0
            )
            Text(text = TipoInc.RMA.toString())
            RadioButton(
                selected = (uiIncMto.tipo == TipoInc.RMA),
                onClick = { incsVM.setTipo(TipoInc.RMA) },
                enabled = idDpto != 0
            )
        }

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                if (idDpto != 0) expanded = !expanded
            },
            modifier = Modifier.padding(horizontal = 10.dp)
        ) {
            TextField(
                value = aulasVM.getNombre(uiIncMto.idAula),
                onValueChange = {},
                enabled = idDpto != 0,
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                if (idDpto != 0) {
                    DropdownMenuItem(text = { }, onClick = {
                        incsVM.setAula("")
                        expanded = false
                    }
                    )
                    uiAulasState.aulas.filter { it.idDpto == idDpto }.forEach {
                        DropdownMenuItem(
                            text = { Text(text = it.nombre) },
                            onClick = {
                                incsVM.setAula(it.id.toString())
                                expanded = false
                            }
                        )
                    }
                }
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 10.dp)
        ) {
            Text(text = stringResource(id = R.string.checkbox_inc_estado))
            Checkbox(
                checked = uiIncMto.estado,
                onCheckedChange = { incsVM.setEstado(!uiIncMto.estado) },
                enabled = idDpto == 0,
            )
            Text(text = if (uiIncMto.estado) EstadoInc.RESUELTA.name else EstadoInc.NORESUELTA.name)
        }
        OutlinedTextField(
            value = uiIncMto.resolucion,
            onValueChange = { incsVM.setResolucion(it) },
            enabled = idDpto == 0,
            label = { Text(text = stringResource(id = R.string.resolucion_mantenimiento)) },
            isError = !uiIncMto.datosObligatorios,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text
            ),
            singleLine = true,
            modifier = modifier
                .fillMaxWidth()
                .height(100.dp)
                .padding(horizontal = 10.dp, vertical = 2.dp)
        )

        Button(
            onClick = {
                if (uiIncBus.incSelected != -1) incsVM.editar() else incsVM.alta()
            },
            modifier = Modifier.padding(10.dp)
        ) {
            Icon(imageVector = Icons.Filled.Add, contentDescription = null)
        }
    }

    if (uiInfoState == IncsInfoState.Success) {
        if (uiIncBus.incSelected != -1) onShowSnackbar(contexto.getString(R.string.edicion_ok)) else onShowSnackbar(
            contexto.getString(R.string.alta_ok)
        )
        onNavUp()
        incsVM.resetInfoState()
        incsVM.resetDatos()

    } else if (uiInfoState == IncsInfoState.Error) {
        if (uiIncBus.incSelected != -1) onShowSnackbar(contexto.getString(R.string.edicion_ko)) else onShowSnackbar(
            contexto.getString(R.string.alta_ko)
        )
        incsVM.resetInfoState()
    }
}