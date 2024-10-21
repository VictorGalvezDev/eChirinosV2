package com.dam.echirinosv3.ui.screens.aulas

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
import com.dam.echirinosv3.data.model.Aula
import com.dam.echirinosv3.ui.components.DlgConfirmacion
import com.dam.echirinosv3.ui.main.MainVM

@Composable
fun AulasBus(
    aulasVM: AulasVM,
    mainVM: MainVM,
    modifier: Modifier = Modifier,
    onShowSnackbar: (String) -> Unit = {},
    onNavDown: () -> Unit = {}
) {

    val uiAulasBus = aulasVM.uiAulasBus
    val uiMainVM = mainVM.uiMainState
    val uiInfoState = aulasVM.uiInfoState

    val uiAulasState by aulasVM.uiAulasState.collectAsState()

    val contexto = LocalContext.current

    Box(
        modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            itemsIndexed(uiAulasState.aulas) { index, item ->
                DptoCard(
                    aula = item,
                    itemSelected = uiAulasBus.aulaSelected == index,
                    aulasVM = aulasVM,
                    onItemSelectedChange = {
                        aulasVM.setAulaSelected(if (uiAulasBus.aulaSelected != index) index else -1)
                    })
            }
        }
        FloatingActionButton(
            onClick = {
                if (uiAulasBus.aulaSelected == -1) {
                    aulasVM.resetDatos()
                    aulasVM.setDptoId(uiMainVM.login!!.id.toString())
                }
                onNavDown()
            },
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.BottomEnd)
        ) {
            if (uiAulasBus.aulaSelected != -1) Icon(
                imageVector = Icons.Filled.Star,
                null
            ) else Icon(imageVector = Icons.Filled.Add, null)
        }
    }
    if (uiAulasBus.showDlgBorrar) {
        DlgConfirmacion(
            mensaje = R.string.txt_borrar,
            botonCancelarText = R.string.but_cancelar,
            botonAceptarText = R.string.but_actepar_borrado,
            onCancelarClick = { aulasVM.setShowDlgBorrar(false) },
            onAceptarClick = {
                aulasVM.baja()
                aulasVM.resetDatos()
                aulasVM.setShowDlgBorrar(false)
            },
            onDimmissRequest = { aulasVM.setShowDlgBorrar(false) })
    }
    if (uiInfoState == AulasInfoState.Error) {
        onShowSnackbar(contexto.getString(R.string.borrar_ko))
        aulasVM.resetInfoState()
    } else if (uiInfoState == AulasInfoState.Success) {
        onShowSnackbar(contexto.getString(R.string.borrar_ok))
        aulasVM.resetInfoState()
    }
}


@Composable
fun DptoCard(
    aula: Aula,
    itemSelected: Boolean,
    aulasVM: AulasVM,
    onItemSelectedChange: () -> Unit,
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
                    painter = painterResource(R.drawable.aula),
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
                    text = stringResource(id = R.string.show_dptoid_aula_card, aula.idDpto),
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = stringResource(id = R.string.show_id_aula_card, aula.id),
                    fontWeight = FontWeight.Bold
                )
                Text(text = aula.nombre, fontSize = 20.sp, fontStyle = FontStyle.Italic)
            }
            if (itemSelected) {
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = { aulasVM.setShowDlgBorrar(true) }
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