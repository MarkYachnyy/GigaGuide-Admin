package ru.vsu.cs.iachnyi_m_a.gigaguide.admin.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.R
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.navigation.TourReviewScreenClass
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.ui.theme.MediumBlue
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.ui.theme.MediumGrey
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.ui.theme.Red
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.ui.theme.White
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.viewmodel.EditTourScreenViewModel

@Composable
fun EditTourScreen(tourId: Int,
                   editTourScreenViewModel: EditTourScreenViewModel,
                   navController: NavController) {
    LaunchedEffect(Unit) {
        editTourScreenViewModel.loadExistingTour(tourId)
    }
    var deleteDialogOpen by remember { mutableStateOf(false) }

    when {
        deleteDialogOpen -> {
            Dialog(onDismissRequest = { deleteDialogOpen = false }) {
                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(color = MaterialTheme.colorScheme.background)
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        modifier = Modifier.padding(bottom = 20.dp),
                        text = "Удалить тур?",
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.titleLarge
                    )
                    Row {
                        Button(
                            modifier = Modifier.padding(end = 20.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MediumBlue,
                                contentColor = White
                            ),
                            contentPadding = PaddingValues(10.dp),
                            onClick = { deleteDialogOpen = false }) {
                            Text("Отмена")
                        }
                        Button(
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MediumBlue,
                                contentColor = White
                            ),
                            contentPadding = PaddingValues(10.dp),
                            onClick = {
                                editTourScreenViewModel.deleteTour { navController.popBackStack() }
                                deleteDialogOpen = false
                            }) {
                            Text("УДАЛИТЬ")
                        }
                    }
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .background(color = MaterialTheme.colorScheme.background)
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(top = 20.dp)
                .fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                RoundedCornerSquareButton(
                    modifier = Modifier.size(40.dp),
                    imageVector = ImageVector.vectorResource(R.drawable.chevron_left),
                    onClick = { navController.popBackStack() })
                Text(
                    modifier = Modifier.padding(start = 10.dp),
                    text = "Обновление тура",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            Row {
                RoundedCornerSquareButton(
                    modifier = Modifier.padding(end = 5.dp).size(40.dp),
                    imageVector = ImageVector.vectorResource(R.drawable.comment),
                    onClick = { navController.navigate(TourReviewScreenClass(tourId.toLong())) })
                RoundedCornerSquareButton(
                    modifier = Modifier.size(40.dp),
                    imageVector = Icons.Filled.Delete,
                    contentColor = Red,
                    onClick = { deleteDialogOpen = true })
            }
        }

        if (editTourScreenViewModel.tour == null) {
            Text(
                "Нет изображения",
                color = MediumGrey,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(vertical = 10.dp)
            )
        } else {
            AsyncImage(
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .aspectRatio(1.5f),
                model = if (editTourScreenViewModel.newTourImageURI == null) editTourScreenViewModel.tour!!.imageLink else editTourScreenViewModel.newTourImageURI,
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
        }
        Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = MediumBlue,
                    contentColor = White
                ),
                contentPadding = PaddingValues(10.dp),
                onClick = { editTourScreenViewModel.uploadTourPhoto() }) {
                Text("Выбрать изобр-е")
            }
            if (editTourScreenViewModel.newTourImageURI != null) {
                Button(
                    modifier = Modifier.padding(start = 10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MediumBlue,
                        contentColor = White
                    ),
                    contentPadding = PaddingValues(10.dp),
                    onClick = { editTourScreenViewModel.newTourImageURI = null }) {
                    Text("Удалить изобр-е")
                }
            }
        }

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            text = "Название",
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleLarge

        )
        CustomTextField(
            hint = "Название",
            value = editTourScreenViewModel.newTourName,
            onValueChange = {
                editTourScreenViewModel.newTourName = it
                editTourScreenViewModel.updateTourError = ""
            })
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            text = "Описание",
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleLarge
        )
        CustomTextField(
            hint = "Описание",
            value = editTourScreenViewModel.newTourDescription,
            onValueChange = {
                editTourScreenViewModel.newTourDescription = it
                editTourScreenViewModel.updateTourError = ""
            },
            multiLine = true
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            text = "Категория",
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleLarge
        )
        CustomTextField(
            hint = "Категория",
            value = editTourScreenViewModel.newTourCategory,
            onValueChange = {
                editTourScreenViewModel.newTourCategory = it
                editTourScreenViewModel.updateTourError = ""
            },
            multiLine = true
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            text = "Тип",
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleLarge
        )
        CustomTextField(
            hint = "Тип",
            value = editTourScreenViewModel.newTourType,
            onValueChange = {
                editTourScreenViewModel.newTourType = it
                editTourScreenViewModel.updateTourError = ""
            },
            multiLine = true
        )
        Text(
            text = editTourScreenViewModel.updateTourError,
            modifier = Modifier.padding(vertical = 10.dp),
            color = Red
        )
        Button(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            colors = ButtonDefaults.buttonColors(containerColor = MediumBlue, contentColor = White),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp),
            onClick = {
                editTourScreenViewModel.updateTour()
            }) {
            Text("ОБНОВИТЬ ТУР", style = MaterialTheme.typography.titleMedium)
        }
    }
}