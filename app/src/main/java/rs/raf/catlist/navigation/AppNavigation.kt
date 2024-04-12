package rs.raf.catlist.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import rs.raf.catlist.breeds.details.breedDetails
import rs.raf.catlist.breeds.list.breedsListScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "breeds",
    ) {
        breedsListScreen(
            route = "breeds",
            navController = navController,
        )

        breedDetails(
            route = "breeds/{id}",
            navController = navController,
        )

//        breedsEditor(
//            route = "breeds/editor?id={dataId}",
//            arguments = listOf(
//                navArgument(name = "dataId") {
//                    this.type = NavType.StringType
//                    this.nullable = true
//                }
//            ),
//            navController = navController,
//        )
    }
}