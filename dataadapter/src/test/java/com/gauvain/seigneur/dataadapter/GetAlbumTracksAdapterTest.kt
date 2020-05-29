package com.gauvain.seigneur.dataadapter

import com.gauvain.seigneur.dataadapter.adapter.GetAlbumTracksAdapter
import com.gauvain.seigneur.dataadapter.mock.AdapterOutcomeModelMock
import com.gauvain.seigneur.dataadapter.mock.DeezerServiceMock
import com.gauvain.seigneur.dataadapter.mock.ResponseGsonObjectMock
import com.gauvain.seigneur.dataadapter.service.DeezerService
import com.gauvain.seigneur.domain.model.RequestExceptionType
import com.gauvain.seigneur.domain.provider.GetAlbumTracksException
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.MockitoAnnotations
import java.lang.Exception

class GetAlbumTracksAdapterTest {

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun `When we receive object from provider it must return a list of tracks`() {
        val serviceSuccessResponse: DeezerService =
            DeezerServiceMock.createServiceWithResponses(ResponseGsonObjectMock.createAlbumTracksSuccessResponse())
        val provider = GetAlbumTracksAdapter(serviceSuccessResponse)
        val result = runCatching {
            provider.getTracks(0L)
        }
        assertThat(result.isSuccess).isNotNull()
        assertThat(result.getOrNull()).isEqualTo(AdapterOutcomeModelMock.createSuccessTrackListModel())
    }

    @Test
    fun `When we receive only a message from provider it must return GetUserAlbumsException`() {
        val messageResponse: DeezerService =
            DeezerServiceMock.createServiceWithResponses(ResponseGsonObjectMock.createTrackListErrorResponse())
        val provider = GetAlbumTracksAdapter(messageResponse)
        val result = runCatching {
            provider.getTracks(0L)
        }
        assertThat(result.exceptionOrNull()).isNotNull()
        assertThat(result.exceptionOrNull()).isInstanceOf(GetAlbumTracksException::class.java)
        assertThat((result.exceptionOrNull() as GetAlbumTracksException).type).isEqualTo(
            RequestExceptionType.FORMATTED_ERROR
        )
        assertThat((result.exceptionOrNull() as GetAlbumTracksException).description).isEqualTo(
            "no data")
    }

    @Test
    fun `When we receive null object from provider it must return GetUserAlbumsException`() {
        val messageResponse: DeezerService =
            DeezerServiceMock.createServiceWithResponses(ResponseGsonObjectMock.createTrackListNullBodyResponse())
        val provider = GetAlbumTracksAdapter(messageResponse)
        val result = runCatching {
            provider.getTracks(0L)
        }
        assertThat(result.exceptionOrNull()).isNotNull()
        assertThat(result.exceptionOrNull()).isInstanceOf(GetAlbumTracksException::class.java)
        assertThat((result.exceptionOrNull() as GetAlbumTracksException).type).isEqualTo(
            RequestExceptionType.BODY_NULL
        )
        assertThat((result.exceptionOrNull() as GetAlbumTracksException).description).isEqualTo(
            "Body null")
    }

    @Test
    fun `When we receive EXCEPTION from provider it must return GetUserAlbumsException`() {
        val serviceFailedResponse: DeezerService =
            DeezerServiceMock.createServiceThatFail(Exception("fake exception"))
        val provider = GetAlbumTracksAdapter(serviceFailedResponse)
        val result = runCatching {
            provider.getTracks(0L)
        }
        assertThat(result.exceptionOrNull()).isNotNull()
        assertThat(result.exceptionOrNull()).isInstanceOf(GetAlbumTracksException::class.java)
        assertThat((result.exceptionOrNull() as GetAlbumTracksException).type).isEqualTo(
            RequestExceptionType.UNKNOWN_ERROR
        )
        assertThat((result.exceptionOrNull() as GetAlbumTracksException).description).isEqualTo(
            "java.lang.Exception: fake exception"
        )
    }
}