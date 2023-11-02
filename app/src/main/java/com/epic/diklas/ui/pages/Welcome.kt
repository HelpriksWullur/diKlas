package com.epic.diklas.ui.pages

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.epic.diklas.ui.theme.DiKlasTheme

@Composable
fun Welcome(navController: NavController) {
    val context = LocalContext.current
    val sharedPref = context.getSharedPreferences("diKlasPref", MODE_PRIVATE)
    val editor = sharedPref.edit()

    Surface(
        color = MaterialTheme.colorScheme.background
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(1f)
        ) {
            Row(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(4.dp)
            ) {
                Text(
                    text = "diKlas",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = "1.0.0"
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 12.dp)
                    .padding(bottom = 6.dp)
            ) {
                Text(
                    text = "Selamat Datang",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "bla bla bla bla bla bla bla"
                )
                Button(
                    onClick = { startApp(editor, navController) },
                    modifier = Modifier
                        .align(Alignment.End)
                ) {
                    Text(
                        text = "MULAI",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

fun startApp(editor: SharedPreferences.Editor, navController: NavController) {
    editor.putBoolean("isFirstRun", false).apply()
    navController.navigate("login")
}

@Preview(name = "Gelap",
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Preview(name = "Terang",
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun WelcomePreview() {
    val navController = rememberNavController()
    DiKlasTheme {
        Welcome(navController)
    }
}