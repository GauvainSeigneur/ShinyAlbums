package com.gauvain.seigneur.shinyalbums.views.splash

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.gauvain.seigneur.shinyalbums.views.userAlbums.UserAlbumsActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(UserAlbumsActivity.newIntent(this))
        finish()
    }
}
