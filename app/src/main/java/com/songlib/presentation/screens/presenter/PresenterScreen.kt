package com.songlib.presentation.screens.presenter

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.songlib.data.models.Song
import com.songlib.data.sample.*
import com.songlib.domain.entities.UiState
import com.songlib.presentation.components.*
import com.songlib.presentation.components.action.AppTopBar
import com.songlib.presentation.screens.presenter.components.*
import com.songlib.presentation.theme.ThemeColors
import com.songlib.presentation.viewmodels.PresenterViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PresenterScreen(
    viewModel: PresenterViewModel,
    navController: NavHostController,
    onBackPressed: () -> Unit,
    song: Song?,
) {
    LaunchedEffect(song) {
        song?.let { viewModel.loadSong(it) }
    }

    val uiState by viewModel.uiState.collectAsState()
    val title by viewModel.title.collectAsState()
    val verses by viewModel.verses.collectAsState()
    val indicators by viewModel.indicators.collectAsState()

    Scaffold(
        topBar = {
            Surface(shadowElevation = 3.dp) {
                AppTopBar(
                    title = title,
                    actions = {
                        if (uiState == UiState.Loaded) {
                            IconButton(onClick = { }) {
                                Icon(
                                    imageVector = Icons.Filled.Favorite,
                                    contentDescription = "Like"
                                )
                            }
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = onBackPressed) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    },
                )
            }
        },
        content = {
            Box(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize()
                    .background(ThemeColors.accent1)
            ) {
                when (uiState) {
                    is UiState.Error -> ErrorState(
                        errorMessage = (uiState as UiState.Error).errorMessage,
                        onRetry = { }
                    )

                    is UiState.Loaded -> PresenterContent(
                        verses = verses,
                        indicators = indicators
                    )

                    UiState.Loading -> LoadingState("Loading song ...")

                    else -> EmptyState()
                }
            }
        }
    )
}

@Composable
fun PresenterContent(
    verses: List<String>,
    indicators: List<String>
) {
    val pagerState = rememberPagerState { verses.size }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        PresenterTabs(
            pagerState = pagerState,
            verses = verses,
            modifier = Modifier
                .weight(1f)
        )

        PresenterIndicators(
            pagerState = pagerState,
            indicators = indicators,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPresenterContent() {
    PresenterContent(
        verses = SampleVerses,
        indicators = SampleIndicators
    )
}
