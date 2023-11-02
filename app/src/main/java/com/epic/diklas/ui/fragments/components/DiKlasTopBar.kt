package com.epic.diklas.ui.fragments.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.epic.diklas.DiKlasViewModel
import com.epic.diklas.R
import com.epic.diklas.data.Role

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiKlasTopBar(navController: NavController) {
    val viewModel: DiKlasViewModel = viewModel()
    val profileData = viewModel.userDataState.collectAsState()

    TopAppBar(
        title = {
            Text(
                text = stringResource(id = R.string.app_name),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        },
        actions = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = profileData.value.name,
                        color = MaterialTheme.colorScheme.secondary,
                        fontSize = 16.sp
                    )
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = MaterialTheme.shapes.extraSmall,
                    ) {
                        Text(
                            text = if (profileData.value.role == Role.TEACHER) "Guru" else "Siswa",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .padding(horizontal = 4.dp)
                        )
                    }
                }
                Spacer(Modifier.width(8.dp))
                Card(
                    shape = CircleShape,
                    onClick = {
                        navController.navigate("profile")
                    }
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_default_profile),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                    )
                }
            }
        }
    )
}