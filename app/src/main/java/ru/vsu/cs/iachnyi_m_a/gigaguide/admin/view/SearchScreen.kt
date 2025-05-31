package ru.vsu.cs.iachnyi_m_a.gigaguide.admin.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.model.SightTourThumbnail
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.navigation.CreateSightScreenObject
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.navigation.CreateTourScreenObject
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.navigation.EditSightScreenClass
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.navigation.EditTourScreenClass
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.ui.theme.GigaGuideAdminTheme
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.ui.theme.MediumBlue
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.ui.theme.White
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.viewmodel.SearchScreenViewModel


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SearchScreen(
    searchScreenViewModel: SearchScreenViewModel,
    navController: NavController = rememberNavController()
) {
    LaunchedEffect(Unit) {
        searchScreenViewModel.loadSearchResult()
    }
    GigaGuideAdminTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 40.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.weight(1f)) {
                    CustomTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = searchScreenViewModel.searchBarValue,
                        onValueChange = { searchScreenViewModel.searchBarValue = it },
                        hint = "Поиск по названию",
                        isPassword = false
                    )
                    Icon(
                        modifier = Modifier
                            .clickable(
                                onClick = { searchScreenViewModel.loadSearchResult() },
                                enabled = !searchScreenViewModel.loading
                            )
                            .align(alignment = Alignment.CenterEnd)
                            .padding(10.dp)
                            .size(30.dp),
                        contentDescription = null,
                        imageVector = Icons.Filled.Search,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .clickable(onClick = {
                            searchScreenViewModel.searchTours = false
                            searchScreenViewModel.loadSearchResult()
                        })
                        .weight(1f)
                        .padding(start = 5.dp, end = 5.dp, top = 10.dp)
                        .clip(CircleShape)
                        .background(color = MediumBlue)
                        .padding(10.dp)
                ) {
                    Text(
                        color = White,
                        text = "Дост-ти",
                        modifier = Modifier.align(alignment = Alignment.Center)
                    )
                }
                Box(
                    modifier = Modifier
                        .clickable(onClick = {
                            searchScreenViewModel.searchTours = true
                            searchScreenViewModel.loadSearchResult()
                        })
                        .weight(1f)
                        .padding(start = 5.dp, end = 5.dp, top = 10.dp)
                        .clip(CircleShape)
                        .background(color = MediumBlue)
                        .padding(10.dp)
                ) {
                    Text(
                        color = White,
                        text = "Туры",
                        modifier = Modifier.align(alignment = Alignment.Center)
                    )
                }
            }

            FlowRow(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.padding(vertical = 15.dp)
            ) {
                Box(
                    modifier = Modifier
                        .clickable(onClick = { navController.navigate(if (searchScreenViewModel.searchTours) CreateTourScreenObject else CreateSightScreenObject) })
                        .fillMaxWidth()
                        .padding(start = 5.dp, end = 5.dp, bottom = 10.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(color = MediumBlue)
                        .padding(10.dp)
                ) {
                    Text(
                        color = White,
                        text = if (searchScreenViewModel.searchTours) "+Тур" else "+Дост-ть",
                        modifier = Modifier.align(alignment = Alignment.Center)
                    )
                }

                if (searchScreenViewModel.searchTours) {
                    for (thumbnail in searchScreenViewModel.tourResult) {
                        SightTourSearchResult(
                            modifier = Modifier
                                .clickable(onClick = {
                                    navController.navigate(
                                        EditTourScreenClass(thumbnail.sightId)
                                    )
                                })
                                .fillMaxWidth(0.5f)
                                .padding(start = 5.dp, end = 5.dp, bottom = 10.dp),
                            sightTourThumbnail = thumbnail
                        )
                    }
                } else {
                    for (thumbnail in searchScreenViewModel.sightResult) {
                        SightTourSearchResult(
                            modifier = Modifier
                                .clickable(onClick = {
                                    navController.navigate(
                                        EditSightScreenClass(thumbnail.sightId)
                                    )
                                })
                                .fillMaxWidth(0.5f)
                                .padding(start = 5.dp, end = 5.dp, bottom = 10.dp),
                            sightTourThumbnail = thumbnail
                        )
                    }
                }


            }
        }
    }
}

@Composable
fun SightTourSearchResult(modifier: Modifier, sightTourThumbnail: SightTourThumbnail) {
    Column(modifier = modifier) {
        AsyncImage(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .fillMaxWidth()
                .aspectRatio(180 / 110f),
            contentDescription = null,
            model = sightTourThumbnail.imageLink,
            contentScale = ContentScale.Crop
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            text = sightTourThumbnail.name,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}