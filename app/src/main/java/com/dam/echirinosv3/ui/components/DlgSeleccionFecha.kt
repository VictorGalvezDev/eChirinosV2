package com.dam.echirinosv3.ui.components

import androidx.annotation.StringRes
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DlgSeleccionFecha(
    @StringRes botonAceptarText: Int,
    @StringRes botonCancelarText: Int,
    datePickerState: DatePickerState,
    showDatePicker: Boolean,
    onClickOk: (String) -> Unit,
    onDimissRequestAndCancel: () -> Unit
) {
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = onDimissRequestAndCancel,
            confirmButton = {
                TextButton(
                    onClick = {
                        onClickOk(
                            if (datePickerState.selectedDateMillis == null) LocalDate.now()
                                .toString() else
                                Instant.ofEpochMilli(
                                    datePickerState.selectedDateMillis!!
                                ).atZone(ZoneId.of("UTC")).toLocalDate().toString()
                        )
                    }
                ) {
                    Text(text = stringResource(botonAceptarText))
                }
            },
            dismissButton = {
                TextButton(onClick = onDimissRequestAndCancel) {
                    Text(text = stringResource(botonCancelarText))
                }
            }
        )
        {
            DatePicker(state = datePickerState)
        }
    }
}