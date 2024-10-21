package com.dam.echirinosv3.ui.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.dam.echirinosv3.R
import kotlinx.coroutines.delay


@Composable
fun SplashScreen(
    mainVM: MainVM,
    navHome: () -> Unit,
    navLogin: () -> Unit,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(key1 = true) {
        mainVM.getPrefereces()
        delay((mainVM.prefState.defaultTimeSplash.toLong() * 1000))
        if (mainVM.prefState.showLoginOnStart) navLogin() else navHome()
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(R.drawable.chirinoslogo),
                contentDescription = null,
                modifier = Modifier
                    .padding(8.dp)
                    .size(200.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Text(text = stringResource(id = R.string.splash_titulo))
            Text(text = stringResource(id = R.string.splash_desc))
        }
    }
}