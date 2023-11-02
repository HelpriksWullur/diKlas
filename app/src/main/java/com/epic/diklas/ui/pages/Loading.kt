package com.epic.diklas.ui.pages

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.epic.diklas.R
import com.epic.diklas.data.userActive
import com.epic.diklas.ui.theme.DiKlasTheme
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

@Composable
fun Loading(navController: NavController) {
    val database = Firebase.database.reference

    val auth = Firebase.auth
    val currentUser = auth.currentUser

    DisposableEffect(Unit) {
        val dbListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.child("data_user").children.forEach {
                    if (it.child("email").value.toString() == currentUser?.email) {
                        userActive = it.key.toString()
                        navController.navigate("main_page")
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        }

        database.addValueEventListener(dbListener)

        onDispose {
            database.removeEventListener(dbListener)
        }
    }
    DiKlasTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize(1f)
            ) {
                val rotationController = rememberInfiniteTransition(label = "")
                val rotation by rotationController.animateFloat(
                    initialValue = 0f,
                    targetValue = 360f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(durationMillis = 1000, easing = LinearEasing),
                        repeatMode = RepeatMode.Restart
                    ), label = ""
                )
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .align(Alignment.Center)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_loading),
                        contentDescription = null,
                        modifier = Modifier
                            .size(42.dp)
                            .rotate(rotation)
                    )
                    Text(
                        text = "Memuat otomatis...",
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        }
    }
}