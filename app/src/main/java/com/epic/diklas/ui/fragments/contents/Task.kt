package com.epic.diklas.ui.fragments.contents

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.epic.diklas.data.TaskStat
import com.epic.diklas.data.model.StudentViewModel
import com.epic.diklas.data.model.TeacherViewModel

@Composable
fun StudentTask(modifier: Modifier = Modifier, navController: NavController) {
    val viewModel: StudentViewModel = viewModel()
    val taskData = viewModel.studentTaskData.collectAsState()

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
            items(taskData.value) {
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
                                text = "Batas Waktu",
                                color = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier
                                    .weight(1f)
                            )
                            Text(
                                text = it.deadline,
                                textAlign = TextAlign.End,
                                color = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier
                                    .weight(1f)
                            )
                        }

                        val statusText = when (it.statusTask) {
                            TaskStat.NOT_DONE -> "Belum dikerjakan"
                            TaskStat.ASSESSED -> "Sudah dinilai"
                            TaskStat.NOT_YET_ASSESSED -> "Belum dinilai"
                            TaskStat.LATE -> "Terlambat"
                        }
                        Row {
                            Text(
                                text = "Status",
                                color = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier
                                    .weight(1f)
                            )
                            Text(
                                text = statusText,
                                textAlign = TextAlign.End,
                                color = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier
                                    .weight(1f)
                            )
                        }
                        OutlinedButton(
                            onClick = {
                                navController.navigate(route = "task_desk/${it.taskCode}")
                                Log.d("Data", "Task code: ${it.taskCode}")
                            },
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                            modifier = Modifier
                                .fillMaxWidth(1f)
                                .padding(top = 6.dp)
                        ) {
                            Text(
                                text = "Selebihnya"
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TeacherTask(modifier: Modifier, navController: NavController) {
    val viewModel: TeacherViewModel = viewModel()
    val task = viewModel.teacherTaskState.collectAsState()

    Surface(
        color = MaterialTheme.colorScheme.background,
    ) {
        Box(
            modifier = modifier
                .fillMaxSize(1f)
        ) {
            Column {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize(1f)
                        .padding(horizontal = 10.dp)
                        .padding(top = 16.dp)
                ) {
                    items(task.value) {task ->
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
                                        text = "${task.studentClass} - ${task.studentName}",
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
                                        text = "Batas Waktu",
                                        color = MaterialTheme.colorScheme.secondary,
                                        modifier = Modifier
                                            .weight(1f)
                                    )
                                    Text(
                                        text = task.deadline,
                                        textAlign = TextAlign.End,
                                        color = MaterialTheme.colorScheme.secondary,
                                        modifier = Modifier
                                            .weight(1f)
                                    )
                                }

                                val statusText = when (task.statusTask) {
                                    TaskStat.NOT_DONE -> "Belum dikerjakan"
                                    TaskStat.ASSESSED -> "Sudah dinilai"
                                    TaskStat.NOT_YET_ASSESSED -> "Belum dinilai"
                                    TaskStat.LATE -> "Terlambat"
                                }

                                Row {
                                    Text(
                                        text = "Status",
                                        color = MaterialTheme.colorScheme.secondary,
                                        modifier = Modifier
                                            .weight(1f)
                                    )
                                    Text(
                                        text = statusText,
                                        textAlign = TextAlign.End,
                                        color = MaterialTheme.colorScheme.secondary,
                                        modifier = Modifier
                                            .weight(1f)
                                    )
                                }
                                OutlinedButton(
                                    onClick = {
                                              navController.navigate("task_desk/${task.studentId}")
                                    },
                                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                                    modifier = Modifier
                                        .fillMaxWidth(1f)
                                        .padding(top = 6.dp)
                                ) {
                                    Text(
                                        text = "Selebihnya"
                                    )
                                }
                            }
                        }
                    }
                }
            }
            FloatingActionButton(
                onClick = {
                    navController.navigate("task_add")
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 10.dp, end = 10.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(horizontal = 6.dp)
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null)
                    Text(
                        text = "Buat Tugas"
                    )
                }
            }
        }
    }
}
