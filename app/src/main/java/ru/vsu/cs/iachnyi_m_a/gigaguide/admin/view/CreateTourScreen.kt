package ru.vsu.cs.iachnyi_m_a.gigaguide.admin.view

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.model.SightTourThumbnail
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.ui.theme.MediumBlue
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.ui.theme.MediumGrey
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.ui.theme.Red
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.ui.theme.White
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.viewmodel.CreateTourScreenViewModel

@Composable
fun CreateTourScreen(
    navController: NavController,
    createTourScreenViewModel: CreateTourScreenViewModel
) {
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
                text = "Создание дост-ти",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        if (createTourScreenViewModel.tourImageURI == null) {
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
                model = createTourScreenViewModel.tourImageURI,
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
        }
        Button(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            colors = ButtonDefaults.buttonColors(containerColor = MediumBlue, contentColor = White),
            contentPadding = PaddingValues(10.dp),
            onClick = { createTourScreenViewModel.uploadPhoto() }) {
            Text("Выбрать изображение")
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
            value = createTourScreenViewModel.tourName,
            onValueChange = {
                createTourScreenViewModel.tourName = it
                createTourScreenViewModel.error = ""
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
            value = createTourScreenViewModel.tourDescription,
            onValueChange = {
                createTourScreenViewModel.tourDescription = it
                createTourScreenViewModel.error = ""
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
            hint = "Категория (например, исторический)",
            value = createTourScreenViewModel.tourCategory,
            onValueChange = {
                createTourScreenViewModel.tourCategory = it
                createTourScreenViewModel.error = ""
            }
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
            hint = "Тип (пеший, автомобильный и т.п.)",
            value = createTourScreenViewModel.tourType,
            onValueChange = {
                createTourScreenViewModel.tourType = it
                createTourScreenViewModel.error = ""
            }
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            text = "Достопримечательности",
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleLarge
        )
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            CustomTextField(
                style = MaterialTheme.typography.bodyLarge,
                hint = "Поиск по названию",
                onValueChange = { createTourScreenViewModel.searchQuery = it },
                value = createTourScreenViewModel.searchQuery
            )
            RoundedCornerSquareButton(
                modifier = Modifier
                    .padding(start = 10.dp)
                    .size(40.dp),
                imageVector = Icons.Default.Search,
                onClick = { createTourScreenViewModel.searchSights() })
        }
        Column(modifier = Modifier.fillMaxWidth()) {
            for (i in 0..createTourScreenViewModel.searchResult.size - 1) {
                var thumb = createTourScreenViewModel.searchResult[i]
                Row(
                    modifier = Modifier
                        .padding(top = 5.dp)
                        .clip(RoundedCornerShape(5.dp))
                        .background(
                            MaterialTheme.colorScheme.tertiary
                        )
                        .padding(5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(thumb.name)
                    RoundedCornerSquareButton(
                        modifier = Modifier.size(30.dp),
                        imageVector = Icons.Default.Add,
                        onClick = {
                            if (!createTourScreenViewModel.chosenSights.contains(thumb)) createTourScreenViewModel.chosenSights.add(
                                thumb
                            )
                        })
                }
            }
        }
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            text = "Выбранные",
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleSmall
        )
        Column(modifier = Modifier.fillMaxWidth()) {
            for (i in 0..createTourScreenViewModel.chosenSights.size - 1) {
                var thumb = createTourScreenViewModel.chosenSights[i]
                SightBox(
                    modifier = Modifier
                        .padding(bottom = 5.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .padding(5.dp),
                    thumb = thumb,
                    deleteCompletelyCallback = { createTourScreenViewModel.chosenSights.removeAt(i) },
                    moveForwardCallback = if (i == createTourScreenViewModel.chosenSights.size - 1) null else {
                        {
                            var temp = createTourScreenViewModel.chosenSights[i]
                            createTourScreenViewModel.chosenSights[i] =
                                createTourScreenViewModel.chosenSights[i + 1]
                            createTourScreenViewModel.chosenSights[i + 1] = temp
                        }
                    },
                    moveBackwardCallback = if (i == 0) null else {
                        {
                            var temp = createTourScreenViewModel.chosenSights[i]
                            createTourScreenViewModel.chosenSights[i] =
                                createTourScreenViewModel.chosenSights[i - 1]
                            createTourScreenViewModel.chosenSights[i - 1] = temp
                        }
                    }
                )
            }
        }
        Text(
            text = createTourScreenViewModel.error,
            modifier = Modifier.padding(vertical = 10.dp),
            color = Red
        )
        Button(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            colors = ButtonDefaults.buttonColors(containerColor = MediumBlue, contentColor = White),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp),
            onClick = { createTourScreenViewModel.createTour { navController.popBackStack() } }) {
            Text("ЗАГРУЗИТЬ", style = MaterialTheme.typography.titleMedium)
        }
    }
}

@Composable
fun SightBox(
    thumb: SightTourThumbnail,
    deleteCompletelyCallback: () -> Unit,
    moveForwardCallback: (() -> Unit)?,
    moveBackwardCallback: (() -> Unit)?,
    modifier: Modifier,
) {
    Row(modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {

        AsyncImage(
            model = thumb.imageLink,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(color = MediumGrey)
                .weight(2f)
                .aspectRatio(1f),
            contentDescription = null
        )

        Text(modifier = Modifier.weight(6f).padding(start = 5.dp), style = MaterialTheme.typography.titleMedium, text = thumb.name)

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
                    deleteCompletelyCallback.invoke()
                },
                imageVector = Icons.Default.Delete
            )
        }
    }
}