package com.epic.diklas.ui.fragments.contents

import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.epic.diklas.DiKlasViewModel
import com.epic.diklas.R
import com.epic.diklas.data.AttendStat
import com.epic.diklas.data.ClassStat
import com.epic.diklas.data.model.TeacherViewModel
import com.epic.diklas.rememberQrBitmapPainter
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

@Composable
fun StudentAttendance(modifier: Modifier = Modifier, navController: NavController) {
    val viewModel: DiKlasViewModel = viewModel()
    val scheduleData = viewModel.scheduleDataState.collectAsState()
    val schedule = scheduleData.value

    val studentAttendance = viewModel.studentAttendanceState.collectAsState()
    val attendanceStatus = if (studentAttendance.value.isNotEmpty()) {
        when (studentAttendance.value[schedule.meet-1]) {
            0 -> AttendStat.NOT_ATTENDANCE
                1 -> AttendStat.ATTENDANCE
                    2 -> AttendStat.PERMISSION
            else -> AttendStat.NOT_ATTENDANCE
        }
    } else {
        AttendStat.NOT_ATTENDANCE
    }

    val context = LocalContext.current

    Surface(
        color = MaterialTheme.colorScheme.background, modifier = modifier
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {
            Card(
                modifier = Modifier.padding(bottom = 10.dp)
            ) {
                Row(
                    modifier = Modifier.padding(8.dp)
                ) {
                    var statText = ""
                    statText = when (schedule.status) {
//                        ClassStat.STARTED -> if (attendanceStatus == AttendStat.ATTENDANCE) "Anda di dalam kelas" else "Kelas sedang berjalan"
                        ClassStat.STARTED ->
                            when (attendanceStatus) {
                               AttendStat.ATTENDANCE -> "Anda di dalam kelas"
                                AttendStat.NOT_ATTENDANCE -> "Kelas sedang berjalan"
                                AttendStat.PERMISSION -> "Anda sedang izin dari kelas"
                            }
                        ClassStat.NOT_YET_STARTED -> "Kelas belum mulai"
                        else -> "Tidak ada kelas"
                    }
                    Text(
                        text = statText,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            Card(
                modifier = Modifier.padding(bottom = 10.dp)
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)
                ) {
                    if (schedule.status != ClassStat.NO_CLASS) {
                        Row(
                            modifier = Modifier.fillMaxWidth(1f)
                        ) {
                            Text(
                                text = "Subjek",
                                fontSize = 18.sp,
                                color = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = schedule.subject,
                                fontSize = 18.sp,
                                color = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier.weight(1f)
                            )
                        }
                        Row(
                        ) {
                            Text(
                                text = "Pertemuan",
                                fontSize = 18.sp,
                                color = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = schedule.meet.toString(),
                                fontSize = 18.sp,
                                color = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier.weight(1f)
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(1f)
                        ) {
                            Text(
                                text = "Guru",
                                fontSize = 18.sp,
                                color = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = schedule.teacherName,
                                fontSize = 18.sp,
                                color = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier.weight(1f)
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(1f)
                        ) {
                            Text(
                                text = "Waktu",
                                fontSize = 18.sp,
                                color = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = schedule.scheduleTime,
                                fontSize = 18.sp,
                                color = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    } else {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(1f)
                                .padding(vertical = 10.dp)
                        ) {
                            Text(
                                text = "Silahkan masuk lagi besok!",
                                textAlign = TextAlign.Center,
                                fontSize = 18.sp,
                                color = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier
                                    .weight(1f)
                            )
                        }
                    }
                }
            }
            if (schedule.status == ClassStat.STARTED && attendanceStatus != AttendStat.ATTENDANCE) {
//            if (schedule.status == ClassStat.STARTED) {
                val permission = android.Manifest.permission.CAMERA
                val isCameraPermissionGranted = remember { mutableStateOf(checkPermission(context, permission)) }

                val askPermission = rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission(), onResult = { isGranted ->
                    isCameraPermissionGranted.value = isGranted
                })

                Button(shape = MaterialTheme.shapes.medium,
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 0.dp),
                    onClick = {
                        if (!isCameraPermissionGranted.value) {
                            askPermission.launch(permission)
                        } else {
                            navController.navigate("scanner")
                        }
                    }) {
                    Icon(
                        painter = painterResource(id = R.drawable.img_qr_scanner),
                        contentDescription = null
                    )
                    Text(
                        text = "Pindai QR Kelas",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
            }
        }
    }
}

private fun checkPermission(context: Context, permission: String): Boolean {
    return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
}

@Composable
fun TeacherAttendance(modifier: Modifier, navController: NavController) {
    val viewModel: DiKlasViewModel = viewModel()
    val scheduleData = viewModel.scheduleDataState.collectAsState()
    val schedule = scheduleData.value

    val teacherVM: TeacherViewModel = viewModel()

    Surface(
        color = MaterialTheme.colorScheme.background, modifier = modifier
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize(1f)
                .padding(horizontal = 20.dp)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .padding(bottom = 10.dp)
            ) {
                Row(
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    var statText = ""
                    statText = when (schedule.status) {
                        ClassStat.STARTED -> "Kelas sedang berjalan"
                        ClassStat.NOT_YET_STARTED -> "Kelas belum mulai"
                        else -> "Tidak ada kelas"
                    }
                    Text(
                        text = statText,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            Card(
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .padding(bottom = 10.dp)
            ) {
                if (schedule.status != ClassStat.NO_CLASS) {
                    Column(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)
                    ) {
                        Row {
                            Text(
                                text = "Kelas",
                                fontSize = 18.sp,
                                color = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = schedule.room.toString(),
                                fontSize = 18.sp,
                                color = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier.weight(1f)
                            )
                        }
                        Row {
                            Text(
                                text = "Pertemuan",
                                fontSize = 18.sp,
                                color = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = schedule.meet.toString(),
                                fontSize = 18.sp,
                                color = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier.weight(1f)
                            )
                        }
                        Row {
                            Text(
                                text = "Waktu",
                                fontSize = 18.sp,
                                color = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = schedule.scheduleTime,
                                fontSize = 18.sp,
                                color = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                } else {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(1f)
                            .padding(vertical = 10.dp)
                    ) {
                        Text(
                            text = "Silahkan masuk lagi besok!",
                            textAlign = TextAlign.Center,
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier
                                .weight(1f)
                        )
                    }
                }
            }
            if (schedule.status == ClassStat.STARTED) {
                var isShowed by remember { mutableStateOf(false) }

                if (isShowed) {
                    LaunchedEffect(schedule.code) {
                        delay(5.seconds)
                        teacherVM.changeQR(schedule.keyCode, generateCode())
                    }
                }

                val imgQR = rememberQrBitmapPainter(content = schedule.code)

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .padding(vertical = 4.dp)
                ) {
                    if (isShowed) {
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White
                            ),
                            modifier = Modifier
                                .padding(bottom = 10.dp)
                        ) {
                            Image(
                                painter = imgQR,
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(10.dp)
                            )
                        }
                    }
                    Button(
                        onClick = {
                            isShowed = !isShowed
                        },
                        shape = MaterialTheme.shapes.medium,
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 0.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.img_qr_scanner),
                            contentDescription = null
                        )
                        Text(
                            text = if (isShowed) "Sembunyikan QR" else "Tampilkan QR",
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }
                }
                Text(
                    text = "atau",
                    modifier = Modifier
                        .padding(top = 6.dp)
                )
                TextButton(
                    onClick = {
                        navController.navigate("manual_attendance")
                    }
                ) {
                    Text(
                        text = "coba absensi manual"
                    )
                }
            }
        }
    }
}

private fun generateCode(): String {
    val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
    return (1..32).map { allowedChars.random() }.joinToString("")
}
