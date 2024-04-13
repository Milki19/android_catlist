package rs.raf.catlist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import rs.raf.catlist.core.theme.CatlistTheme
import rs.raf.catlist.navigation.AppNavigation

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
//            CatlistTheme {
//                AppNavigation()
//            }
            AppNavigation()
        }
    }
}

