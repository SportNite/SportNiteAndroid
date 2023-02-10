package com.pawlowski.sportnite

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.pawlowski.auth.ILightAuthManager
import com.pawlowski.sportnite.presentation.ui.navigation.RootComposable
import com.pawlowski.sportnite.presentation.ui.theme.SportNiteTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    internal lateinit var lightAuthManager: ILightAuthManager

    override fun onCreate(savedInstanceState: Bundle?) {
        setActivityInstance(this)
        super.onCreate(savedInstanceState)
        setContent {
            SportNiteTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RootComposable(
                        isUserLoggedIn = { lightAuthManager.isUserAuthenticated() }
                    )
                }
            }
        }
    }

    companion object {
        private var mainActivity: MainActivity? = null
        private val LOCK = Any()
        fun getInstance(): MainActivity? = synchronized(LOCK) {
            mainActivity
        }
        private fun setActivityInstance(activity: MainActivity?) {
            synchronized(LOCK) {
                mainActivity = activity
            }
        }
    }


    override fun onResume() {
        super.onResume()
        setActivityInstance(this)
    }

    override fun onRestart() {
        super.onRestart()
        setActivityInstance(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        setActivityInstance(null)
    }
}



