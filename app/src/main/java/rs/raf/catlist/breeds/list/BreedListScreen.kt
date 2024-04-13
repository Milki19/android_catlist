package rs.raf.catlist.breeds.list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import rs.raf.catlist.breeds.domain.BreedData

@ExperimentalMaterial3Api
fun NavGraphBuilder.breedsListScreen(
    route: String,
    navController: NavController,
) = composable(route = route) {
    val breedListViewModel = viewModel<BreedListViewModel>()
    val state by breedListViewModel.state.collectAsState()

    BreedListScreen(
        state = state,
        onItemClick = {
            navController.navigate(route = "breeds/${it.id}")
        },
        onSearch = { searchText ->
            breedListViewModel.searchBreedByName(searchText)
        },
        resetSearch = {
            breedListViewModel.clearSearch()
        }
    )
}


@ExperimentalMaterial3Api
@Composable
fun BreedListScreen(
    state: BreedListState,
    onItemClick: (BreedData) -> Unit,
    onSearch: (String) -> Unit,
    resetSearch: () -> Unit,
)  {
    var searchText by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    LaunchedEffect(searchText) {
        if (searchText.isNotBlank()) {
            onSearch(searchText)
        }
    }

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            ) {
                CenterAlignedTopAppBar(
                    title = { Text(text = "List of Cats") },
                    modifier = Modifier
                        .align(Alignment.TopCenter),
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFFC4BFAE),
                        scrolledContainerColor = Color(0xFF9B6559),
                    ),
                )
                Column(
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(bottom = 22.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .background(Color(0xFFC4BFAE))
                            .padding(8.dp)
                    ) {
                        OutlinedTextField(
                            value = searchText,
                            onValueChange = { searchText = it },
                            label = { Text("Search") },
                            modifier = Modifier.weight(1f),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Search
                            ),
                            keyboardActions = KeyboardActions(
                                onSearch = {
                                    focusManager.clearFocus()
                                    if (searchText.isNotBlank()) {
                                        onSearch(searchText)
                                    } else {
                                        searchText = ""
                                        resetSearch()
                                    }
                                }
                            ),
                        )
                    }
                }
            }
        },
        content = {
            state?.let { currentState ->
                BreedList(
                    paddingValues = it,
                    items = currentState.breeds,
                    onItemClick = onItemClick,
                    fetching = currentState.fetching,
                    error = currentState.error,
                )
            }
        }
    )
}

@Composable
private fun BreedList(
    items: List<BreedData>,
    paddingValues: PaddingValues,
    onItemClick: (BreedData) -> Unit,
    fetching: Boolean,
    error: BreedListState.ListError?
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .verticalScroll(scrollState)
            .fillMaxSize()
            .padding(paddingValues),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        items.forEach {
            Column {
                key(it.id) {
                    BreedListItem(
                        data = it,
                        onClick = {
                            onItemClick(it)
                        },
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        if (items.isEmpty()) {
            when {
                fetching -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator()
                    }
                }
                error != null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        val errorMessage = when (error) {
                            is BreedListState.ListError.ListUpdateFailed ->
                                "Failed to load. Error message: ${error.cause?.message}."
                            else -> "Unknown error occurred."
                        }
                        Text(
                            text = errorMessage,
                            textAlign = TextAlign.Center
                        )
                    }
                }
                else -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(text = "There are no cats available for this search.", textAlign = TextAlign.Center)
                    }
                }
            }
        }
    }
}


@Composable
private fun BreedListItem(
    data: BreedData,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .clickable { onClick() },
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = buildString {
                    append(data.name)
                    if (data.alternativeNames.isNotEmpty()) {
                        append(" (${data.alternativeNames})")
                    }
                },
                style = TextStyle(color = Color(0xFF9B6559),fontWeight = FontWeight.Bold),

            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = data.description.take(250).plus("..."),
                style = TextStyle(color = Color(0xFF9B6559))
            )
            Spacer(modifier = Modifier.height(8.dp))
            val temperamentList = takeThreeTemperaments(data).joinToString(", ")
            Text(
                text = temperamentList,
                style = TextStyle(color = Color(0xFF9B6559), fontStyle = FontStyle.Italic)
            )
        }
        Icon(
            modifier = Modifier.padding(16.dp),
            imageVector = Icons.Default.KeyboardArrowRight,
            contentDescription = null,
        )
    }
}

private fun takeThreeTemperaments(data: BreedData): List<String> {
    val temperamentList = data.temperament.split(",")
    return temperamentList.shuffled().take(3)
}
