package ru.vsu.cs.iachnyi_m_a.gigaguide.admin.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.R
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.ui.theme.Black
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.ui.theme.GigaGuideAdminTheme
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.ui.theme.LightGrey
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.ui.theme.MediumGrey
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.ui.theme.Red
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.ui.theme.Yellow
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.viewmodel.ReviewScreenViewModel
import ru.vsu.cs.iachnyi_m_a.gigaguide.mobile.model.review.Review
import java.time.format.DateTimeFormatter

@Composable
fun ReviewScreen(
    navController: NavController = rememberNavController(),
    objectId: Long,
    isTour: Boolean,
    reviewScreenViewModel: ReviewScreenViewModel
) {
    LaunchedEffect(Unit) {
        reviewScreenViewModel.isTour = isTour
        reviewScreenViewModel.objectId = objectId.toInt()
        reviewScreenViewModel.loadReviews()
    }

    Column(
        modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = { navController.popBackStack() },
                contentPadding = PaddingValues(6.dp),
                modifier = Modifier
                    .defaultMinSize(minWidth = 1.dp, minHeight = 1.dp)
                    .height(50.dp)
                    .aspectRatio(1f),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.secondary
                ),
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.chevron_left),
                    contentDescription = "chevron_left",
                    modifier = Modifier.fillMaxSize()
                )
            }
            Text(
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.headlineSmall,
                text = "Отзывы",
                modifier = Modifier.padding(start = 20.dp)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            if (reviewScreenViewModel.otherReviews.isEmpty()) {
                Text(
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.titleLarge,
                    text = "Нет отзывов"
                )
            } else {
                for (review in reviewScreenViewModel.otherReviews) {
                    MyReviewBox(
                        review = review,
                        deleteCallback = { reviewScreenViewModel.deleteReview(review.id) })
                    GradientSeparator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 35.dp)
                    )
                }
            }
        }

    }
}

@Composable
fun MyReviewBox(
    review: Review, deleteCallback: () -> Unit
) {
    var optionsOpen by remember { mutableStateOf(false) }
    GigaGuideAdminTheme {
        Box(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .background(color = MaterialTheme.colorScheme.tertiary)
                    .fillMaxWidth()
                    .padding(15.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {

                        Icon(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(color = LightGrey)
                                .padding(5.dp),
                            imageVector = ImageVector.vectorResource(R.drawable.person_outline),
                            tint = Black,
                            contentDescription = "user icon"
                        )

                        Text(
                            modifier = Modifier.padding(start = 10.dp),
                            text = review.userName,
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = review.rating.toString(),
                            style = MaterialTheme.typography.titleLarge
                        )
                        Icon(
                            modifier = Modifier.size(35.dp),
                            contentDescription = null,
                            imageVector = Icons.Filled.Star,
                            tint = Yellow
                        )
                    }
                }
                Text(
                    modifier = Modifier.padding(vertical = 10.dp),
                    text = review.text,
                    style = MaterialTheme.typography.bodyMedium
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        color = MediumGrey,
                        style = MaterialTheme.typography.bodyMedium,
                        text = review.date.format(DateTimeFormatter.ofPattern("d MMMM yyyy"))
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            color = MediumGrey,
                            text = "Изменить отзыв",
                            modifier = Modifier.padding(horizontal = 7.dp),
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.three_dots_vertical),
                            modifier = Modifier
                                .clickable(onClick = { optionsOpen = true })
                                .size(25.dp),
                            tint = Black,
                            contentDescription = null
                        )
                    }
                }
            }
            AnimatedVisibility(
                visible = optionsOpen, modifier = Modifier.align(Alignment.BottomEnd)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
                        .clip(
                            RoundedCornerShape(
                                topStart = 10.dp, topEnd = 10.dp, bottomStart = 10.dp
                            )
                        )
                        .background(
                            MaterialTheme.colorScheme.background
                        )
                ) {
                    Text(
                        modifier = Modifier
                            .clickable(onClick = deleteCallback)
                            .padding(15.dp),
                        text = "Удалить",
                        color = Red,
                        style = MaterialTheme.typography.titleMedium
                    )
                    GradientSeparator(
                        modifier = Modifier
                            .padding(horizontal = 15.dp)
                            .width(100.dp)
                    )
                    Text(
                        modifier = Modifier
                            .clickable(onClick = { optionsOpen = false })
                            .padding(15.dp),
                        text = "Отменить",
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }


        }

    }
}