package com.epic.diklas.ui.subpages

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.epic.diklas.DiKlasViewModel
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskAdd(navController: NavController) {
    val viewModel: DiKlasViewModel = viewModel()
    val roomData = viewModel.teacherAndRoomData.collectAsState()
    val rooms = roomData.value

    BackHandler {
        navController.navigate("main_page")
    }

    Scaffold(
        topBar = { TaskAddTopBar(navController) }
    ) { padding ->
        TaskAddContent(Modifier.padding(padding), rooms)
    }
}

@Composable
fun TaskAddTopBar(navController: NavController) {
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
                text = "Buat Tugas",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskAddContent(modifier: Modifier, rooms: List<String>) {
    val context = LocalContext.current

    val calendar = Calendar.getInstance()

    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)

    val title = remember { mutableStateOf("") }
    val description = remember { mutableStateOf("") }

    Surface(
        color = MaterialTheme.colorScheme.surface,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(1f)
                .padding(horizontal = 10.dp)
                .padding(top = 10.dp)
        ) {
            val showDialog = remember { mutableStateOf(false) }

            if (showDialog.value) {
                AlertDialog(
                    onDismissRequest = { /*TODO*/ },
                    title = {
                        Text(text = "Unggah")
                    },
                    text = {
                        Text(
                            text = "Apakah Anda yakin untuk mengunggah?"
                        )
                    },
                    confirmButton = {
                        TextButton(
                            onClick = { /*TODO*/ }
                        ) {
                            Text(
                                text = "Ya"
                            )
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = {
                                showDialog.value = false
                            }
                        ) {
                            Text(
                                text = "Tidak"
                            )
                        }
                    }
                )
            }

            var mExpanded by remember { mutableStateOf(false) }
            var mTextFieldSize by remember { mutableStateOf(Size.Zero) }
            var selectedText by remember { mutableStateOf("") }

            val icon = if (mExpanded) {
                Icons.Default.KeyboardArrowUp
            } else {
                Icons.Default.KeyboardArrowDown
            }

            Column(
                modifier = Modifier
                    .padding(bottom = 10.dp)
            ) {
                Text(
                    text = "Kelas",
                    fontWeight = FontWeight.Bold
                )
                OutlinedTextField(
                    value = selectedText,
                    onValueChange = {},
                    readOnly = true,
                    placeholder = {
                        Text(
                            text = "-- Pilih Kelas --",
                            color = MaterialTheme.colorScheme.secondary
                        )
                    },
                    trailingIcon = {
                        Icon(
                            imageVector = icon, contentDescription = null,
                            Modifier.clickable {
                                mExpanded = !mExpanded
                            }
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .onGloballyPositioned { coordinates ->
                            mTextFieldSize = coordinates.size.toSize()
                        }
                )
                DropdownMenu(
                    expanded = mExpanded,
                    onDismissRequest = { mExpanded = false },
                    modifier = Modifier
                        .width(with(LocalDensity.current) { mTextFieldSize.width.toDp() })
                ) {
                    rooms.forEach { label ->
                        DropdownMenuItem(
                            text = {
                                Text(text = label)
                            },
                            onClick = {
                                selectedText = label
                                mExpanded = false
                            }
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .padding(bottom = 10.dp)
            ) {
                Text(
                    text = "Judul Tugas",
                    fontWeight = FontWeight.Bold
                )
                Row {
                    OutlinedTextField(
                        value = title.value,
                        onValueChange = {
                            title.value = it
                        },
                        placeholder = {
                            Text(
                                text = "Ketik judul tugas",
                                color = MaterialTheme.colorScheme.secondary
                            )
                        },
                        modifier = Modifier
                            .weight(1f)
                    )
                }
            }
            Column(
                modifier = Modifier
                    .padding(bottom = 10.dp)
            ) {
                Text(
                    text = "Deskripsi",
                    fontWeight = FontWeight.Bold
                )
                Row {
                    OutlinedTextField(
                        value = description.value,
                        onValueChange = {
                            description.value = it
                        },
                        placeholder = {
                            Text(
                                text = "Ketik deskripsi tugas",
                                color = MaterialTheme.colorScheme.secondary
                            )
                        },
                        modifier = Modifier
                            .weight(1f)
                    )
                }
            }

            val dateText = remember { mutableStateOf("$day/${month + 1}/$year") }
            val timeText = remember { mutableStateOf("$hour:$minute") }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth(1f)
            ) {
                Row {
                    Text(
                        text = "Tenggat Waktu",
                        fontWeight = FontWeight.Bold
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier
                        .fillMaxWidth(1f)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Tanggal"
                        )
                        Text(text = dateText.value)
                        OutlinedButton(
                            onClick = {
                                DatePickerDialog(
                                    context,
                                    { _: DatePicker, pickedYear: Int, pickedMonth: Int, pickedDay: Int ->
                                        dateText.value = "$pickedDay/${pickedMonth + 1}/$pickedYear"
                                    },
                                    year,
                                    month,
                                    day
                                ).show()
                            },
                            shape = MaterialTheme.shapes.medium,
                            contentPadding = PaddingValues(all = 0.dp)
                        ) {
                            Text(
                                text = "Atur Tanggal",
                                modifier = Modifier
                                    .padding(horizontal = 10.dp)
                            )
                        }
                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Jam"
                        )
                        Text(
                            text = timeText.value
                        )
                        OutlinedButton(
                            onClick = {
                                TimePickerDialog(
                                    context, { _: TimePicker, pickedHour: Int, pickedMinute: Int ->
                                        timeText.value =
                                            "${if (pickedHour.toString().length < 2) "0$pickedHour" else pickedHour}:${if (pickedMinute.toString().length < 2) "0$pickedMinute" else pickedMinute}"
                                    }, hour, minute, true
                                ).show()
                            },
                            shape = MaterialTheme.shapes.medium,
                            contentPadding = PaddingValues(all = 0.dp)
                        ) {
                            Text(
                                text = "Atur Jam",
                                modifier = Modifier
                                    .padding(horizontal = 10.dp)
                            )
                        }
                    }
                }
            }
            Button(
                onClick = {
                    if (selectedText.isNotEmpty() && description.value.isNotEmpty()) {
                        showDialog.value = true
                    } else {
                        if (selectedText.isEmpty() && description.value.isEmpty()) {
                            Toast.makeText(
                                context,
                                "Kelas dan deskripsi belum diatur",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else if (selectedText.isEmpty() && description.value.isNotEmpty()) {
                            Toast.makeText(
                                context,
                                "Kelas belum dipilih",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else if (selectedText.isNotEmpty() && description.value.isEmpty()) {
                            Toast.makeText(
                                context,
                                "Deskripsi belum diisi",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                },
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .padding(top = 10.dp)
            ) {
                Text(
                    text = "Unggah Tugas"
                )
            }
        }
    }
}
