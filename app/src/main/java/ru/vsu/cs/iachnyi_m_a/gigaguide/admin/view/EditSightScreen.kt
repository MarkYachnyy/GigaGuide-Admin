package ru.vsu.cs.iachnyi_m_a.gigaguide.admin.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
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
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.model.EditMoment
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.ui.theme.MediumBlue
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.ui.theme.MediumGrey
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.ui.theme.Red
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.ui.theme.White
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.viewmodel.EditSightScreenViewModel

@Composable
fun EditSightScreen(
    sightId: Int,
    editSightScreenViewModel: EditSightScreenViewModel,
    navController: NavController
) {
    LaunchedEffect(Unit) {
        editSightScreenViewModel.loadExistingSight(sightId)
        editSightScreenViewModel.loadExistingMoments(sightId)
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
                        text = "Удалить достопримечательность?",
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
                                editSightScreenViewModel.deleteSight { navController.popBackStack() }
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
                    text = "Обновление дост-ти",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            RoundedCornerSquareButton(
                modifier = Modifier.size(40.dp),
                imageVector = Icons.Filled.Delete,
                contentColor = Red,
                onClick = { deleteDialogOpen = true })
        }

        if (editSightScreenViewModel.sight == null) {
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
                model = if (editSightScreenViewModel.newSightImageURI == null) editSightScreenViewModel.sight!!.imageLink else editSightScreenViewModel.newSightImageURI,
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
                onClick = { editSightScreenViewModel.uploadSightPhoto() }) {
                Text("Выбрать изобр-е")
            }
            if (editSightScreenViewModel.newSightImageURI != null) {
                Button(
                    modifier = Modifier.padding(start = 10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MediumBlue,
                        contentColor = White
                    ),
                    contentPadding = PaddingValues(10.dp),
                    onClick = { editSightScreenViewModel.newSightImageURI = null }) {
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
            value = editSightScreenViewModel.newSightName,
            onValueChange = {
                editSightScreenViewModel.newSightName = it
                editSightScreenViewModel.updateSightError = ""
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
            value = editSightScreenViewModel.newSightDescription,
            onValueChange = {
                editSightScreenViewModel.newSightDescription = it
                editSightScreenViewModel.updateSightError = ""
            },
            multiLine = true
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            text = "Город",
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleLarge
        )
        CustomTextField(
            hint = "Город",
            value = editSightScreenViewModel.newSightCity,
            onValueChange = {
                editSightScreenViewModel.newSightCity = it
                editSightScreenViewModel.updateSightError = ""
            }
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            text = "Координаты",
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleLarge
        )
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            CustomTextField(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 5.dp),
                hint = "Широта",
                value = editSightScreenViewModel.newSightLatitude.toString(),
                onValueChange = {
                    editSightScreenViewModel.updateSightError = ""
                    try {
                        var lat = it.toDouble()
                        editSightScreenViewModel.newSightLatitude = lat
                    } catch (e: Exception) {
                        editSightScreenViewModel.updateSightError = "Недопустимый формат числа"
                    }
                }
            )
            CustomTextField(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 5.dp),
                hint = "Долгота",
                value = editSightScreenViewModel.newSightLongitude.toString(),
                onValueChange = {
                    editSightScreenViewModel.updateSightError = ""
                    try {
                        var lon = it.toDouble()
                        editSightScreenViewModel.newSightLongitude = lon
                    } catch (e: Exception) {
                        editSightScreenViewModel.updateSightError = "Недопустимый формат числа"
                    }
                }
            )
        }
        Text(
            text = editSightScreenViewModel.updateSightError,
            modifier = Modifier.padding(vertical = 10.dp),
            color = Red
        )
        Button(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            colors = ButtonDefaults.buttonColors(containerColor = MediumBlue, contentColor = White),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp),
            onClick = {
                editSightScreenViewModel.updateSight()
            }) {
            Text("ОБНОВИТЬ ДОС-ТЬ", style = MaterialTheme.typography.titleMedium)
        }
        Text(
            text = "Моменты",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp)
        ) {
            for (i in 0..editSightScreenViewModel.editMoments.size - 1) {
                var editMoment = editSightScreenViewModel.editMoments[i]
                EditMomentBox(
                    modifier = Modifier
                        .padding(bottom = 5.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(color = MaterialTheme.colorScheme.tertiary)
                        .fillMaxWidth()
                        .padding(5.dp),
                    editMoment = editMoment,
                    chooseImageCallback = {
                        editSightScreenViewModel.uploadMomentPhoto(i)
                    },
                    moveForwardCallback = if (i == editSightScreenViewModel.editMoments.size - 1) null else {
                        {
                            var temp = editSightScreenViewModel.editMoments[i]
                            editSightScreenViewModel.editMoments[i] =
                                editSightScreenViewModel.editMoments[i + 1]
                            editSightScreenViewModel.editMoments[i + 1] = temp
                        }
                    },
                    moveBackwardCallback = if (i == 0) null else {
                        {
                            var temp = editSightScreenViewModel.editMoments[i]
                            editSightScreenViewModel.editMoments[i] =
                                editSightScreenViewModel.editMoments[i - 1]
                            editSightScreenViewModel.editMoments[i - 1] = temp
                        }
                    },
                    deleteCompletelyCallback = {
                        editSightScreenViewModel.editMoments.removeAt(i)
                    }
                )
            }
        }

        Text(
            text = editSightScreenViewModel.updateMomentsError,
            color = Red
        )

        Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = MediumBlue,
                    contentColor = White
                ),
                contentPadding = PaddingValues(10.dp),
                onClick = { editSightScreenViewModel.addEmptyMoment() }) {
                Text("+Момент")
            }
            Button(
                modifier = Modifier.padding(start = 10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MediumBlue,
                    contentColor = White
                ),
                contentPadding = PaddingValues(10.dp),
                onClick = { editSightScreenViewModel.updateMoments() }) {
                Text("Обновить моменты")
            }

        }
    }
}

