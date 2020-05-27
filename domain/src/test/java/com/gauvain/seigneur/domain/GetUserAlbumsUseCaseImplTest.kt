package com.gauvain.seigneur.domain

import com.gauvain.seigneur.domain.mock.OutComeModelMock
import com.gauvain.seigneur.domain.mock.ProviderModelMock
import com.gauvain.seigneur.domain.model.ErrorType
import com.gauvain.seigneur.domain.model.Outcome
import com.gauvain.seigneur.domain.model.RequestExceptionType
import com.gauvain.seigneur.domain.provider.GetUserAlbumsException
import com.gauvain.seigneur.domain.provider.GetUserAlbumsProvider
import com.gauvain.seigneur.domain.useCase.GetUserAlbumsUseCase
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.given
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.mockito.*

class GetUserAlbumsUseCaseImplTest {

    @Mock
    private lateinit var provider: GetUserAlbumsProvider
    private lateinit var useCase: GetUserAlbumsUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun `When we receive success from provider adapter must return OutCome Success`() {
        runBlockingTest {
            given(
                provider.getUserAlbums(
                    "userId",
                    0
                )
            ).willReturn(ProviderModelMock.createAlbumPaginedModel())
            useCase = GetUserAlbumsUseCase.create(provider)
            val outcome = useCase.invoke("userId", 0)
            assertThat(outcome).isNotNull()
            assertThat(outcome).isEqualTo(Outcome.Success(OutComeModelMock.createPaginedAlbumsOutCome()))
        }
    }

    @Test
    fun `When we receive ERROR UNKNOWN from provider adapter must return OutCome Error`() {
        runBlockingTest {
            given(provider.getUserAlbums("userId", 0)).willThrow(
                GetUserAlbumsException(RequestExceptionType.UNKNOWN_ERROR)
            )
            useCase = GetUserAlbumsUseCase.create(provider)
            val outcome = useCase.invoke("userId", 0)
            assertThat(outcome).isNotNull()
            assertThat(outcome).isEqualTo(Outcome.Error(ErrorType.ERROR_UNKNOWN))
        }
    }

    @Test
    fun `When we receive ERROR BODY NULL from provider adapter must return OutCome Error`() {
        runBlockingTest {
            given(provider.getUserAlbums("userId", 0)).willThrow(
                GetUserAlbumsException(RequestExceptionType.BODY_NULL)
            )
            useCase = GetUserAlbumsUseCase.create(provider)
            val outcome = useCase.invoke("userId", 0)
            assertThat(outcome).isNotNull()
            assertThat(outcome).isEqualTo(Outcome.Error(ErrorType.ERROR_BODY))
        }
    }

    @Test
    fun `When we receive ERROR FORMATTED from provider adapter must return OutCome Error`() {
        runBlockingTest {
            given(provider.getUserAlbums("userId", 0)).willThrow(
                GetUserAlbumsException(RequestExceptionType.FORMATTED_ERROR)
            )
            useCase = GetUserAlbumsUseCase.create(provider)
            val outcome = useCase.invoke("userId", 0)
            assertThat(outcome).isNotNull()
            assertThat(outcome).isEqualTo(Outcome.Error(ErrorType.ERROR_FORMATTED))
        }
    }

}