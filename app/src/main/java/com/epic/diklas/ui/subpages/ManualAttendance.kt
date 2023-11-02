package com.epic.diklas.ui.subpages

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.epic.diklas.data.AttendStat
import com.epic.diklas.data.model.TeacherViewModel
import com.epic.diklas.ui.theme.DiKlasTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManualAttendance(navController: NavController) {

    BackHandler {
        navController.navigate("main_page")
    }
    DiKlasTheme {
        Scaffold(
            topBar = { ManualAttendanceTopBar(navController) }
        ) { padding ->
            ManualAttendanceContent(Modifier.padding(padding))
        }
    }
}

@Composable
fun ManualAttendanceTopBar(navController: NavController) {
    Surface(
        color = MaterialTheme.colorScheme.background
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth(1f)
        ) {
            IconButton(
                onClick = {
                    navController.navigate("main_page")
                }
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowLeft,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(32.dp)
                )
            }

            Text(
                text = "ABSENSI MANUAL",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}


@Composable
fun ManualAttendanceContent(modifier: Modifier = Modifier) {
    val viewModel: TeacherViewModel = viewModel()
    val student = viewModel.manualAttendanceState.collectAsState()

    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(1f)
                .padding(horizontal = 10.dp)
        ) {
            Card(
                shape = RectangleShape,
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .padding(top = 16.dp, bottom = 10.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(horizontal = 10.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "Nama",
                        color = MaterialTheme.colorScheme.secondary,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .weight(1f)
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .weight(1f)
                    ) {
                        Text(
                            text = "Hadir",
                            color = MaterialTheme.colorScheme.secondary,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .weight(1f)
                        )
                        Text(
                            text = "Izin",
                            color = MaterialTheme.colorScheme.secondary,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .weight(1f)
                        )
                        Text(
                            text = "Tidak Hadir",
                            color = MaterialTheme.colorScheme.secondary,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .weight(1f)
                        )
                    }
                }
            }
            LazyColumn {
                items(student.value) { student ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth(1f)
                            .padding(bottom = 10.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .padding(horizontal = 10.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = student.name,
                                color = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier
                                    .weight(1f)
                            )
                            Row(
                                modifier = Modifier
                                    .weight(1f)
                            ) {
                                val status = remember { mutableStateOf(student.status) }
                                RadioButton(
                                    selected = status.value == AttendStat.ATTENDANCE,
                                    onClick = {
                                        viewModel.changeStatus(
                                            student.code,
                                            student.meet,
                                            1
                                        )
                                        status.value = AttendStat.ATTENDANCE
                                    },
                                    modifier = Modifier
                                        .weight(1f)
                                )
                                RadioButton(
                                    selected = status.value == AttendStat.PERMISSION,
                                    onClick = {
                                        viewModel.changeStatus(
                                            student.code,
                                            student.meet,
                                            2
                                        )
                                        status.value = AttendStat.PERMISSION
                                    },
                                    modifier = Modifier
                                        .weight(1f)
                                )
                                RadioButton(
                                    selected = status.value == AttendStat.NOT_ATTENDANCE,
                                    onClick = {
                                        viewModel.changeStatus(
                                            student.code,
                                            student.meet,
                                            0
                                        )
                                        status.value = AttendStat.NOT_ATTENDANCE
                                    },
                                    modifier = Modifier
                                        .weight(1f)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
