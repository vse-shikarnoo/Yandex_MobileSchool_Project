package yandex.school.project.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import yandex.school.project.core.domain.entities.Account
import yandex.school.project.splash.di.LocalSplashViewModelFactory
import yandex.school.project.splash.di.SplashComponent

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
        if (logoAnimationState.isAtEnd && logoAnimationState.isPlaying) {
            accountChange(viewModel.account)
            goNextDestination()
        }
    }
}