package rs.raf.catlist.photos.grid

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import coil.compose.SubcomposeAsyncImage
import rs.raf.catlist.core.compose.AppIconButton
import rs.raf.catlist.photos.grid.model.PhotoUiModel
import rs.raf.catlist.photos.grid.AlbumGridContract.AlbumGridUiState



fun NavGraphBuilder.photoGrid(
    route: String,
    arguments: List<NamedNavArgument>,
    onPhotoClick: (String) -> Unit,
    onClose: () -> Unit,
) = composable(
    route = route,
    arguments = arguments
) { navBackStackEntry ->

    val photoGridViewModel: AlbumsGridViewModel = hiltViewModel(navBackStackEntry)

    val state = photoGridViewModel.state.collectAsState()

    AlbumGridScreen(
        state = state.value,
        onPhotoClick = onPhotoClick,
        onClose = onClose,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumGridScreen(
    state: AlbumGridUiState,
    onPhotoClick: (String) -> Unit,
    onClose: () -> Unit,
) {
    Scaffold (
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Gallery",
                        style = TextStyle(
                            fontSize = 27.sp,
                            fontWeight = FontWeight.Bold
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
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
        content = { paddingValues ->
            BoxWithConstraints (
                modifier = Modifier,
                contentAlignment = Alignment.BottomCenter,
            ){
                val screenWith = this.maxWidth
                val cellSize = (screenWith / 2) - 4.dp

                if(state.photos.isEmpty() && state.updating){
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(36.dp),
                        )
                    }
                } else {
                    LazyVerticalGrid(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 5.dp)
                            .padding(top = 10.dp),
                        columns = GridCells.Fixed(2),
                        contentPadding = paddingValues,
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        itemsIndexed(
                            items = state.photos,
                            key = { index: Int, photo: PhotoUiModel -> photo.photoId }
                        ) { index: Int, photo: PhotoUiModel ->
                            Card (
                                modifier = Modifier
                                    .size(cellSize)
                                    .clickable {
                                        onPhotoClick(photo.breedId)
                                    }
                            ){
                                SubcomposeAsyncImage(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(280.dp)
                                        .align(Alignment.CenterHorizontally),
                                    model = photo.url,
                                    contentScale = ContentScale.Crop,
                                    contentDescription = null,
                                )
                            }

                        }
                    }
                }
            }

        }
    )
}