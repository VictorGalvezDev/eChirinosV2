package com.dam.echirinosv3.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun CustomOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    @StringRes idLabel: Int,
    isError: Boolean,
    keyboardType: KeyboardType,
    singleLine: Boolean,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        label = {
            Text(
                text = stringResource(idLabel),
                fontWeight = FontWeight.Bold
            )
        },

        isError = isError,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType
        ),
        singleLine = singleLine
    )

}