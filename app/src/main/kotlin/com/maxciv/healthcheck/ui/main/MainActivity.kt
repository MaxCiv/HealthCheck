package com.maxciv.healthcheck.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.statusBarsPadding
import com.maxciv.healthcheck.ui.theme.Green
import com.maxciv.healthcheck.ui.theme.HealthCheckTheme
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
                    Surface(
                        color = MaterialTheme.colors.background,
                        modifier = Modifier.statusBarsPadding(),
                    ) {
                        Status()
                    }
                }
            }
        }
    }
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

    Row(modifier = Modifier.padding(16.dp)) {
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
