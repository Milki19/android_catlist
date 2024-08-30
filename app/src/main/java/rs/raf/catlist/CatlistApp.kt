package rs.raf.catlist

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class CatlistApp  : Application() {

    override fun onCreate() {
        super.onCreate()
//        RmaDatabase.initDatabase(context = this)
    }

}