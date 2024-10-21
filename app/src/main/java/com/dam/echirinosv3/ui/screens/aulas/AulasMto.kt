package com.dam.echirinosv3.ui.screens.aulas

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.dam.echirinosv3.R
import com.dam.echirinosv3.ui.components.CustomOutlinedTextField

@Composable
fun AulasMto(
    aulasVM: AulasVM,
    modifier: Modifier = Modifier,
    onShowSnackbar: (String) -> Unit = {},
    onNavUp: () -> Unit = {}
) {
    val uiAulasBus = aulasVM.uiAulasBus
    val uiAulasMto = aulasVM.uiAulasMto
    val uiInfoState = aulasVM.uiInfoState

    val contexto = LocalContext.current


    Column(modifier = modifier) {
        OutlinedTextField(
            value = uiAulasMto.dptoId,
            onValueChange = { },
            enabled = false,
            label = { Text(text = stringResource(id = R.string.dptoid_mantenimiento)) },
            isError = !uiAulasMto.datosObligatorios,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            singleLine = true,
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 2.dp)
        )

        OutlinedTextField(
            value = uiAulasMto.id,
            onValueChange = { aulasVM.setId(it) },
            enabled = uiAulasBus.aulaSelected == -1,
            label = { Text(text = stringResource(id = R.string.id_mantenimiento)) },
            isError = !uiAulasMto.datosObligatorios,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            singleLine = true,
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 2.dp)
        )

        CustomOutlinedTextField(
            value = uiAulasMto.nombre,
            onValueChange = { aulasVM.setNombre(it) },
            idLabel = R.string.nombre_mantenimiento,
            isError = !uiAulasMto.datosObligatorios,
            keyboardType = KeyboardType.Text,
            singleLine = true,
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 2.dp)
        )

        Button(
            onClick = {
                if (uiAulasBus.aulaSelected != -1) aulasVM.editar() else aulasVM.alta()
            },
            modifier = Modifier.padding(5.dp)
        ) {
            Icon(imageVector = Icons.Filled.Add, contentDescription = null)
        }
    }

    if (uiInfoState == AulasInfoState.Success) {
        if (uiAulasBus.aulaSelected != -1) onShowSnackbar(contexto.getString(R.string.edicion_ok)) else onShowSnackbar(
            contexto.getString(R.string.alta_ok)
        )
        aulasVM.resetInfoState()
        onNavUp()
        aulasVM.resetDatos()

    } else if (uiInfoState == AulasInfoState.Error) {
        if (uiAulasBus.aulaSelected != -1) onShowSnackbar(contexto.getString(R.string.edicion_ko)) else onShowSnackbar(
            contexto.getString(R.string.alta_ko)
        )
        aulasVM.resetInfoState()
    }
}