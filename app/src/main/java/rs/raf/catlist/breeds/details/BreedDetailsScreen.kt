package rs.raf.catlist.breeds.details

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import coil.compose.SubcomposeAsyncImage
import rs.raf.catlist.breeds.list.model.BreedUiModel
import rs.raf.catlist.core.compose.AppIconButton
import rs.raf.catlist.photos.grid.model.PhotoUiModel

fun NavGraphBuilder.breedDetails(
    route: String,

    onGalleryClick: (String) -> Unit,
    navController: NavController,
    onClose: () -> Unit,
) = composable(
    route = route,

    enterTransition = { slideInVertically { it } },
    popExitTransition = { slideOutVertically { it } },

    ) { navBackStackEntry ->

    val breedDetailsViewModel: BreedDetailsViewModel = hiltViewModel(navBackStackEntry)

    val state = breedDetailsViewModel.state.collectAsState()




    CatDetailsScreen(
        state = state.value,
        onGalleryClick = onGalleryClick,
        onClose = onClose
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatDetailsScreen(
    state: BreedDetailsContract.BreedDetailsState,
    onGalleryClick: (String) -> Unit,
    onClose: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = state.breed?.name ?: "Loading",
                        style = TextStyle(
                            fontSize = 27.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF9B6559)
                        )
                    )
                },
                navigationIcon = {
                    AppIconButton(
                        imageVector = Icons.Default.ArrowBack,
                        onClick = onClose,
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFC4BFAE), // Bež boja za pozadinu top bara
                    scrolledContainerColor = Color(0xFFC4BFAE),
                ),
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                if (state.fetching) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator()
                    }
                } else if (state.error) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(text = "Failed to load.")
                    }
                } else if (state.breed != null) {
                    state.image?.let {
                        LoginDataColumn(
                            data = state.breed,
                            image = it,
                            onGalleryClick = onGalleryClick,
                            wiki = state.breed.wikipedia_url,
                        )
                    }
                }
            }
        }
    )
}

@Composable
private fun LoginDataColumn(
    data: BreedUiModel,
    image: PhotoUiModel,
    onGalleryClick: (String) -> Unit,
    wiki: String,
) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
    ) {
        SubcomposeAsyncImage(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
                .align(Alignment.CenterHorizontally),
            model = image.url,
            contentScale = ContentScale.Crop,
            contentDescription = null,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            style = MaterialTheme.typography.bodyMedium.copy(
                color = Color(0xFF9B6559) // Smeđa boja za opis
            ),
            text = data.description,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            style = MaterialTheme.typography.bodyLarge.copy(
                color = Color(0xFF9B6559) // Smeđa boja za temperament
            ),
            text = data.temperament,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            style = MaterialTheme.typography.bodyLarge.copy(
                color = Color(0xFF9B6559) // Smeđa boja za origin
            ),
            text = "Origin: " + data.origin,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            style = MaterialTheme.typography.bodyLarge.copy(
                color = Color(0xFF9B6559) // Smeđa boja za life span
            ),
            text = "Life span: " + data.life_span,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            style = MaterialTheme.typography.bodyLarge.copy(
                color = Color(0xFF9B6559) // Smeđa boja za težinu
            ),
            text = "Average weight: " + data.weight,
        )

        Spacer(modifier = Modifier.height(8.dp))
        var retka = if(data.rare == 0) "No" else "Yes"

        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            style = MaterialTheme.typography.bodyLarge.copy(
                color = Color(0xFF9B6559) // Smeđa boja za rare status
            ),
            text = "Rare: " + retka,
        )

        Spacer(modifier = Modifier.height(8.dp))
        Divider(color = Color(0xFF9B6559))
        Spacer(modifier = Modifier.height(16.dp))

        // Prvi red widget-a
        Row (
            Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            RatingBar(rating = data.child_friendly, trait = "Child Friendly")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row (
            Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            RatingBar(rating = data.affection_level, trait = "Affection Level")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row (
            Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            RatingBar(rating = data.adaptability, trait = "Adaptability")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row (
            Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            RatingBar(rating = data.intelligence, trait = "Intelligence")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row (
            Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            RatingBar(rating = data.social_needs, trait = "Social Needs")
        }

        Spacer(modifier = Modifier.height(8.dp))
        Divider(color = Color(0xFF9B6559))

        Spacer(modifier = Modifier.height(16.dp))

        val context = LocalContext.current
        val intent = remember { Intent(Intent.ACTION_VIEW, Uri.parse(wiki)) }

        Row(
            Modifier.align(Alignment.CenterHorizontally)
        ) {
            Button(
                onClick = { context.startActivity(intent) },
                modifier = Modifier
                    .width(200.dp)
                    .padding(16.dp),
            ) {
                Text(text = "Wikipedia")
            }

            Button(
                onClick = {  onGalleryClick(data.id) },
                modifier = Modifier
                    .width(200.dp)
                    .padding(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFDF6C49) // Boja dugmeta za Gallery
                )
            ) {
                Text(text = "Gallery")
            }
        }
    }
}

@Composable
fun RatingBar(
    rating: Int,
    trait: String,
    maxRating: Int = 5,
    filledIcon: ImageVector = Icons.Default.Star,
    emptyIcon: ImageVector = Icons.Default.StarOutline,
    iconTint: Color = Color(0xFFFFD700)
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row {
            repeat(maxRating) { index ->
                val icon = if (index < rating) filledIcon else emptyIcon
                val tint = if (index < rating) iconTint else Color.Gray
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.padding(2.dp),
                    tint = tint
                )
            }
        }
        Text(
            text = trait,
            style = TextStyle(fontSize = 18.sp, color = Color(0xFF9B6559)) // Smeđa boja za ime karakteristike
        )
    }
}
