package com.epic.diklas.ui.pages

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import com.epic.diklas.data.userActive
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Login(navController: NavController) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val context = LocalContext.current

    val auth: FirebaseAuth = Firebase.auth
    val database = Firebase.database.reference

    val userLiveData = remember {
        MutableLiveData<String>()
    }

    val userState = userLiveData.observeAsState()

    DisposableEffect(username) {
        val idListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val id = snapshot.child("data_user").child(username).child("email").value.toString()
                userLiveData.value = id
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        }

        database.addValueEventListener(idListener)

        onDispose {
            database.removeEventListener(idListener)
        }
    }

    Surface(
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize(1f)
        ) {
            Text(
                text = "masuk",
            )
            Text(
                text = "diKlas",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(bottom = 14.dp)
            )
            TextField(
                value = username,
                singleLine = true,
                onValueChange = {
                    username = it
                },
                placeholder = {
                    Text(text = "Username")
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null
                    )
                },
                shape = MaterialTheme.shapes.small,
                colors = TextFieldDefaults.textFieldColors(
                    disabledIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                modifier = Modifier
                    .padding(bottom = 10.dp)
            )
            TextField(
                value = password,
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(
                    mask = '*'
                ),
                onValueChange = {
                    password = it
                },
                placeholder = {
                    Text(text = "Password")
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = null
                    )
                },
                shape = MaterialTheme.shapes.small,
                colors = TextFieldDefaults.textFieldColors(
                    disabledIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                modifier = Modifier
                    .padding(bottom = 10.dp)
            )
            Button(
                onClick = {
                    if (username.isNotEmpty() or password.isNotEmpty()) {
                        auth.signInWithEmailAndPassword(userState.value!!, password)
                            .addOnCompleteListener { task ->
                                if (task.isComplete) {
                                    if (task.isSuccessful) {
                                        userActive = username
                                        navController.navigate("main_page")
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "Username atau password salah!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }
                    } else {
                        Toast.makeText(
                            context,
                            "Username atau password belum dimasukan!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                shape = MaterialTheme.shapes.medium,
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp)
            ) {
                Text(
                    text = "Login",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(top = 2.dp, bottom = 6.dp)
                )
            }
        }
    }
}

//@Preview(
//    name = "Terang",
//    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
//)
//@Preview(
//    name = "Gelap",
//    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
//)
//@Composable
//fun LoginPreview() {
//    DiKlasTheme {
//        Login()
//    }
//}