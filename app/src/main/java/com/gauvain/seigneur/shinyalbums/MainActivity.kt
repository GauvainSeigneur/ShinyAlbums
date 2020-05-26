package com.gauvain.seigneur.shinyalbums

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gauvain.seigneur.presentation.model.LiveDataState
import com.gauvain.seigneur.presentation.model.LoadingState
import com.gauvain.seigneur.presentation.model.NextRequestState
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
            adapter.submitList(it)
        })

        viewModel.nextLoadingState?.observe(this, Observer {
            when(it) {
                is LiveDataState.Success -> {
                    when(it.data) {
                        LoadingState.IS_LOADED -> {
                            adapter.setNetworkState(NextRequestState.LOADED)
                        }
                        LoadingState.IS_LOADING -> {
                            adapter.setNetworkState(NextRequestState.LOADING)
                        }
                    }
                }
                is LiveDataState.Error -> {
                    adapter.setNetworkState(NextRequestState.error(it.errorData.description?.getFormattedString(this)))
                }
            }

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
