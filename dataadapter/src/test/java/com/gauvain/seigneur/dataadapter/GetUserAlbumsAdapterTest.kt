package com.gauvain.seigneur.dataadapter

import com.gauvain.seigneur.dataadapter.adapter.GetUserAlbumsAdapter
import com.gauvain.seigneur.dataadapter.mock.AdapterOutcomeModelMock
import com.gauvain.seigneur.dataadapter.mock.DeezerServiceMock
import com.gauvain.seigneur.dataadapter.mock.ResponseGsonObjectMock
import com.gauvain.seigneur.dataadapter.service.DeezerService
import com.gauvain.seigneur.domain.model.RequestExceptionType
import com.gauvain.seigneur.domain.provider.GetUserAlbumsException
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.mockito.MockitoAnnotations
import java.lang.Exception

class GetUserAlbumsAdapterTest {

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun `When we receive object from provider it must return a list of albums`() {
        runBlockingTest {
            val serviceSuccessResponse: DeezerService =
                DeezerServiceMock.createServiceWithResponses(ResponseGsonObjectMock.createSuccessAlbumResponse())
            val provider = GetUserAlbumsAdapter(serviceSuccessResponse)
            val result = runCatching {
                provider.getUserAlbums("userId", 0)
            }
            assertThat(result.isSuccess).isNotNull()
            assertThat(result.getOrNull()).isEqualTo(AdapterOutcomeModelMock.createSuccessAlbumPaginedModel())
        }
    }

    @Test
    fun `When we receive only a message from provider it must return GetUserAlbumsException`() {
        runBlockingTest {
            val messageResponse: DeezerService =
                DeezerServiceMock.createServiceWithResponses(ResponseGsonObjectMock.createErrorResponse())
            val provider = GetUserAlbumsAdapter(messageResponse)
            val result = runCatching {
                provider.getUserAlbums("userId", 0)
            }
            assertThat(result.exceptionOrNull()).isNotNull()
            assertThat(result.exceptionOrNull()).isInstanceOf(GetUserAlbumsException::class.java)
            assertThat((result.exceptionOrNull() as GetUserAlbumsException).type).isEqualTo(
                RequestExceptionType.FORMATTED_ERROR
            )
            assertThat((result.exceptionOrNull() as GetUserAlbumsException).description).isEqualTo(
                "no data"
            )
        }
    }

    @Test
    fun `When we receive null object from provider it must return GetUserAlbumsException`() {
        runBlockingTest {
            val messageResponse: DeezerService =
                DeezerServiceMock.createServiceWithResponses(ResponseGsonObjectMock.createNullBodyResponse())
            val provider = GetUserAlbumsAdapter(messageResponse)
            val result = runCatching {
                provider.getUserAlbums("userId", 0)
            }
            assertThat(result.exceptionOrNull()).isNotNull()
            assertThat(result.exceptionOrNull()).isInstanceOf(GetUserAlbumsException::class.java)
            assertThat((result.exceptionOrNull() as GetUserAlbumsException).type).isEqualTo(
                RequestExceptionType.BODY_NULL
            )
            assertThat((result.exceptionOrNull() as GetUserAlbumsException).description).isEqualTo(
                "Body null"
            )
        }
    }

    @Test
    fun `When we receive EXCEPTION from provider it must return GetUserAlbumsException`() {
        runBlockingTest {
            val serviceFailedResponse: DeezerService =
                DeezerServiceMock.createServiceThatFail(Exception("fake exception"))
            val provider = GetUserAlbumsAdapter(serviceFailedResponse)
            val result = runCatching {
                provider.getUserAlbums("userId", 0)
            }
            assertThat(result.exceptionOrNull()).isNotNull()
            assertThat(result.exceptionOrNull()).isInstanceOf(GetUserAlbumsException::class.java)
            assertThat((result.exceptionOrNull() as GetUserAlbumsException).type).isEqualTo(
                RequestExceptionType.UNKNOWN_ERROR
            )
            assertThat((result.exceptionOrNull() as GetUserAlbumsException).description).isEqualTo(
                "java.lang.Exception: fake exception"
            )
        }
    }
}