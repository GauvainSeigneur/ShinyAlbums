package com.gauvain.seigneur.shinyalbums

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.gauvain.seigneur.presentation.UserAlbumsViewModel
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val viewModel : UserAlbumsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        buttonLol.setOnClickListener {
            viewModel.getAlbums()
        }
    }
}
