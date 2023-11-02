package com.epic.diklas.ui.subpages

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.epic.diklas.DiKlasViewModel
import com.epic.diklas.data.Role
import com.epic.diklas.data.TaskStat
import com.epic.diklas.data.model.StudentViewModel
import com.epic.diklas.data.model.StudentViewModel.StudentTaskData
import com.epic.diklas.data.model.TeacherViewModel
import com.epic.diklas.data.model.TeacherViewModel.TeacherTaskData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDesk(navControllerMain: NavController, code: String) {
    val viewModel: DiKlasViewModel = viewModel()
    val userData = viewModel.userDataState.collectAsState()
    val role = userData.value.role

    BackHandler {
        navControllerMain.navigate("main_page")
    }

    Scaffold(
        topBar = { TaskDeskTopBar(navControllerMain) }
    ) { padding ->
        if (role == Role.TEACHER) {
            TaskDeskContentTeacher(Modifier.padding(padding), code)
        } else {
            TaskDeskContentStudent(Modifier.padding(padding), code)
        }
    }
}

@Composable
fun TaskDeskTopBar(navController: NavController) {
    Surface(
        color = MaterialTheme.colorScheme.background,
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
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
                text = "Kerjakan Tugas",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDeskContentStudent(modifier: Modifier, code: String) {
    val viewModel: StudentViewModel = viewModel()
    val taskData = viewModel.studentTaskData.collectAsState()

    val task = if (taskData.value.isNotEmpty()) {
            taskData.value.filter {
                it.taskCode == code
            }[0]
    } else {
        StudentTaskData("", "0", "", "", "", TaskStat.NOT_DONE, "", "", 0)
    }

    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(1f)
                .padding(horizontal = 10.dp)
                .padding(top = 10.dp)
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.TopCenter)
            ) {
                var answer by remember { mutableStateOf(task.answer ?: "") }
                val note by remember { mutableStateOf("") }

                Card(
                    shape = MaterialTheme.shapes.small,
                    modifier = Modifier
                        .fillMaxWidth(1f)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 10.dp, vertical = 8.dp)
                    ) {
                        Row {
                            Text(
                                text = "Subjek",
                                color = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier
                                    .weight(1f)
                            )
                            Text(
                                text = task.subject,
                                color = MaterialTheme.colorScheme.secondary,
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
                                text = task.teacherName,
                                color = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier
                                    .weight(1f)
                            )
                        }
                        Row {
                            Text(
                                text = "Tenggat Waktu",
                                color = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier
                                    .weight(1f)
                            )
                            Text(
                                text = task.deadline,
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
                                color = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier
                                    .weight(1f)
                            )
                        }
                    }
                }
                Column(
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    if (task.statusTask == TaskStat.ASSESSED || task.statusTask == TaskStat.NOT_YET_ASSESSED) {
                        Card(
                            shape = MaterialTheme.shapes.small,
                            modifier = Modifier
                                .fillMaxWidth(1f)
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(horizontal = 10.dp, vertical = 8.dp)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .padding(bottom = 6.dp)
                                ) {
                                    Text(
                                        text = "Deskripsi",
                                        color = MaterialTheme.colorScheme.secondary,
                                        fontWeight = FontWeight.Bold,
                                    )
                                    Text(
                                        text = task.description,
                                        color = MaterialTheme.colorScheme.secondary,
                                    )
                                }
                                Column {
                                    Text(
                                        text = "Jawaban",
                                        color = MaterialTheme.colorScheme.secondary,
                                        fontWeight = FontWeight.Bold,
                                    )
                                    Text(
                                        text = task.answer,
                                        color = MaterialTheme.colorScheme.secondary,
                                    )
                                }
                            }
                        }
                        if (task.statusTask == TaskStat.ASSESSED) {
                            Card(
                                shape = MaterialTheme.shapes.small,
                                modifier = Modifier
                                    .fillMaxWidth(1f)
                                    .padding(top = 10.dp)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .padding(horizontal = 10.dp, vertical = 10.dp)
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier
                                            .fillMaxWidth(1f)
                                            .padding(bottom = 6.dp)
                                    ) {
                                        Text(
                                            text = "Nilai",
                                            color = MaterialTheme.colorScheme.secondary,
                                            fontWeight = FontWeight.Bold,
                                        )
                                        Text(
                                            text = task.score.toString(),
                                            color = MaterialTheme.colorScheme.secondary,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 42.sp,
                                        )
                                    }
                                    Text(
                                        text = "Catatan Guru",
                                        color = MaterialTheme.colorScheme.secondary,
                                        fontWeight = FontWeight.Bold,
                                    )
                                    Text(
                                        text = task.note,
                                        color = MaterialTheme.colorScheme.secondary
                                    )
                                }
                            }
                        }
                    }
                }
            }

            if (task.statusTask != TaskStat.ASSESSED && task.statusTask != TaskStat.NOT_YET_ASSESSED) {
                var answer by remember { mutableStateOf("") }

                Column(
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .align(Alignment.BottomCenter)
                ) {
                    Card(
                        shape = MaterialTheme.shapes.small,
                        modifier = Modifier
                            .fillMaxWidth(1f)
                            .padding(bottom = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(horizontal = 10.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = "Deskripsi",
                                color = MaterialTheme.colorScheme.secondary,
                                fontWeight = FontWeight.Bold,
                            )
                            Text(
                                text = task.description,
                                color = MaterialTheme.colorScheme.secondary,
                            )
                        }
                    }
                    Row(
                        verticalAlignment = Alignment.Bottom,
                        modifier = Modifier
                            .fillMaxWidth(1f)
                            .padding(bottom = 10.dp)
                    ) {
                        TextField(
                            value = answer,
                            onValueChange = { answer = it },
                            shape = MaterialTheme.shapes.small,
                            colors = TextFieldDefaults.textFieldColors(
                                disabledIndicatorColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            ),
                            placeholder = {
                                Text(text = "Ketik jawaban kamu disini...")
                            },
                            modifier = Modifier
                                .weight(1f)
                        )
                        Button(
                            shape = MaterialTheme.shapes.small,
                            onClick = { /*TODO*/ },
                            enabled = answer.isNotEmpty(),
                            modifier = Modifier
                                .padding(start = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Send,
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(vertical = 8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDeskContentTeacher(modifier: Modifier, code: String) {
    val viewModel: TeacherViewModel = viewModel()
    val taskData = viewModel.teacherTaskState.collectAsState()

    val task = if (taskData.value.isNotEmpty()) {
        taskData.value.filter {
            it.studentId == code
        }[0]
    } else {
        TeacherTaskData("", "0", "", "", "", TaskStat.NOT_DONE, "", "", 0)
    }

    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(1f)
                .padding(horizontal = 10.dp)
                .padding(top = 10.dp)
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.TopCenter)
            ) {
                var answer by remember { mutableStateOf(task.answer ?: "") }
                val note by remember { mutableStateOf("") }

                Card(
                    shape = MaterialTheme.shapes.small,
                    modifier = Modifier
                        .fillMaxWidth(1f)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 10.dp, vertical = 8.dp)
                    ) {
                        Row {
                            Text(
                                text = "Nama",
                                color = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier
                                    .weight(1f)
                            )
                            Text(
                                text = task.studentName,
                                color = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier
                                    .weight(1f)
                            )
                        }
                        Row {
                            Text(
                                text = "Kelas",
                                color = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier
                                    .weight(1f)
                            )
                            Text(
                                text = task.studentClass,
                                color = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier
                                    .weight(1f)
                            )
                        }
                        Row {
                            Text(
                                text = "Tenggat Waktu",
                                color = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier
                                    .weight(1f)
                            )
                            Text(
                                text = task.deadline,
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
                                color = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier
                                    .weight(1f)
                            )
                        }
                    }
                }
                Column(
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    if (task.statusTask == TaskStat.ASSESSED || task.statusTask == TaskStat.NOT_YET_ASSESSED) {
                        Card(
                            shape = MaterialTheme.shapes.small,
                            modifier = Modifier
                                .fillMaxWidth(1f)
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(horizontal = 10.dp, vertical = 8.dp)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .padding(bottom = 6.dp)
                                ) {
                                    Text(
                                        text = "Deskripsi",
                                        color = MaterialTheme.colorScheme.secondary,
                                        fontWeight = FontWeight.Bold,
                                    )
                                    Text(
                                        text = task.description,
                                        color = MaterialTheme.colorScheme.secondary,
                                    )
                                }
                                Column {
                                    Text(
                                        text = "Jawaban",
                                        color = MaterialTheme.colorScheme.secondary,
                                        fontWeight = FontWeight.Bold,
                                    )
                                    Text(
                                        text = task.answer,
                                        color = MaterialTheme.colorScheme.secondary,
                                    )
                                }
                            }
                        }
                        if (task.statusTask == TaskStat.ASSESSED) {
                            Card(
                                shape = MaterialTheme.shapes.small,
                                modifier = Modifier
                                    .fillMaxWidth(1f)
                                    .padding(top = 10.dp)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .padding(horizontal = 10.dp, vertical = 10.dp)
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier
                                            .fillMaxWidth(1f)
                                            .padding(bottom = 6.dp)
                                    ) {
                                        Text(
                                            text = "Nilai",
                                            color = MaterialTheme.colorScheme.secondary,
                                            fontWeight = FontWeight.Bold,
                                        )
                                        Text(
                                            text = task.score.toString(),
                                            color = MaterialTheme.colorScheme.secondary,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 42.sp,
                                        )
                                    }
                                    Text(
                                        text = "Catatan Guru",
                                        color = MaterialTheme.colorScheme.secondary,
                                        fontWeight = FontWeight.Bold,
                                    )
                                    Text(
                                        text = task.note,
                                        color = MaterialTheme.colorScheme.secondary
                                    )
                                }
                            }
                        }
                    }
                }
            }

            if (task.statusTask != TaskStat.ASSESSED && task.statusTask != TaskStat.NOT_YET_ASSESSED) {
                var answer by remember { mutableStateOf("") }

                Column(
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .align(Alignment.BottomCenter)
                ) {
                    Card(
                        shape = MaterialTheme.shapes.small,
                        modifier = Modifier
                            .fillMaxWidth(1f)
                            .padding(bottom = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(horizontal = 10.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = "Deskripsi",
                                color = MaterialTheme.colorScheme.secondary,
                                fontWeight = FontWeight.Bold,
                            )
                            Text(
                                text = task.description,
                                color = MaterialTheme.colorScheme.secondary,
                            )
                        }
                    }
                    Row(
                        verticalAlignment = Alignment.Bottom,
                        modifier = Modifier
                            .fillMaxWidth(1f)
                            .padding(bottom = 10.dp)
                    ) {
                        TextField(
                            value = answer,
                            onValueChange = { answer = it },
                            shape = MaterialTheme.shapes.small,
                            colors = TextFieldDefaults.textFieldColors(
                                disabledIndicatorColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            ),
                            placeholder = {
                                Text(text = "Ketik jawaban kamu disini...")
                            },
                            modifier = Modifier
                                .weight(1f)
                        )
                        Button(
                            shape = MaterialTheme.shapes.small,
                            onClick = { /*TODO*/ },
                            enabled = answer.isNotEmpty(),
                            modifier = Modifier
                                .padding(start = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Send,
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(vertical = 8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
