package com.maxciv.healthcheck.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Help
import androidx.compose.material.icons.rounded.PauseCircle
import androidx.compose.material.icons.rounded.Pending
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.maxciv.healthcheck.domain.HealthCheckResult
import com.maxciv.healthcheck.ui.theme.Blue
import com.maxciv.healthcheck.ui.theme.Green
import com.maxciv.healthcheck.ui.theme.HealthCheckTheme
import com.maxciv.healthcheck.ui.theme.Orange
import com.maxciv.healthcheck.ui.theme.Red
import dagger.hilt.android.AndroidEntryPoint

/**
 * @author maxim.oleynik
 * @since 15.08.2021
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            HealthCheckTheme {
                ProvideWindowInsets {
                    HealthCheckApp()
                }
            }
        }
    }
}

@Composable
fun HealthCheckApp() {
    Surface(
        color = MaterialTheme.colors.background,
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        MainScreen()
    }
}

interface ListItem

data class HealthCheckCollectionListItem
constructor(
    val id: Long,
    val name: String,
    val healthCheckListItems: List<HealthCheckListItem>,
) : ListItem

data class HealthCheckListItem
constructor(
    val id: Long,
    val name: String,
    val url: String,
    val healthCheckResult: HealthCheckResult,
)

@OptIn(ExperimentalPagerApi::class)
@Composable
fun MainScreen() {
    val viewModel = hiltViewModel<MainViewModel>()

    val listItems: List<ListItem> by viewModel.state.healthCheckCollectionListItemsFlow.collectAsState()

    val pagerState = rememberPagerState(
        pageCount = 2, //TODO 0
//        pageCount = listItems.size,
        initialOffscreenLimit = 2,
    )

    HorizontalPager(
        state = pagerState,
    ) { page ->
        when (val listItem = listItems.getOrNull(page)) {
            is HealthCheckCollectionListItem -> Statuses(listItem)
            else -> Unit
//            else -> error("Unsupported list item: $listItem") //TODO 0
        }
    }

    //TODO 0 колонка табы
}

@Composable
fun NavigationHolder() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "profile") {
        composable("profile") { }
        composable("friendslist") { }
    }

}

@Composable
fun HealthCheckCollectionScreen() {

}

@Composable
fun HealthCheckListView() {

}

@Composable
fun HealthCheckCollectionControlScreen() {
    //TODO 0 тут просмотр, перемещение, редактирование, добавление, быстрый переход
}

@Composable
fun SettingsScreen() {

}

@Composable
fun Status() {
    val viewModel = hiltViewModel<MainViewModel>()
    val isUrlReachable by viewModel.state.isUrlReachableFlow.collectAsState()

    val icon = if (isUrlReachable) {
        Icons.Rounded.CheckCircle
    } else {
        Icons.Rounded.Cancel
    }
    val tint = if (isUrlReachable) {
        Green
    } else {
        Red
    }

    Row(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        Icon(
            imageVector = icon,
            tint = tint,
            contentDescription = null,
            modifier = Modifier.padding(end = 16.dp),
        )
        Text(text = "http://52.15.88.104:8080/")
    }
}

@Composable
fun Statuses(healthCheckCollectionListItem: HealthCheckCollectionListItem) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        healthCheckCollectionListItem.healthCheckListItems.forEach {
            Status2(it)
        }
    }
}

@Composable
fun Status2(healthCheckListItem: HealthCheckListItem) {
    val icon = when (healthCheckListItem.healthCheckResult) {
        HealthCheckResult.INITIAL -> Icons.Rounded.Help
        HealthCheckResult.LOADING -> Icons.Rounded.Pending
        HealthCheckResult.SUCCESS -> Icons.Rounded.CheckCircle
        HealthCheckResult.FAIL -> Icons.Rounded.Cancel
        HealthCheckResult.NO_INTERNET -> Icons.Rounded.PauseCircle
    }

    val tint = when (healthCheckListItem.healthCheckResult) {
        HealthCheckResult.INITIAL -> Blue
        HealthCheckResult.LOADING -> Blue
        HealthCheckResult.SUCCESS -> Green
        HealthCheckResult.FAIL -> Red
        HealthCheckResult.NO_INTERNET -> Orange
    }

    Row(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Icon(
            imageVector = icon,
            tint = tint,
            contentDescription = null,
            modifier = Modifier.padding(end = 16.dp),
        )
        Text(text = healthCheckListItem.url)
    }
}

@Composable
fun Greeting(name: String) {
    val viewModel = hiltViewModel<MainViewModel>()
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    HealthCheckTheme {
        Greeting("Android")
    }
}
