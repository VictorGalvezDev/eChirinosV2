package com.dam.echirinosv3.ui.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.dam.echirinosv3.R

@Composable
fun PrefScreen(
    mainVM: MainVM,
    onNavUp: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .padding(5.dp)
            .fillMaxSize()
    ) {
        val uiMainPref = mainVM.uiPrefState

        OutlinedTextField(
            value = uiMainPref.defaultTimeSplash,
            onValueChange = { mainVM.setDefaultTtTimeSplash(it) },
            label = { Text(text = stringResource(id = R.string.default_time_splash_ui_pref)) },
            placeholder = { Text(text = stringResource(id = R.string.placeholder_splash_time)) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            singleLine = true,
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 2.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(text = stringResource(id = R.string.login_on_start_ui_pref))
            Spacer(modifier = Modifier.padding(8.dp))
            Switch(
                checked = uiMainPref.loginOnStart,
                onCheckedChange = { mainVM.setLoginOnStart(!uiMainPref.loginOnStart) })
        }

        Spacer(modifier = Modifier.padding(20.dp))
        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
            Button(onClick = {
                mainVM.savePreferences()
                onNavUp()
            }) {
                Text(text = stringResource(id = R.string.save_pref_button))
            }
        }
    }
}