package com.epic.diklas.ui.fragments.contents

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.epic.diklas.data.model.StudentViewModel
import com.epic.diklas.data.model.TeacherViewModel

@Composable
fun StudentHistory(modifier: Modifier, navController: NavController) {
    val studentVM: StudentViewModel = viewModel()
    val history = studentVM.historyStudentState.collectAsState()

    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = modifier
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(1f)
                .padding(horizontal = 10.dp)
                .padding(top = 16.dp)
        ) {
            items(history.value) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .padding(bottom = 10.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 6.dp, vertical = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(bottom = 6.dp)
                        ) {
                            Text(
                                text = it.subject,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .weight(1f)
                            )
                        }
                        Row {
                            Text(
                                text = "Guru",
                                color = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier
                                    .weight(1f)
                            )
                            Text(
                                text = it.teacherName,
                                textAlign = TextAlign.End,
                                color = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier
                                    .weight(1f)
                            )
                        }
                        Row {
                            Text(
                                text = "Pertemuan",
                                color = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier
                                    .weight(1f)
                            )
                            Text(
                                text = it.meet.toString(),
                                textAlign = TextAlign.End,
                                color = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier
                                    .weight(1f)
                            )
                        }
                        Row {
                            Text(
                                text = "Jumlah Masuk",
                                color = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier
                                    .weight(1f)
                            )
                            Text(
                                text = it.totalAttendances.toString(),
                                textAlign = TextAlign.End,
                                color = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier
                                    .weight(1f)
                            )
                        }
                        Row {
                            Text(
                                text = "Total Pertemuan",
                                color = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier
                                    .weight(1f)
                            )
                            Text(
                                text = it.totalMeet.toString(),
                                textAlign = TextAlign.End,
                                color = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier
                                    .weight(1f)
                            )
                        }
                        OutlinedButton(
                            onClick = {
                                navController.navigate("attendance_detail/${it.classCode}")
                            },
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                            modifier = Modifier
                                .fillMaxWidth(1f)
                                .padding(top = 6.dp)
                        ) {
                            Text(
                                text = "Rincian"
                            )
                        }
                    }
                }

            }
        }
    }
}

@Composable
fun TeacherHistory(modifier: Modifier, navController: NavController) {
    val viewModel: TeacherViewModel = viewModel()
    val history = viewModel.historyDataTeacher.collectAsState()

    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = modifier
            .fillMaxSize(1f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(1f)
                .padding(horizontal = 10.dp)
                .padding(top = 10.dp)
        ) {
            LazyColumn {
                items(history.value) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth(1f)
                            .padding(bottom = 10.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(horizontal = 6.dp, vertical = 4.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(bottom = 6.dp)
                            ) {
                                Text(
                                    text = "${it.room} - ${it.name}",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier
                                        .weight(1f)
                                )
                            }
                            Row {
                                Text(
                                    text = "Pertemuan",
                                    color = MaterialTheme.colorScheme.secondary,
                                    modifier = Modifier
                                        .weight(1f)
                                )
                                Text(
                                    text = it.meet.toString(),
                                    textAlign = TextAlign.End,
                                    color = MaterialTheme.colorScheme.secondary,
                                    modifier = Modifier
                                        .weight(1f)
                                )
                            }
                            Row {
                                Text(
                                    text = "Jumlah Masuk",
                                    color = MaterialTheme.colorScheme.secondary,
                                    modifier = Modifier
                                        .weight(1f)
                                )
                                Text(
                                    text = it.totalAttendances.toString(),
                                    textAlign = TextAlign.End,
                                    color = MaterialTheme.colorScheme.secondary,
                                    modifier = Modifier
                                        .weight(1f)
                                )
                            }
                            Row {
                                Text(
                                    text = "Total Pertemuan",
                                    color = MaterialTheme.colorScheme.secondary,
                                    modifier = Modifier
                                        .weight(1f)
                                )
                                Text(
                                    text = it.totalMeet.toString(),
                                    textAlign = TextAlign.End,
                                    color = MaterialTheme.colorScheme.secondary,
                                    modifier = Modifier
                                        .weight(1f)
                                )
                            }
                            OutlinedButton(
                                onClick = {
                                    navController.navigate("attendance_detail/${it.studentId}")
                                },
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                                modifier = Modifier
                                    .fillMaxWidth(1f)
                            ) {
                                Text(
                                    text = "Rincian"
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}