package com.epic.diklas.ui.pages

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.epic.diklas.DiKlasViewModel
import com.epic.diklas.data.model.TeacherViewModel
import com.epic.diklas.data.userActive
import com.journeyapps.barcodescanner.CaptureManager
import com.journeyapps.barcodescanner.CompoundBarcodeView

@Composable
fun QRScanner(navController: NavController) {
    val viewModel: DiKlasViewModel = viewModel()
    val scheduleData = viewModel.scheduleDataState.collectAsState()

    val teacherVM: TeacherViewModel = viewModel()

    val context = LocalContext.current

    val compoundBarcodeView = remember {
        CompoundBarcodeView(context).apply {
            val capture = CaptureManager(context as Activity, this)
            capture.initializeFromIntent(context.intent, null)
            this.setStatusText("Silahkan pindai kode QR kelas")
            this.resume()
            capture.decode()
            this.decodeSingle { result ->
                val schedule = scheduleData.value
                result.text?.let {
                    if (it == schedule.code) {
                        Toast.makeText(context, "Kode valid", Toast.LENGTH_SHORT).show()
                        navController.navigate("main_page")
                        teacherVM.changeStatus(userActive, schedule.meet, 1)
                    } else {
                        Toast.makeText(context, "Kode tidak valid", Toast.LENGTH_SHORT).show()
                        navController.navigate("main_page")
                    }
                }
            }
        }
    }

    AndroidView(
        modifier = Modifier.fillMaxSize(1f),
        factory = { compoundBarcodeView })
}
