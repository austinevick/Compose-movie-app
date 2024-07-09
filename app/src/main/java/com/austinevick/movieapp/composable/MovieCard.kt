package com.austinevick.movieapp.composable

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.austinevick.movieapp.common.IMAGE_URL

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun MovieCard(
    modifier: Modifier = Modifier,
    imageUrl: String,
    title: String,
    releaseDate: String,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    onItemClick: () -> Unit
) {
    val style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
   with(sharedTransitionScope){
    Column {
        AsyncImage(
            model = "$IMAGE_URL${imageUrl}",
            contentDescription = null,
            modifier = modifier
                .sharedElement(
                    sharedTransitionScope.rememberSharedContentState(key = imageUrl),
                    animatedVisibilityScope = animatedContentScope
                )
                .clickable { onItemClick() }
                .padding(horizontal = 8.dp, vertical = 8.dp)
                .clip(RoundedCornerShape(8.dp)),

            )
        Text(
            text = title,
            fontSize = 12.sp,
            style = style.copy(fontWeight = FontWeight.SemiBold),
            modifier = modifier
                .sharedElement(
                    sharedTransitionScope.rememberSharedContentState(key = title),
                    animatedVisibilityScope = animatedContentScope
                )
                .padding(start = 8.dp)
        )
        Text(
            text = releaseDate.substring(0, 4),
            fontSize = 12.sp, style = style.copy(color = Color.Gray),
            modifier = modifier
                .sharedElement(
                    sharedTransitionScope.rememberSharedContentState(key = releaseDate),
                    animatedVisibilityScope = animatedContentScope
                )
                .padding(start = 8.dp)
        )
    }
}}