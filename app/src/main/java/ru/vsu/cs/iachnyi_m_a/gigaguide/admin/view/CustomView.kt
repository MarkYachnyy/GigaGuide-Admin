package ru.vsu.cs.iachnyi_m_a.gigaguide.admin.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp


@Composable
fun RoundedCornerSquareButton(
    modifier: Modifier,
    onClick: () -> Unit,
    imageVector: ImageVector,
    contentColor: Color = MaterialTheme.colorScheme.secondary,
    containerColor: Color = MaterialTheme.colorScheme.primary
) {
    Button(
        onClick = onClick,
        contentPadding = PaddingValues(6.dp),
        modifier = modifier
            .aspectRatio(1f),
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = "search",
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun GradientSeparator(modifier: Modifier) {
    Spacer(
        modifier = modifier
            .height(1.dp)
            .background(
                brush = Brush.horizontalGradient(
                    colorStops = arrayOf(
                        0.0f to MaterialTheme.colorScheme.onBackground.copy(0f),
                        0.5f to MaterialTheme.colorScheme.onBackground,
                        1f to MaterialTheme.colorScheme.onBackground.copy(0f)
                    )
                )
            )
    )
}