package rs.raf.catlist.auth.profile

import rs.raf.catlist.quiz.di.Quiz

interface ProfileContract {

    data class ProfileState(
        val name: String = "",
        val nickname: String = "",
        val email: String = "",
        val quizResults: List<Quiz> = emptyList(),
        val bestScore: Float = 0f,
        val bestPosition: Int = 0,
        val isNameValid: Boolean = true,
        val isNicknameValid: Boolean = true,
        val isEmailValid: Boolean = true,
    )
    sealed class ProfileEvent {
        data class EditProfile(val name: String, val nickname: String, val email: String) : ProfileEvent()
        data class OnNameChange(val name: String) : ProfileEvent()
        data class OnNicknameChange(val nickname: String) : ProfileEvent()
        data class OnEmailChange(val email: String) : ProfileEvent()
    }

}