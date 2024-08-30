package rs.raf.catlist.auth.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

fun NavGraphBuilder.login(
    route: String,
    onCreate: () -> Unit
) = composable(route = route) {
    val loginViewModel = hiltViewModel<LoginViewModel>()
    val state = loginViewModel.state.collectAsState()

    LoginScreen(
        state = state.value,
        eventPublisher = {
            loginViewModel.setEvent(it)
        },
        onCreate = onCreate
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    state: LoginContract.LoginState,
    eventPublisher: (uiEvent: LoginContract.LoginEvent) -> Unit,
    onCreate: () -> Unit
) {
    Scaffold (
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Profile",
                        style = TextStyle(
                            fontSize = 27.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF9B6559)
                        )
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFC4BFAE), // Bež boja za pozadinu top bara
                    titleContentColor = Color(0xFF9B6559) // Smeđa boja za tekst u top baru
                ),
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(top = 60.dp)
                    .padding(paddingValues),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Create Account",
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(horizontal = 10.dp)
                        .padding(top = 20.dp),
                    style = TextStyle(
                        fontSize = 35.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF9B6559) // Smeđa boja za naslov
                    ),
                    textAlign = TextAlign.Center
                )
                TextField(
                    value = state.name,
                    onValueChange = { eventPublisher(LoginContract.LoginEvent.OnNameChange(it)) },
                    label = { Text("Name", color = Color(0xFF9B6559)) }, // Smeđa boja za labelu
                    isError = !state.isNameValid,
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .padding(horizontal = 10.dp)
                        .fillMaxWidth(),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.Transparent, // Transparentna pozadina
                        cursorColor = Color(0xFF9B6559), // Smeđa boja za kursor
                        focusedIndicatorColor = Color(0xFF9B6559), // Smeđa boja za ivicu kad je u fokusu
                        unfocusedIndicatorColor = Color(0xFF9B6559), // Smeđa boja za ivicu kad nije u fokusu
                        focusedTextColor = Color(0xFF9B6559), // Smeđa boja za tekst u fokusu
                        unfocusedTextColor = Color(0xFF9B6559) // Smeđa boja za tekst van fokusa
                    ),
                )
                if (!state.isNameValid) {
                    Text(text = "Name cannot be empty", color = Color(0xFFDF6C49)) // Crvena boja za error poruku
                }

                TextField(
                    value = state.nickname,
                    onValueChange = { eventPublisher(LoginContract.LoginEvent.OnNicknameChange(it)) },
                    label = { Text("Nickname", color = Color(0xFF9B6559)) }, // Smeđa boja za labelu
                    isError = !state.isNicknameValid,
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .padding(horizontal = 10.dp)
                        .fillMaxWidth(),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.Transparent, // Transparentna pozadina
                        cursorColor = Color(0xFF9B6559), // Smeđa boja za kursor
                        focusedIndicatorColor = Color(0xFF9B6559), // Smeđa boja za ivicu kad je u fokusu
                        unfocusedIndicatorColor = Color(0xFF9B6559), // Smeđa boja za ivicu kad nije u fokusu
                        focusedTextColor = Color(0xFF9B6559), // Smeđa boja za tekst u fokusu
                        unfocusedTextColor = Color(0xFF9B6559) // Smeđa boja za tekst van fokusa
                    ),
                )
                if (!state.isNicknameValid) {
                    Text(
                        text = "Nickname can only contain letters, numbers, and underscores",
                        color = Color(0xFFDF6C49) // Crvena boja za error poruku
                    )
                }

                TextField(
                    value = state.email,
                    onValueChange = { eventPublisher(LoginContract.LoginEvent.OnEmailChange(it)) },
                    label = { Text("Email", color = Color(0xFF9B6559)) }, // Smeđa boja za labelu
                    isError = !state.isEmailValid,
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .padding(horizontal = 10.dp)
                        .fillMaxWidth(),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.Transparent, // Transparentna pozadina
                        cursorColor = Color(0xFF9B6559), // Smeđa boja za kursor
                        focusedIndicatorColor = Color(0xFF9B6559), // Smeđa boja za ivicu kad je u fokusu
                        unfocusedIndicatorColor = Color(0xFF9B6559), // Smeđa boja za ivicu kad nije u fokusu
                        focusedTextColor = Color(0xFF9B6559), // Smeđa boja za tekst u fokusu
                        unfocusedTextColor = Color(0xFF9B6559) // Smeđa boja za tekst van fokusa
                    ),
                )
                if (!state.isEmailValid) {
                    Text(
                        text = "Enter a valid email address",
                        color = Color(0xFFDF6C49) // Crvena boja za error poruku
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        eventPublisher(LoginContract.LoginEvent.OnCreateProfile)
                        onCreate()
                    },
                    enabled = state.isNameValid && state.isNicknameValid && state.isEmailValid,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFDF6C49), // Crvena boja za dugme
                        contentColor = Color.White,
                    )
                ) {
                    Text(
                        text = "Create",
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp,
                        ),
                    )
                }
            }
        }
    )
}
