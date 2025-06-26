package yandex.school.project.presentation.screens.splash

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import yandex.school.project.domain.entities.Account
import yandex.school.project.domain.usecases.account.GetFirstAccountUseCase
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val getFirstAccountUseCase: GetFirstAccountUseCase
) : ViewModel() {

    var accountId: Int = 1
        private set

    init {
        viewModelScope.launch {
            accountId = try {
                getFirstAccountUseCase()!!.id
            } catch (e: Exception) {
                1
            }
        }
    }
} 