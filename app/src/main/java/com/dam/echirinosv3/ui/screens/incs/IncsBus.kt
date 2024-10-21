package com.dam.echirinosv3.ui.screens.incs

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dam.echirinosv3.R
import com.dam.echirinosv3.data.model.EstadoInc
import com.dam.echirinosv3.data.model.Incidencia
import com.dam.echirinosv3.ui.components.DlgConfirmacion
import com.dam.echirinosv3.ui.main.MainVM
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun IncsBus(
    incsVM: IncsVM,
    mainVM: MainVM,
    modifier: Modifier = Modifier,
    onShowSnackbar: (String) -> Unit = {},
    onNavDown: () -> Unit = {}
) {

    val uiIncBus = incsVM.uiIncBus
    val uiMainVM = mainVM.uiMainState
    val uiInfoState = incsVM.uiInfoState

    val uiIncsState by incsVM.uiIncsState.collectAsState()

    val contexto = LocalContext.current

    Box(
        modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            itemsIndexed(uiIncsState.inc) { index, item ->
                DptoCard(
                    inc = item,
                    itemSelected = uiIncBus.incSelected == index,
                    incsVM = incsVM,
                    onItemSelectedChange = {
                        incsVM.setIncSelected(if (uiIncBus.incSelected != index) index else -1)
                    })
            }
        }

        if (mainVM.uiMainState.login?.id != 0 || uiIncBus.incSelected != -1) {
            FloatingActionButton(
                onClick = {
                    if (uiIncBus.incSelected == -1) {
                        incsVM.resetDatos()
                        incsVM.setDptoId(uiMainVM.login!!.id.toString())
                        incsVM.setFecha(LocalDate.now().toString())
                        incsVM.setId(
                            LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))
                                .toString()
                        )
                    }
                    onNavDown()
                },
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.BottomEnd)
            ) {
                if (uiIncBus.incSelected != -1) Icon(
                    imageVector = Icons.Filled.Star,
                    null
                ) else Icon(imageVector = Icons.Filled.Add, null)
            }
        }


    }
    if (uiIncBus.showDlgBorrar) {
        DlgConfirmacion(
            mensaje = R.string.txt_borrar,
            botonCancelarText = R.string.but_cancelar,
            botonAceptarText = R.string.but_actepar_borrado,
            onCancelarClick = { incsVM.setShowDlgBorrar(false) },
            onAceptarClick = {
                incsVM.baja()

                incsVM.resetDatos()
                incsVM.setShowDlgBorrar(false)
            },
            onDimmissRequest = { incsVM.setShowDlgBorrar(false) })
    }
    if (uiInfoState == IncsInfoState.Error) {
        onShowSnackbar(contexto.getString(R.string.borrar_ko))
        incsVM.resetInfoState()
    } else if (uiInfoState == IncsInfoState.Success) {
        onShowSnackbar(contexto.getString(R.string.borrar_ok))
        incsVM.resetInfoState()
    }
}


@Composable
fun DptoCard(
    inc: Incidencia,
    itemSelected: Boolean,
    incsVM: IncsVM,
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
                    painter = painterResource(R.drawable.campana),
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
                Row {
                    Text(
                        text = stringResource(id = R.string.show_dptoid_inc_card, inc.idDpto),
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.padding(5.dp))
                    Text(
                        text = stringResource(
                            id = R.string.show_aula_inc_card,
                            if (inc.idAula == null) "" else inc.idAula.toString()
                        ),
                    )
                }

                Text(
                    text = stringResource(id = R.string.show_fecha_inc_card, inc.fecha),
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = stringResource(
                        id = R.string.show_id_inc_card, inc.id
                    ),
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.padding(5.dp))

                Text(
                    text = stringResource(id = R.string.show_tipo_inc_card, inc.tipo),
                )
                Text(
                    text =
                    if (inc.estado) {
                        stringResource(id = R.string.show_estado_inc_card, EstadoInc.RESUELTA)
                    } else {
                        stringResource(id = R.string.show_estado_inc_card, EstadoInc.NORESUELTA)
                    }
                )
            }

            if (itemSelected) {
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = { incsVM.setShowDlgBorrar(true) }
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