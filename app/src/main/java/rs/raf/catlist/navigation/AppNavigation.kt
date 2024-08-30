package rs.raf.catlist.navigation

import androidx.compose.animation.core.spring
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import kotlinx.coroutines.launch
import rs.raf.catlist.auth.AuthStore
import rs.raf.catlist.auth.login.login
import rs.raf.catlist.auth.profile.profile
import rs.raf.catlist.breeds.details.breedDetails
import rs.raf.catlist.breeds.list.breeds
import rs.raf.catlist.leaderboard.list.leaderboard
import rs.raf.catlist.photos.gallery.photoGallery
import rs.raf.catlist.photos.grid.photoGrid
import rs.raf.catlist.quiz.ui.quiz

@Composable
fun BreedNavigation(authStore: AuthStore) {
    val navController = rememberNavController()
    val scope = rememberCoroutineScope()
    val isProfileEmpty by authStore.isEmpty.collectAsState(initial = true)
    var startDest by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        scope.launch {
            startDest = if (isProfileEmpty) "login" else "breed"
        }
    }

    if (startDest == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }else {
        NavHost(
            navController = navController,
            startDestination = startDest!!,
            enterTransition = {
                slideInHorizontally(
                    animationSpec = spring(),
                    initialOffsetX = { it },
                )
            },
            exitTransition = { scaleOut(targetScale = 0.75f) },
            popEnterTransition = { scaleIn(initialScale = 0.75f) },
            popExitTransition = { slideOutHorizontally { it } },
        ) {

            login(
                route = "login",
                onCreate = {
                    navController.navigate(route = "breed")
                }
            )

            breeds(
                route = "breed",
                onBreedClick = {
                    navController.navigate(route = "breed/$it")
                },
                onQuizClick = {
                    navController.navigate(route = "quiz")
                },
                onProfileClick = {
                    navController.navigate(route = "profile")
                },
                onLeaderBoardClick = {
                    navController.navigate(route = "leaderBoard")
                }
            )

            breedDetails(
                route = "breed/{breedId}",
//                arguments = listOf(
//                    navArgument(name = "catId") {
//                        nullable = false
//                        type = NavType.StringType
//                    }
//                ),
                onGalleryClick = {
                    navController.navigate(route = "breeds/grid/$it")
                },
                onClose = {
                    navController.popBackStack()
                },
                navController = navController,
            )

            photoGrid(
                route = "breeds/grid/{breedId}",
                arguments = listOf(
                    navArgument(name = "breedId") {
                        nullable = false
                        type = NavType.StringType
                    }
                ),
                onPhotoClick = {
                    navController.navigate(route = "photo/$it")
                },
                onClose = {
                    navController.popBackStack()
                },
            )

            photoGallery(
                route = "photo/{breedId}",
                arguments = listOf(
                    navArgument(name = "breedId") {
                        nullable = false
                        type = NavType.StringType
                    }
                ),
                onClose = {
                    navController.navigateUp()
                },
            )

            leaderboard(
                route = "leaderBoard",
                onCatalogClick = {
                    navController.navigate(route = "breeds")
                },
                onProfileClick = {
                    navController.navigate(route = "profile")
                },
                onQuizClick = {
                    navController.navigate(route = "quiz")
                },
            )

            profile(
                route = "profile",
                onCatsClick = {
                    navController.navigate(route = "breed")
                },
                onQuizClick = {
                    navController.navigate(route = "quiz")
                },
                onLeaderboardClick = {
                    navController.navigate(route = "leaderboard")
                },
            )

            quiz(
                route = "quiz",
                onQuizCompleted = {
                    navController.navigate(route = "breed")
                },
                onClose = {
                    navController.navigateUp()
                }
            )


        }
    }
}
inline val SavedStateHandle.breedId: String
    get() = checkNotNull(get("breedId")) { "breedId is mandatory" }

inline val SavedStateHandle.photoId: String
    get() = checkNotNull(get("photoId")) { "photoId is mandatory" }