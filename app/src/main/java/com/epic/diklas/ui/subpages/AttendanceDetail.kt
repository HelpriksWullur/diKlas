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
import androidx.compose.material3.Scaffold
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
import com.epic.diklas.DiKlasViewModel
import com.epic.diklas.data.Role
import com.epic.diklas.data.model.StudentViewModel
import com.epic.diklas.data.model.StudentViewModel.StudentHistoryData
import com.epic.diklas.data.model.TeacherViewModel
import com.epic.diklas.data.model.TeacherViewModel.TeacherHistoryData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendanceDetail(navController: NavController, code: String) {
    val viewModel: DiKlasViewModel = viewModel()
    val userData = viewModel.userDataState.collectAsState()

    val role = userData.value.role

    BackHandler(enabled = true) {
        navController.navigate("main_page")
    }
    Scaffold(
        topBar = { AttendanceDetailTopBar(navController) }
    ) { padding ->
        when (role) {
            Role.TEACHER -> AttendanceDetailContainer(Modifier.padding(padding), role, code)
            Role.STUDENT -> AttendanceDetailContainer(Modifier.padding(padding), role, code)
            else -> {}
        }
    }
}

@Composable
fun AttendanceDetailTopBar(navController: NavController) {
    Surface(
        color = MaterialTheme.colorScheme.background
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
                text = "DETAIL KEHADIRAN",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun AttendanceDetailContainer(modifier: Modifier = Modifier, role: Role, code: String) {
    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(1f)
                .padding(horizontal = 10.dp)
                .padding(top = 10.dp)
        ) {
            if (role == Role.TEACHER) {
                AttendanceDetailTeacher(code)
            } else {
                AttendanceDetailStudent(code)
            }
        }
    }
}

@Composable
fun AttendanceDetailTeacher(code: String) {
    val viewModel: TeacherViewModel = viewModel()
    val history = viewModel.historyDataTeacher.collectAsState()
    val historyData = if (history.value.isNotEmpty())  {
        history.value.filter {
            it.studentId == code
        }[0]
    } else {
        TeacherHistoryData("","","",0,0,0, emptyList())
    }

    Card(
        shape = MaterialTheme.shapes.small
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
                        text = historyData.name,
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
                        text = historyData.room,
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier
                            .weight(1f)
                    )
                }
        }
    }

    Card(
        shape = MaterialTheme.shapes.small,
        modifier = Modifier
            .padding(top = 10.dp, bottom = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = 4.dp)
        ) {
            Text(
                text = "Pertemuan",
                color = MaterialTheme.colorScheme.secondary,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .weight(1f)
            )
            Text(
                text = "Tanggal",
                color = MaterialTheme.colorScheme.secondary,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .weight(1f)
            )
            Text(
                text = "Keterangan",
                color = MaterialTheme.colorScheme.secondary,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .weight(1f)
            )
        }
    }
    LazyColumn {
        var index = 0
        items(historyData.attendances) {
            index += 1
            Row(
                modifier = Modifier
                    .padding(bottom = 6.dp)
            ) {
                Text(
                    text = "$index",
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier
                        .weight(1f)
                )
                Text(
                    text = "01/05/2023",
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier
                        .weight(1f)
                )
                Text(
                    text = when (it) {
                        0 -> "Tidak masuk"
                        1 -> "Masuk"
                        2 -> "Izin"
                        else -> {
                            "Belum mulai"
                        }
                    },
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier
                        .weight(1f)
                )
            }
        }
    }
}

@Composable
fun AttendanceDetailStudent(code: String) {
    val viewModel: StudentViewModel = viewModel()
    val history = viewModel.historyStudentState.collectAsState()
    val studentHistory = if (history.value.isNotEmpty()) {
        history.value.filter {
            it.classCode == code
        }[0]
    } else {
        StudentHistoryData("", "", "", "", 0, 0, 0, emptyList())
    }

    Card(
        shape = MaterialTheme.shapes.small
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
                        text = studentHistory.subject,
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
                        text = studentHistory.teacherName,
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier
                            .weight(1f)
                    )
            }
        }
    }
    Card(
        shape = MaterialTheme.shapes.small,
        modifier = Modifier
            .padding(top = 10.dp, bottom = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = 4.dp)
        ) {
            Text(
                text = "Pertemuan",
                color = MaterialTheme.colorScheme.secondary,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .weight(1f)
            )
            Text(
                text = "Tanggal",
                color = MaterialTheme.colorScheme.secondary,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .weight(1f)
            )
            Text(
                text = "Keterangan",
                color = MaterialTheme.colorScheme.secondary,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .weight(1f)
            )
        }
    }
    LazyColumn {
        var index = 0
        items(studentHistory.attendances) {
            index += 1
            Row(
                modifier = Modifier
                    .padding(bottom = 6.dp)
            ) {
                Text(
                    text = "$index",
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier
                        .weight(1f)
                )
                Text(
                    text = "01/05/2023",
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier
                        .weight(1f)
                )
                Text(
                    text = when (it) {
                        0 -> "Tidak masuk"
                        1 -> "Masuk"
                        2 -> "Izin"
                        else -> {
                            "Belum mulai"
                        }
                    },
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier
                        .weight(1f)
                )
            }
        }
    }

}