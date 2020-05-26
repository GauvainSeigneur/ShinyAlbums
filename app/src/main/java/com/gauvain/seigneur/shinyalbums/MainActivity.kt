package com.gauvain.seigneur.shinyalbums

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gauvain.seigneur.presentation.viewModel.UserAlbumsViewModel
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(), UserAlbumListAdapter.Listener {

    private val viewModel : UserAlbumsViewModel by viewModel()
    private lateinit var adapter: UserAlbumListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initAdapter()
        viewModel.fetchAlbums()

        viewModel.albumList?.observe(this, Observer {
            Log.d("albumList", "changed $it")
            adapter.submitList(it)
        })
    }

    override fun onClick(id: String) {

    }

    private fun initAdapter() {
        adapter = UserAlbumListAdapter(this)
        userAlbumsRecyclerView.layoutManager = LinearLayoutManager(
            this, RecyclerView.VERTICAL,
            false
        )
        userAlbumsRecyclerView.addItemDecoration(
            DividerItemDecoration(
                this,
                LinearLayoutManager.VERTICAL
            )
        )
        userAlbumsRecyclerView.adapter = adapter
    }

}
