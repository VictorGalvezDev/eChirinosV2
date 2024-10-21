package com.dam.echirinosv3.ui.components

import androidx.annotation.StringRes
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.dam.echirinosv3.R

@Composable
fun DlgConfirmacion(
    @StringRes mensaje: Int,
    @StringRes botonAceptarText: Int,
    @StringRes botonCancelarText: Int,
    onCancelarClick: () -> Unit,
    onAceptarClick: () -> Unit,
    onDimmissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = onDimmissRequest,
        title = { Text(stringResource(R.string.txt_appbar)) },
        text = { Text(stringResource(mensaje, 0)) },
        modifier = modifier,
        dismissButton = {
            TextButton(
                onClick = onCancelarClick
            ) {
                Text(text = stringResource(botonCancelarText))
            }
        },
        confirmButton = {
            TextButton(
                onClick = onAceptarClick
            ) {
                Text(text = stringResource(botonAceptarText))
            }
        })
}