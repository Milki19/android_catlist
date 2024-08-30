package rs.raf.catlist.breeds.list

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CatchingPokemon
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Badge
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.coroutines.launch
import rs.raf.catlist.breeds.list.BreedListContract.BreedListUiEvent
import rs.raf.catlist.breeds.list.BreedListContract.BreedListState
import rs.raf.catlist.core.compose.AppIconButton
import rs.raf.catlist.core.theme.AppTheme

fun NavGraphBuilder.breeds(
    route: String,
    onBreedClick: (String) -> Unit,
    onProfileClick: () -> Unit,
    onQuizClick: () -> Unit,
    onLeaderBoardClick: () -> Unit,
) = composable(
    route = route
) {
    val breedListViewModel = hiltViewModel<BreedListViewModel>()

    val state = breedListViewModel.state.collectAsState()

    BreedListScreen(
        state = state.value,
        eventPublisher = {
            breedListViewModel.setEvent(it)
        },
        onBreedClick = onBreedClick,
        onProfileClick = onProfileClick,
        onQuizClick = onQuizClick,
        onLeaderBoardClick = onLeaderBoardClick

    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BreedListScreen(
    state: BreedListState,
    eventPublisher: (uiEvent: BreedListUiEvent) -> Unit,
    onBreedClick: (String) -> Unit,
    onProfileClick: () -> Unit,
    onQuizClick: () -> Unit,
    onLeaderBoardClick: () -> Unit
) {
    val uiScope = rememberCoroutineScope()
    val drawerState: DrawerState = rememberDrawerState(DrawerValue.Closed)
    

    BackHandler(enabled = drawerState.isOpen) {
        uiScope.launch { drawerState.close() }
    }

    ModalNavigationDrawer(
        modifier = Modifier,
        drawerState = drawerState,
        drawerContent = {
            UserListDrawer(
                state = state,
                onProfileClick = {
                    uiScope.launch {
                        drawerState.close()
                    }
                    onProfileClick()
                },
                onQuizClick = {
                    uiScope.launch { drawerState.close() }
                    onQuizClick()

                },
                onLeaderBoardClick = {
                    uiScope.launch { drawerState.close() }
                    onLeaderBoardClick()

                },
            )
        },
        content = {
            BreedListScaffold(
                state = state,
                onBreedClick = onBreedClick,
                onDrawerMenuClick = {
                    uiScope.launch {
                        drawerState.open()
                    }
                },
                eventPublisher = eventPublisher
            )

        }
    )

}


@Composable
private fun AppDrawerActionItem(
    icon: ImageVector,
    text: String,
    onClick: (() -> Unit)? = null,
) {
    ListItem(
        modifier = Modifier.clickable(
            enabled = onClick != null,
            onClick = { onClick?.invoke() }
        ),
        leadingContent = {
            Icon(imageVector = icon, contentDescription = null)
        },
        headlineContent = {
            Text(text = text)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UserListDrawer(
    state: BreedListState,
    onProfileClick: () -> Unit,
    onQuizClick: () -> Unit,
    onLeaderBoardClick: () -> Unit,
) {
    BoxWithConstraints {
        ModalDrawerSheet(
            modifier = Modifier.width(maxWidth * 3 / 4),
        ) {
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.BottomStart,
                ) {
                    Text(
                        modifier = Modifier.padding(all = 16.dp),
                        text = state.nickname,
                        style = AppTheme.typography.headlineSmall,
                    )
                }

                Divider(modifier = Modifier.fillMaxWidth())

                Column(modifier = Modifier.weight(2f)) {

                    AppDrawerActionItem(
                        icon = Icons.Default.Person,
                        text = "Profile",
                        onClick = onProfileClick,
                    )

                    AppDrawerActionItem(
                        icon = Icons.Default.Quiz,
                        text = "Quiz",
                        onClick = onQuizClick,
                    )

                    NavigationDrawerItem(
                        label = {
                            Text(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                text = "Breeds",
                            )
                        },
                        icon = {
                            Icon(imageVector = Icons.Default.CatchingPokemon, contentDescription = null)
                        },
                        badge = {
                            Badge {
                                Text(text = "20")
                            }
                        },
                        selected = true,
                        onClick = {

                        },
                    )

                    AppDrawerActionItem(
                        icon = Icons.Default.Leaderboard,
                        text = "LeaderBoard",
                        onClick = onLeaderBoardClick,
                    )

//
                }

                Divider(modifier = Modifier.fillMaxWidth())


            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun BreedListScaffold(
    state: BreedListState,
    onBreedClick: (String) -> Unit,
    onDrawerMenuClick: () -> Unit,
    eventPublisher: (uiEvent: BreedListUiEvent) -> Unit,
){

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    AppIconButton(
                        imageVector = Icons.Default.Menu,
                        onClick = onDrawerMenuClick,
                    )
                },
                title = {
                    Text(
                        text = "List of Cats",
                        style = TextStyle(
                            fontSize = 27.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF9B6559)
                        )
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFC4BFAE),
                ),
            )

            SearchBarAction(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 60.dp)
                    .padding(horizontal = 20.dp),
                onQueryChange = { query ->
                    eventPublisher(
                        BreedListUiEvent.SearchQueryChanged(
                            query = query
                        )
                    )
                },
                onCloseClicked = { eventPublisher(BreedListUiEvent.CloseSearchMode) },
                query = state.query,
                activated = state.isSearchMode
            )
        },

        content = { paddingValues ->
            if (state.loading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                }
            } else {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {

                    var help = state.breed
                    if(state.isSearchMode){
                        help = state.filteredBreeds
                    }

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = paddingValues,
                    ) {
                        items(
                            items = help,
                            key = { it.id },
                        ) { breed ->
                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = Color(0xFFE7E0EC)
                                ),
                                border = BorderStroke(2.dp, Color(0xFF9B6559)),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 8.dp)
                                    .padding(bottom = 16.dp)
                                    .clickable { onBreedClick(breed.id) },
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp)
                                ) {
                                    Text(
                                        text = breed.name,
                                        style = MaterialTheme.typography.headlineMedium.copy(
                                            color = Color(0xFF9B6559),
                                            fontWeight = FontWeight.Medium
                                        )
                                    )

                                    Text(
                                        text = "-" + breed.alt_names,
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            color = Color(0xFF9B6559)
                                        )
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Text(
                                        text = if(breed.description.length > 250) breed.description.take(250).plus("...") else breed.description,
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = Color(0xFF9B6559)
                                        )
                                    )

                                    val temperaments = breed.temperament.split(",")
                                    Spacer(modifier = Modifier.height(8.dp))

                                    Text(
                                        text = "Temperament: " + temperaments.shuffled().take(3).joinToString(", "),
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            color = Color(0xFF9B6559), // Smeđa boja za temperament
                                            fontStyle = FontStyle.Italic
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}

@Composable
private fun SearchBarAction(
    modifier: Modifier = Modifier,
    onQueryChange: (String) -> Unit,
    onCloseClicked: () -> Unit,
    query: String,
    activated: Boolean

) {

    var text by remember { mutableStateOf(query) }
    var active by remember { mutableStateOf(activated) }
    val focusManager = LocalFocusManager.current


    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,

        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        OutlinedTextField(
            modifier = modifier,
//                .fillMaxWidth()
//                .background(MaterialTheme.colorScheme.primaryContainer)
////                .padding(top = 8.dp, bottom = 8.dp)
//                .clip(RoundedCornerShape(25.dp))
//                .padding(horizontal = 16.dp),

            value = text,
            onValueChange = { newText ->
                text = newText
                active = true
                onQueryChange(newText)
            },

            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    focusManager.clearFocus()
                }
            ),
            placeholder = { Text(text = "Search") },
            label = { Text(text = "Search") },
        )
    }
}






// https://developer.android.com/develop/ui/compose/tooling/previews

class UserListStateParameterProvider : PreviewParameterProvider<BreedListState> {
    override val values: Sequence<BreedListState> = sequenceOf(
        BreedListState(
            loading = true,
        ),
        BreedListState(
            loading = false,
        ),
        BreedListState(
            loading = false,
            breed = listOf(
//                BreedUiModel(id = "nig", name = "ger", temperament = "nigger"),
//                BreedUiModel(id = "ni", name = "marko", wikipedia_url = "nesto", affection_level = 1, adaptability = 2, origin = "poreklo", alt_names = "", rare = 2, temperament = "temperament", social_needs = 3, child_friendly = 3, intelligence = 5, reference_image_id = "9012478", weight = Weight("12","11"), life_span = "13", description = "nanan"),
//                BreedApiModel(id = "la", name = "janko", wikipedia_url = "nesto", description = "nanan"),
//                BreedApiModel(id = "ga", name = "stefan", wikipedia_url = "nesto", description = "nanan"),
            ),
        ),
    )
}