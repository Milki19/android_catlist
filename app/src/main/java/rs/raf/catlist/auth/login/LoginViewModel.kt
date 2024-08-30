package rs.raf.catlist.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import rs.raf.catlist.auth.AuthStore
import rs.raf.catlist.auth.domain.LoginData
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authStore: AuthStore
) : ViewModel() {

    private val _state = MutableStateFlow(LoginContract.LoginState())
    val state = _state.asStateFlow()
    private fun setState(reducer: LoginContract.LoginState.() -> LoginContract.LoginState) = _state.update(reducer)

    private val events = MutableSharedFlow<LoginContract.LoginEvent>()
    fun setEvent(event: LoginContract.LoginEvent) = viewModelScope.launch { events.emit(event) }

    init {
        observeEvents()
    }

    private fun observeEvents() {
        viewModelScope.launch {
            events.collect { event ->
                when (event) {
                    is LoginContract.LoginEvent.OnNameChange -> {
                        setState { copy(name = event.name, isNameValid = event.name != "") }
                    }
                    is LoginContract.LoginEvent.OnNicknameChange -> {
                        setState { copy(nickname = event.nickname, isNicknameValid = event.nickname.matches(Regex("^[a-zA-Z0-9_]*$"))) }
                    }
                    is LoginContract.LoginEvent.OnEmailChange -> {
                        setState { copy(email = event.email, isEmailValid = android.util.Patterns.EMAIL_ADDRESS.matcher(event.email).matches()) }
                    }
                    LoginContract.LoginEvent.OnCreateProfile -> {
                        if (state.value.isNameValid && state.value.isNicknameValid && state.value.isEmailValid) {
                            val newProfileData = LoginData(
                                fullName = state.value.name,
                                nickname = state.value.nickname,
                                email = state.value.email
                            )
                            authStore.updateProfileData(newProfileData)
                            setState { copy(isProfileCreated = true) }
                        }
                    }
                }
            }
        }
    }
}