package com.dam.echirinosv3.ui.screens.aulas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.dam.echirinosv3.R
import com.dam.echirinosv3.ui.main.MainVM
import com.dam.echirinosv3.ui.screens.dptos.DptosVM

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AulasFiltro(
    mainVM: MainVM,
    dptosVM: DptosVM,
    aulasVM: AulasVM,
    onNavUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiDptosState by dptosVM.uiDptosState.collectAsState()
    val uiFiltroState = aulasVM.uiFiltroState
    val uiMainState = mainVM.uiMainState
    var expanded by rememberSaveable {
        mutableStateOf(false)
    }



    Column(
        modifier
            .fillMaxSize()
            .padding(8.dp)
    )
    {
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
                        aulasVM.setUiFiltro("")
                        expanded = false
                    })
                    uiDptosState.departamentos.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(text = item.nombre) },
                            onClick = {
                                aulasVM.setUiFiltro(item.id.toString())
                                expanded = false
                            }
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.padding(20.dp))
        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
            if (uiMainState.login!!.id == 0) {
                Button(onClick = {
                    aulasVM.setFiltro(uiFiltroState.idDpto)
                    aulasVM.filtrar()
                    onNavUp()
                }) {
                    Text(text = stringResource(id = R.string.filtrar))
                }
            }
        }
    }
}