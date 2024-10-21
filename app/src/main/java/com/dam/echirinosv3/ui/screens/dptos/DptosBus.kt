package com.dam.echirinosv3.ui.screens.dptos

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.FloatingActionButtonElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dam.echirinosv3.R
import com.dam.echirinosv3.data.model.Departamento
import com.dam.echirinosv3.ui.components.DlgConfirmacion


@Composable
fun DptosBus(
    dptosVM: DptosVM,
    modifier: Modifier = Modifier,
    onShowSnackbar: (String) -> Unit = {},
    onNavDown: () -> Unit = {}
) {
    val uiBusState = dptosVM.uiBusState
    val uiDptosState by dptosVM.uiDptosState.collectAsState()
    val uiInfoState = dptosVM.uiInfoState
    val contexto = LocalContext.current

    Box(
        modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            itemsIndexed(uiDptosState.departamentos) { index, item ->
                DptoCard(
                    dpto = item,
                    itemSelected = uiBusState.dptoSelected == index,
                    dptosVM = dptosVM,
                    onItemSelectedChange = {
                        dptosVM.setDptoSelected(if (uiBusState.dptoSelected != index) index else -1)
                    })
            }
        }
        FloatingActionButton(
            onClick = {
                onNavDown()
                if (uiBusState.dptoSelected == -1) dptosVM.resetDatos()
            },
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.BottomEnd)
        ) {
            if (uiBusState.dptoSelected != -1) Icon(
                imageVector = Icons.Filled.Star,
                null
            ) else Icon(imageVector = Icons.Filled.Add, null)
        }
    }
    if (uiBusState.showDlgBorrar) {
        DlgConfirmacion(
            mensaje = R.string.txt_borrar,
            botonCancelarText = R.string.but_cancelar,
            botonAceptarText = R.string.but_actepar_borrado,
            onCancelarClick = { dptosVM.setShowDlgBorrar(false) },
            onAceptarClick = {
                dptosVM.baja()
                dptosVM.resetDatos()
                dptosVM.setShowDlgBorrar(false)
            },
            onDimmissRequest = { dptosVM.setShowDlgBorrar(false) })
    }

    if (uiInfoState == DptosInfoState.Error) {
        onShowSnackbar(contexto.getString(R.string.borrar_ko))
        dptosVM.resetInfoState()
    } else if (uiInfoState == DptosInfoState.Success) {
        onShowSnackbar(contexto.getString(R.string.borrar_ok))
        dptosVM.resetInfoState()
    }
}


@Composable
fun DptoCard(
    dpto: Departamento,
    itemSelected: Boolean,
    onItemSelectedChange: () -> Unit,
    dptosVM: DptosVM,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(4.dp)
            .clickable { onItemSelectedChange() },
        border = if (itemSelected) BorderStroke(1.dp, Color.Black) else null
    ) {
        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            Box {
                Image(
                    painter = painterResource(R.drawable.apartamento),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(8.dp)
                        .size(100.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(8.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.show_clave_card, dpto.id),
                    fontWeight = FontWeight.Bold
                )
                Text(text = dpto.nombre, fontSize = 20.sp, fontStyle = FontStyle.Italic)
            }
            if (itemSelected) {
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = { dptosVM.setShowDlgBorrar(true) }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = null,
                        tint = Color.Red,
                        modifier = Modifier.size(128.dp)
                    )
                }
            }
        }
    }
}