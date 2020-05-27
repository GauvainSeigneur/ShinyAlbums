package com.gauvain.seigneur.presentation.model

import com.gauvain.seigneur.domain.model.AlbumModel

data class AlbumItemData(
   val id: Long,
   val cover: String,
   val title: String,
   val artistName: String,
   val isExplicitLyrics: Boolean
)


fun AlbumModel.toItemData() : AlbumItemData = AlbumItemData(
   id = this.id,
   title = this.title,
   artistName = this.artist.name,
   isExplicitLyrics = this.explicitLyrics,
   cover = this.cover
)


