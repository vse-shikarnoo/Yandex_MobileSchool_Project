package yandex.school.project.expenses.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import yandex.school.project.core.ui.components.TopBar
import yandex.school.project.core.ui.components.TopBarState
import yandex.school.project.expenses.navigation.ExpensesNavGraph

class ExpensesSampleActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)


        //val expensesComponent = DaggerExpensesComponent.factory().create()
        //val viewModelFactory = expensesComponent.viewModelFactory()

        setContent {
            //CompositionLocalProvider(LocalExpensesViewModelFactory provides viewModelFactory) {
                Scaffold(
                    topBar = {
                        TopBar(
                            TopBarState(
                                title = "Expenses"
                            )
                        )
                    },
                    bottomBar = {}
                ) {
                    Box(modifier = Modifier.padding(it)) {
                        ExpensesNavGraph(accountId = 1) { }
                    }
                }
            }
        }
    //}
}