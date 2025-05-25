
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun previewScreen(){

    MyApp()
}

@Composable
fun MyApp() {
    val navController = rememberNavController()

    val items = listOf(
        BottomNavItem("homeScreen", "Home", Icons.Default.Home),
        //BottomNavItem("search", "Search", Icons.Default.Search),
        //BottomNavItem("profile", "Profile", Icons.Default.Person)
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                val currentDestination = navController.currentBackStackEntryAsState().value?.destination?.route
                items.forEach { item ->
                    NavigationBarItem(
                        selected = currentDestination == item.route,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(imageVector = item.icon, contentDescription = item.title) },
                        label = { Text(item.title) }
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "homeScreen",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("homeScreen") { ScreenContent("Home Screen") }
            //composable("profileScreen") { ScreenContent("Search Screen") }
            //composable("chatScreen") { ScreenContent("Profile Screen") }
        }
    }
}

@Composable
fun ScreenContent(title: String) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Text(text = title, style = MaterialTheme.typography.headlineMedium)
    }
}

data class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
)