package com.dam.echirinosv3.ui.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.dam.echirinosv3.R

@Composable
fun HomeScreen(mainVM: MainVM, modifier: Modifier = Modifier) {

    Surface(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = if (mainVM.uiMainState.login != null) {
                    stringResource(id = R.string.logged_as, mainVM.uiMainState.login!!.nombre)
                } else {
                    stringResource(id = R.string.no_logged)
                }
            )
        }
    }
}