package com.epic.diklas.ui.pages

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.epic.diklas.DiKlasViewModel
import com.epic.diklas.DiKlasViewModel.UserData
import com.epic.diklas.R
import com.epic.diklas.data.Role
import com.epic.diklas.data.position
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Profile(navController: NavController) {
    val viewModel: DiKlasViewModel = viewModel()

    BackHandler(enabled = true) {
        navController.navigate("main_page")
    }
    Scaffold(
        topBar = { ProfileTopBar(navController) },
        bottomBar = { ProfileBottomBar() })
    { padding ->
        ProfileContent(Modifier.padding(padding), navController, viewModel)
    }
}

@Composable
fun ProfileTopBar(navController: NavController) {
    Surface(
        color = MaterialTheme.colorScheme.background
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
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
                    modifier = Modifier.size(32.dp)
                )
            }
            Text(
                text = "PROFIL",
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun ProfileContent(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: DiKlasViewModel
) {
    val profileData = viewModel.userDataState.collectAsState()
    val auth = Firebase.auth

    val showDialog = remember { mutableStateOf(false) }

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
            when (profileData.value.role) {
                Role.STUDENT -> ProfileContentStudent(profileData.value)
                Role.TEACHER -> ProfileContentTeacher(profileData.value)
                else -> {}
            }
            if (showDialog.value) {
                AlertDialog(
                    onDismissRequest = { },
                    title = {
                        Text(
                            text = "Logout",
                        )
                    },
                    text = {
                        Text(
                            text = "Apakah Anda yakin ingin logout?",
                        )
                    },
                    shape = MaterialTheme.shapes.medium,
                    confirmButton = {
                        TextButton(
                            onClick = {
                                navController.navigate("login") {
                                    popUpTo(navController.graph.startDestinationId) {
                                        inclusive = true
                                    }
                                    launchSingleTop = true
                                }
                                auth.signOut()
                                position = ""
                            }
                        ) {
                            Text(
                                text = "Ya",
                                fontWeight = FontWeight.Bold
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
                                text = "Tidak",
                                color = Color.LightGray,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                )
            }
            Button(
                onClick = {
                    showDialog.value = true
                },
                shape = MaterialTheme.shapes.medium,
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 0.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ExitToApp, contentDescription = null
                )
                Text(
                    text = "LOGOUT",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
        }
    }
}

@Composable
fun ProfileContentStudent(userData: UserData) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.padding(horizontal = 10.dp)
    ) {
        Box(
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            Card(
                modifier = Modifier
                    .padding(top = 68.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .padding(horizontal = 10.dp)
                        .padding(top = 120.dp, bottom = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(bottom = 8.dp)
                    ) {
                        Text(
                            text = "Nama",
                            color = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 20.dp)
                        )
                        Text(
                            text = userData.name,
                            color = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Row(
                        modifier = Modifier.padding(bottom = 8.dp)
                    ) {
                        Text(
                            text = "NIS",
                            color = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 20.dp)
                        )
                        Text(
                            text = userData.id,
                            color = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Row {
                        Text(
                            text = "Kelas",
                            color = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 20.dp)
                        )
                        Text(
                            text = userData.classOrSubject,
                            color = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .align(Alignment.TopCenter)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_default_profile),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(136.dp)
                        .clip(CircleShape)
                )
                TextButton(onClick = { /*TODO*/ }) {
                    Text(text = "ubah foto profil")
                }
            }
        }
    }
}

@Composable
fun ProfileContentTeacher(userData: UserData) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.padding(horizontal = 10.dp)
    ) {
        Box(
            modifier = Modifier.padding(bottom = 6.dp)
        ) {
            Card(
                modifier = Modifier.padding(top = 68.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .padding(horizontal = 10.dp)
                        .padding(top = 120.dp, bottom = 10.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(bottom = 8.dp)
                    ) {
                        Text(
                            text = "Nama",
                            color = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 20.dp)
                        )
                        Text(
                            text = userData.name,
                            color = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Row(
                        modifier = Modifier.padding(bottom = 8.dp)
                    ) {
                        Text(
                            text = "NIP",
                            color = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 20.dp)
                        )
                        Text(
                            text = userData.id,
                            color = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Row {
                        Text(
                            text = "Subjek",
                            color = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 20.dp)
                        )
                        Text(
                            text = userData.classOrSubject,
                            color = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .align(Alignment.TopCenter)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_default_profile),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(136.dp)
                        .clip(CircleShape)
                )
                TextButton(onClick = { /*TODO*/ }) {
                    Text(text = "ubah foto profil")
                }
            }
        }
    }
}

@Composable
fun ProfileBottomBar(modifier: Modifier = Modifier) {
    Surface(
        color = MaterialTheme.colorScheme.background, modifier = modifier
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(1f)
        ) {
            Text(
                text = "diKlas 1.0.0",
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.padding(vertical = 10.dp)
            )
        }
    }
}
