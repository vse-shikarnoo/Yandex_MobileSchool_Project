package yandex.school.project.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import yandex.school.project.core.domain.entities.Account
import yandex.school.project.core.ui.components.PinEnterScreen
import yandex.school.project.core.utils.PinCodeStorage
import yandex.school.project.core.utils.Result
import yandex.school.project.splash.di.LocalSplashViewModelFactory
import yandex.school.project.splash.di.SplashComponent
import androidx.compose.foundation.layout.Arrangement

@Composable
fun ProvidedSplashScreen(
    splashComponent: SplashComponent,
    goNextDestination: () -> Unit,
    accountChange: (Account?) -> Unit
) {
    val viewModelFactory = remember { splashComponent.viewModelFactory() }
    CompositionLocalProvider(LocalSplashViewModelFactory provides viewModelFactory) {
        SplashScreen(
            goNextDestination, accountChange
        )
    }
}

@Composable
internal fun SplashScreen(
    goNextDestination: () -> Unit,
    accountChange: (Account?) -> Unit
) {
    val factory = LocalSplashViewModelFactory.current
    val viewModel: SplashViewModel = viewModel(factory = factory)
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    var showPinEnter by remember { mutableStateOf(false) }
    var pinError by remember { mutableStateOf<String?>(null) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.splash))
        val logoAnimationState =
            animateLottieCompositionAsState(composition = composition)
        LottieAnimation(
            composition = composition,
            progress = { logoAnimationState.progress }
        )
        if (logoAnimationState.isAtEnd && logoAnimationState.isPlaying && !showPinEnter) {
            val account = (uiState as? Result.Success)?.data
            accountChange(account)
            if (PinCodeStorage.hasPin(context)) {
                showPinEnter = true
            } else {
                goNextDestination()
            }
        }
        if (showPinEnter) {
            Dialog(onDismissRequest = { /* не даём закрыть по клику вне */ }) {
                androidx.compose.material3.Surface(
                    shape = RoundedCornerShape(28.dp),
                    tonalElevation = 8.dp,
                    color = MaterialTheme.colorScheme.background,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                ) {
                    PinEnterScreen(
                        onPinEntered = { pin ->
                            if (PinCodeStorage.checkPin(context, pin)) {
                                pinError = null
                                showPinEnter = false
                                goNextDestination()
                            } else {
                                pinError = "Неверный PIN-код"
                            }
                        },
                        errorMessage = pinError
                    )

                }
            }
        }
    }
}