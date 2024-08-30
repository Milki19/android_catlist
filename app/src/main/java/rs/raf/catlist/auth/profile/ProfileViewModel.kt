package rs.raf.catlist.auth.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import rs.raf.catlist.auth.AuthStore
import rs.raf.catlist.quiz.repository.QuizRepository
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authStore: AuthStore,
    private val kvizRepository: QuizRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileContract.ProfileState())
    val state = _state.asStateFlow()
    private fun setState(reducer: ProfileContract.ProfileState.() -> ProfileContract.ProfileState) = _state.update(reducer)

    private val events = MutableSharedFlow<ProfileContract.ProfileEvent>()
    fun setEvent(event: ProfileContract.ProfileEvent) = viewModelScope.launch { events.emit(event) }

    init {
        observeEvents()
        GetProfileData()
    }

    private fun observeEvents() {
        viewModelScope.launch {
            events.collect { event ->
                when (event) {
                    is ProfileContract.ProfileEvent.EditProfile -> {
                        if (event.name != state.value.name && state.value.isNameValid){
                            authStore.updateFullName(event.name)
                            setState { copy(name = event.name) }
                        }
                        if (event.email != state.value.email && state.value.isEmailValid) {
                            authStore.updateEmail(event.email)
                            setState { copy(email = event.email) }
                        }
                        if (event.nickname != state.value.nickname && state.value.isNicknameValid){
                            authStore.updateNickname(event.nickname)
                            setState { copy(nickname = event.nickname) }
                        }
                    }
                    is ProfileContract.ProfileEvent.OnNameChange -> {
                        setState { copy(isNameValid = event.name != "" ) }
                    }
                    is ProfileContract.ProfileEvent.OnNicknameChange -> {
                        setState { copy(isNicknameValid = event.nickname.matches(Regex("^[a-zA-Z0-9_]*$"))) }
                    }
                    is ProfileContract.ProfileEvent.OnEmailChange -> {
                        setState { copy(isEmailValid = android.util.Patterns.EMAIL_ADDRESS.matcher(event.email).matches()) }
                    }
                }
            }
        }
    }



    private fun GetProfileData() {
        viewModelScope.launch {
            val profileData = authStore.data.first()
            val quizResultsFlow = kvizRepository.getAllQuizResults(profileData.nickname)
            val bestScore = kvizRepository.getBestScore(profileData.nickname) ?: 0f
            val bestPosition = if(kvizRepository.getBestPosition(profileData.nickname) == null) 0 else kvizRepository.getBestPosition(profileData.nickname)!!

            quizResultsFlow.collect { quizResults ->
                setState {
                    copy(
                        name = profileData.fullName,
                        nickname = profileData.nickname,
                        email = profileData.email,
                        quizResults = quizResults,
                        bestScore = bestScore,
                        bestPosition = bestPosition
                    )
                }
            }
        }
    }
}