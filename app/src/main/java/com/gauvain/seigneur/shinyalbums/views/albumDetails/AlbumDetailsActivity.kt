package com.gauvain.seigneur.shinyalbums.views.albumDetails

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.gauvain.seigneur.shinyalbums.R

class AlbumDetailsActivity : AppCompatActivity() {

    companion object {
        fun newIntent(context: Context): Intent = Intent(context, AlbumDetailsActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_album_details)
    }
}