@Composable
fun EditMomentBox(
    moveForwardCallback: (() -> Unit)?,
    moveBackwardCallback: (() -> Unit)?,
    modifier: Modifier,
    editMoment: EditMoment,
    chooseImageCallback: () -> Unit,
    deleteCompletelyCallback: () -> Unit
) {
    if (editMoment.willDelete) {
        Column(
            modifier = modifier
                .clip(RoundedCornerShape(10.dp))
                .background(color = MaterialTheme.colorScheme.tertiary)
                .fillMaxWidth()
                .padding(vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Данный момент будет удалён", modifier = Modifier.padding(bottom = 5.dp))
            Text(
                text = "Отменить",
                color = MediumBlue,
                modifier = Modifier.clickable(onClick = { editMoment.willDelete = false })
            )
        }
    } else {

        Column(
            modifier = modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier
                        .weight(2f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (editMoment.existingMoment == null && editMoment.newMomentImageURI == null) {
                        Spacer(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(color = MediumGrey)
                                .fillMaxWidth()
                                .aspectRatio(1f)
                        )
                    } else {
                        AsyncImage(
                            model = if (editMoment.newMomentImageURI == null) editMoment.existingMoment!!.imagePath else editMoment.newMomentImageURI,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(color = MediumGrey)
                                .fillMaxWidth()
                                .aspectRatio(1f),
                            contentDescription = null
                        )
                    }
                    Row(modifier = Modifier.padding(top = 5.dp)) {
                        RoundedCornerSquareButton(
                            modifier = Modifier.size(30.dp),
                            imageVector = Icons.Filled.Add,
                            onClick = { chooseImageCallback.invoke() })
                        if (editMoment.newMomentImageURI != null) {
                            RoundedCornerSquareButton(
                                modifier = Modifier
                                    .padding(start = 10.dp)
                                    .size(30.dp),
                                contentColor = Red,
                                imageVector = Icons.Filled.Delete,
                                onClick = { editMoment.newMomentImageURI = null })
                        }
                    }
                }
                Column(
                    modifier = Modifier
                        .weight(6f)
                        .padding(horizontal = 5.dp)
                ) {
                    CustomTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = editMoment.newMomentName,
                        onValueChange = { editMoment.newMomentName = it },
                        hint = "Имя момента",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 5.dp)
                    ) {
                        CustomTextField(
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 2.dp),
                            value = editMoment.newMomentLatitude.toString(),
                            hint = "Широта",
                            onValueChange = {
                                try {
                                    var lat = it.toDouble()
                                    editMoment.newMomentLatitude = lat
                                } catch (e: Exception) {
                                }
                            },
                            style = MaterialTheme.typography.bodySmall
                        )
                        CustomTextField(
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 2.dp),
                            value = editMoment.newMomentLongitude.toString(),
                            hint = "Долгота",
                            onValueChange = {
                                try {
                                    var lon = it.toDouble()
                                    editMoment.newMomentLongitude = lon
                                } catch (e: Exception) {
                                }
                            },
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
                Column(modifier = Modifier.weight(1f)) {
                    if (moveBackwardCallback != null) {
                        RoundedCornerSquareButton(
                            modifier = Modifier
                                .padding(bottom = 5.dp)
                                .fillMaxWidth(),
                            onClick = moveBackwardCallback,
                            imageVector = Icons.Default.KeyboardArrowUp
                        )
                    }
                    if (moveForwardCallback != null) {
                        RoundedCornerSquareButton(
                            modifier = Modifier
                                .padding(bottom = 5.dp)
                                .fillMaxWidth(),
                            onClick = moveForwardCallback,
                            imageVector = Icons.Default.KeyboardArrowDown
                        )
                    }
                    RoundedCornerSquareButton(
                        modifier = Modifier
                            .fillMaxWidth(),
                        contentColor = Red,
                        onClick = {
                            if (editMoment.existingMoment != null) {
                                editMoment.willDelete = true
                            } else {
                                deleteCompletelyCallback.invoke()
                            }
                        },
                        imageVector = Icons.Default.Delete
                    )
                }
            }
            var isDescOpen by remember { mutableStateOf(false) }
            Button(
                modifier = Modifier.padding(top = 5.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MediumBlue,
                    contentColor = White
                ),
                contentPadding = PaddingValues(10.dp),
                onClick = { isDescOpen = !isDescOpen }) {
                Text(if (!isDescOpen) "Раскрыть гид" else "Скрыть гид")
            }
            AnimatedVisibility(visible = isDescOpen, modifier = Modifier.fillMaxWidth()) {
                CustomTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 5.dp),
                    style = MaterialTheme.typography.bodySmall,
                    hint = "Текст аудиогида",
                    value = editMoment.newMomentContent,
                    onValueChange = { editMoment.newMomentContent = it }
                )
            }
        }
    }
}