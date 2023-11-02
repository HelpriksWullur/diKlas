package com.epic.diklas.ui.pages

import android.content.Context.MODE_PRIVATE
import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.epic.diklas.ui.theme.DiKlasTheme
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

@Composable
fun Splash(navController: NavController) {
    val context = LocalContext.current
    val sharedPref = context.getSharedPreferences("diKlasPref", MODE_PRIVATE)

    Surface(
        color = MaterialTheme.colorScheme.background
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize(1f)
        ) {
            Text(
                text = "diKlas",
                fontSize = 42.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .align(Alignment.Center)
            )
        }
    }
    LaunchedEffect(Unit) {
        delay(0.7.seconds)
        val auth = Firebase.auth
        val currentUser = auth.currentUser

        if (sharedPref.contains("isFirstRun")) {
            if (sharedPref.getBoolean("isFirstRun", false)) {
                navController.navigate("welcome")
            } else {
                if (currentUser != null) {
                    navController.navigate("loading")
                } else {
                    navController.navigate("login")
                }
            }
        } else {
            navController.navigate("welcome")
        }
    }
}

@Preview(
    name = "Gelap",
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Preview(
    name = "Terang",
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun SplashPreview() {
    val navController = rememberNavController()
    DiKlasTheme {
        Splash(navController)
    }
}