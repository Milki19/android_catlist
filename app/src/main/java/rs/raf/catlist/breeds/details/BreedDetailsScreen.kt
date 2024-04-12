package rs.raf.catlist.breeds.details

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import rs.raf.catlist.breeds.domain.BreedData
import rs.raf.catlist.core.compose.AppIconButton
import rs.raf.catlist.core.compose.NoDataContent

fun NavGraphBuilder.breedDetails(
    route: String,
    navController: NavController,
) = composable(
    route = route,
) { navBackStackEntry ->
    val dataId = navBackStackEntry.arguments?.getString("id")
        ?: throw IllegalArgumentException("id is required.")

    // We have to provide factory class to instantiate our view model
    // since it has a custom property in constructor
    val breedDetailsViewModel = viewModel<BreedDetailsViewModel>(
        factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                // We pass the breedId which we read from arguments above
                return BreedDetailsViewModel(breedId = dataId) as T
            }
        },
    )
    val state = breedDetailsViewModel.state.collectAsState()

    BreedDetailsScreen(
        state = state.value,
//        eventPublisher = {
//            //breedDetailsViewModel.setEvent(it)
//        },
        onEditClick = {
            navController.navigate(route = "breeds/editor?id=${state.value.breedId}")
        },
        onClose = {
            navController.navigateUp()
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BreedDetailsScreen(
    state: BreedDetailsState,
    //eventPublisher: (BreedDetailsUiEvent) -> Unit,
    onEditClick: () -> Unit,
    onClose: () -> Unit,
) {
    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(text = state.data?.name ?: "Loading")
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    scrolledContainerColor = MaterialTheme.colorScheme.primaryContainer,
                ),
                actions = {
                    if (state.data != null) {
                        AppIconButton(
                            imageVector = Icons.Default.Edit,
                            onClick = onEditClick,
                        )
                    }
                },
                navigationIcon = {
                    AppIconButton(
                        imageVector = Icons.Default.ArrowBack,
                        onClick = onClose,
                    )
                }
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
                } else if (state.error != null) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        val errorMessage = when (state.error) {
                            is BreedDetailsState.DetailsError.DataUpdateFailed ->
                                "Failed to load. Error message: ${state.error.cause?.message}."
                        }
                        Text(text = errorMessage)
                    }
                } else if (state.data != null) {
                    BreedDataColumn(
                        data = state.data,
                    )
                } else {
                    NoDataContent(id = state.breedId)
                }
            }
        }
    )
}

@Composable
private fun BreedDataColumn(
    data: BreedData,
) {
    Column {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            style = MaterialTheme.typography.headlineSmall,
            text = data.description,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            style = MaterialTheme.typography.bodyLarge,
            text = data.name,
        )

        Spacer(modifier = Modifier.height(32.dp))
    }
}



@Preview
@Composable
fun PreviewDetailsScreen() {
    Surface {
        BreedDetailsScreen(
            state = BreedDetailsState(
                breedId = "124124",
                data = BreedData(
                    id = "12",
                    name = "cana",
                    alternateName = "mamina stara macka",
                    countryCodes = "RS",
                    lifeExpectancy = "15-20 years",
                    averageHeight = "40-60 cm",
                    rare = "Very",
                    wikipedia = "www.wikipedia.com",
                    description = "najlepsa macka na svetu",
                    temperamet = "Predivna"
                ),

//                val id: String,
//            val name: String,
//        val alternateName: String,
//        val countryCodes: String,
//        val lifeExpectancy: String,
//        val averageHeight: String,
//        val rare: String,
//        val wikipedia: String,
//        val description: String,
//        val temperamet: String
            ),

            onEditClick = {},
            onClose = {},
        )
    }
}