
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import moe.tlaster.precompose.PreComposeApp
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.rememberNavigator
import repository.IceTeaRepositoryImpl
import ui.home.HomeScreen

fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "Shafwah") {
        App {
            PreComposeApp {
                DesktopApp()
            }
        }
    }
}

@Composable
private fun DesktopApp() {
    val navigator = rememberNavigator()

    NavHost(
        navigator = navigator,
        initialRoute = Destinations.home.route
    ) {
        scene(
            route = Destinations.home.route
        ) { backStackEntry ->
            HomeScreen(IceTeaRepositoryImpl())
        }
    }
}
