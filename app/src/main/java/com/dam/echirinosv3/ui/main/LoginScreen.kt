package com.dam.echirinosv3.ui.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.dam.echirinosv3.R
import com.dam.echirinosv3.ui.screens.dptos.DptosVM


@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun LoginScreen(
    mainVM: MainVM,
    dptosVM: DptosVM,
    modifier: Modifier = Modifier,
    onShowSnackbar: (String) -> Unit = {},
    navHome: () -> Unit = {}
) {

    var showPassword by rememberSaveable { mutableStateOf(false) }
    var expanded by rememberSaveable {
        mutableStateOf(false)
    }
    val contexto = LocalContext.current
    val controller = LocalSoftwareKeyboardController.current

    val uiLoginState = mainVM.uiLoginState
    val uiDptosState by dptosVM.uiDptosState.collectAsState()

    Surface(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Column(Modifier.padding(5.dp)) {
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = {
                    expanded = !expanded
                }
            ) {
                TextField(
                    value = dptosVM.getNombre(uiLoginState.idDpto),
                    onValueChange = {}, //{mainVM.setClave(it)},
                    readOnly = true,
                    isError = !uiLoginState.datosObligatorios,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    uiDptosState.departamentos.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(text = item.nombre) },
                            onClick = {
                                mainVM.setIdDpto(item.id.toString())
                                expanded = false
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = uiLoginState.clave,
                onValueChange = { mainVM.setClave(it) },
                label = { Text(text = stringResource(id = R.string.login_clave)) },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                isError = !uiLoginState.datosObligatorios,
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    if (showPassword) {
                        IconButton(onClick = { showPassword = false }) {
                            Icon(
                                imageVector = Icons.Filled.Visibility,
                                contentDescription = null
                            )
                        }
                    } else {
                        IconButton(
                            onClick = { showPassword = true }) {
                            Icon(
                                imageVector = Icons.Filled.VisibilityOff,
                                contentDescription = null
                            )
                        }
                    }
                }
            )
            Spacer(modifier = Modifier.padding(20.dp))
            Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                Button(onClick = {
                    val ok =
                        mainVM.setLogin(uiDptosState.departamentos.firstOrNull() {
                            it.id.toString() == uiLoginState.idDpto
                        })
                    if (ok) {
                        navHome()
                        controller?.hide()
                        onShowSnackbar(contexto.getString(R.string.login_ok))
                    } else {
                        onShowSnackbar(contexto.getString(R.string.login_ko))
                    }
                }) {
                    Text(text = stringResource(id = R.string.log_in_text))
                }
            }
        }
    }
}