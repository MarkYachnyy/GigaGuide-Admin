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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.R
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
    }
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .background(color = MaterialTheme.colorScheme.background)
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(top = 20.dp)
                .fillMaxWidth()
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
        Row (modifier = Modifier.align(Alignment.CenterHorizontally)){
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = MediumBlue, contentColor = White),
                contentPadding = PaddingValues(10.dp),
                onClick = { editSightScreenViewModel.uploadSightPhoto() }) {
                Text("Выбрать изображение")
            }
            if(editSightScreenViewModel.newSightImageURI != null){
                Button(
                    modifier = Modifier.padding(start = 10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MediumBlue, contentColor = White),
                    contentPadding = PaddingValues(10.dp),
                    onClick = { editSightScreenViewModel.uploadSightPhoto() }) {
                    Text("Удалить изображение")
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
                editSightScreenViewModel.error = ""
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
                editSightScreenViewModel.error = ""
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
                editSightScreenViewModel.error = ""
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
                    editSightScreenViewModel.error = ""
                    try {
                        var lat = it.toDouble()
                        editSightScreenViewModel.newSightLatitude = lat
                    } catch (e: Exception) {
                        editSightScreenViewModel.error = "Недопустимый формат числа"
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
                    editSightScreenViewModel.error = ""
                    try {
                        var lon = it.toDouble()
                        editSightScreenViewModel.newSightLongitude = lon
                    } catch (e: Exception) {
                        editSightScreenViewModel.error = "Недопустимый формат числа"
                    }
                }
            )
        }
        Text(
            text = editSightScreenViewModel.error,
            modifier = Modifier.padding(vertical = 10.dp),
            color = Red
        )
        Button(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            colors = ButtonDefaults.buttonColors(containerColor = MediumBlue, contentColor = White),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp),
            onClick = {}) {
            Text("ЗАГРУЗИТЬ", style = MaterialTheme.typography.titleMedium)
        }
    }
}