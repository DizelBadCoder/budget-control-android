package dizel.budget_control.app

import android.app.Application
import dizel.budget_control.auth.di.authModule
import dizel.budget_control.budget.di.budgetsModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class App: Application() {
    override fun onCreate() {
        super.onCreate()

        initKoin()
        Timber.plant(Timber.DebugTree())
    }

    private fun initKoin() {
        val modules = listOf(
            authModule,
            budgetsModule
        )

        startKoin {
            androidContext(this@App)
            modules(modules)
        }
    }
}