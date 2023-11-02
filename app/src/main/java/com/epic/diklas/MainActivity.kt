package com.epic.diklas

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.epic.diklas.data.Role
import com.epic.diklas.data.position
import com.epic.diklas.ui.fragments.components.DiKlasTopBar
import com.epic.diklas.ui.fragments.components.NewBottomApp
import com.epic.diklas.ui.fragments.contents.StudentAttendance
import com.epic.diklas.ui.fragments.contents.StudentHistory
import com.epic.diklas.ui.fragments.contents.StudentTask
import com.epic.diklas.ui.fragments.contents.TeacherAttendance
import com.epic.diklas.ui.fragments.contents.TeacherHistory
import com.epic.diklas.ui.fragments.contents.TeacherTask
import com.epic.diklas.ui.pages.Loading
import com.epic.diklas.ui.pages.Login
import com.epic.diklas.ui.pages.Profile
import com.epic.diklas.ui.pages.QRScanner
import com.epic.diklas.ui.pages.Splash
import com.epic.diklas.ui.pages.Welcome
import com.epic.diklas.ui.subpages.AttendanceDetail
import com.epic.diklas.ui.subpages.ManualAttendance
import com.epic.diklas.ui.subpages.TaskAdd
import com.epic.diklas.ui.subpages.TaskDesk
import com.epic.diklas.ui.theme.DiKlasTheme
import kotlinx.coroutines.delay
import kotlin.system.exitProcess
import kotlin.time.Duration.Companion.seconds

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DiKlasTheme {
                DiKlasApp()
            }
        }
    }
}

@Composable
fun DiKlasApp(context: Context = LocalContext.current) {
    var backCurrentlyPressed by remember { mutableStateOf(false) }
    // mengontrol tombol back(kembali), ketika tombol back ditekan fungsi akan mengubah nilai backCurrentlyPressed menjadi true
    // jika sebelumnya bernilai false, dan akan mengakhiri program jika sebelumnya bernilai sudah true
    BackHandler {
        if (backCurrentlyPressed) {
            exitProcess(0)
        } else {
            Toast.makeText(context, "Tekan lagi untuk keluar", Toast.LENGTH_SHORT).show()
            backCurrentlyPressed = true
        }
    }

    // dalam 2 detik nilai backCurrentlyPressed akan kembali menjadi false, jika terdeteksi sebelumnya bernilai true
    if (backCurrentlyPressed) {
        LaunchedEffect(Unit) {
            delay(2.seconds)
            backCurrentlyPressed = false
        }
    }

    val navControllerMain = rememberNavController()

    DiKlasTheme {
        NavHost(
            navController = navControllerMain,
            startDestination = "splash",
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {
            composable("splash") {
                Splash(navControllerMain)
            }
            composable("loading") {
                Loading(navControllerMain)
            }
            composable("welcome") {
                Welcome(navControllerMain)
            }
            composable("login") {
                Login(navControllerMain)
            }
            composable("main_page") {
                MainPage(navControllerMain)
            }
            composable("profile") {
                Profile(navControllerMain)
            }
            composable("scanner") {
                QRScanner(navControllerMain)
            }
            composable(
                route = "attendance_detail/{code}",
                arguments = listOf(
                    navArgument("code") {
                        type = NavType.StringType
                    }
                )
            ) {
                val code = it.arguments?.getString("code")
                AttendanceDetail(navControllerMain, code = code.toString())
            }
            composable(
                route = "task_desk/{code}",
                arguments = listOf(
                    navArgument("code") {
                        type = NavType.StringType
                    }
                )
            ) {
                val code = it.arguments?.getString("code")
                TaskDesk(navControllerMain = navControllerMain, code = code.toString())
            }
            composable(
                route = "task_add",
            ) {
                TaskAdd(navControllerMain)
            }
            composable("manual_attendance") {
                ManualAttendance(navControllerMain)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainPage(navControllerMain: NavController) {
    val navController = rememberNavController()
    val viewModel: DiKlasViewModel = viewModel()

    val userData = viewModel.userDataState.collectAsState()

    val user = userData.value

    Scaffold(
        topBar = { DiKlasTopBar(navControllerMain) },
        bottomBar = { NewBottomApp(navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = position.ifEmpty { "attendance" },
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {
            composable(
                route = "attendance"
            ) {
                when (user.role) {
                    Role.STUDENT -> StudentAttendance(Modifier.padding(innerPadding), navControllerMain)
                    Role.TEACHER -> TeacherAttendance(Modifier.padding(innerPadding), navControllerMain)
                    else -> {}
                }
                position = navController.currentDestination?.route.toString()
            }
            composable(
                route = "history"
            ) {
                when (user.role) {
                    Role.STUDENT -> StudentHistory(Modifier.padding(innerPadding), navControllerMain)
                    Role.TEACHER -> TeacherHistory(Modifier.padding(innerPadding), navControllerMain)
                    else -> {}
                }
                position = navController.currentDestination?.route.toString()
            }
            composable(
                route = "task"
            ) {
                when (user.role) {
                    Role.STUDENT -> StudentTask(Modifier.padding(innerPadding), navControllerMain)
                    Role.TEACHER -> TeacherTask(Modifier.padding(innerPadding), navControllerMain)
                    else -> {}
                }
                position = navController.currentDestination?.route.toString()
            }
        }
    }
}
