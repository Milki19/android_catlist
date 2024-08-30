package rs.raf.catlist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import rs.raf.catlist.auth.AuthStore
import rs.raf.catlist.core.theme.CatlistTheme
import rs.raf.catlist.navigation.BreedNavigation
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var authStore: AuthStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

//        val app = (applicationContext as RmaApp)


        setContent {
            CatlistTheme {

                BreedNavigation(authStore)
            }
        }
    }
}

