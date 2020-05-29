package com.gauvain.seigneur.domain

import com.gauvain.seigneur.domain.mock.OutComeModelMock
import com.gauvain.seigneur.domain.mock.ProviderModelMock
import com.gauvain.seigneur.domain.model.ErrorType
import com.gauvain.seigneur.domain.model.Outcome
import com.gauvain.seigneur.domain.model.RequestExceptionType
import com.gauvain.seigneur.domain.provider.GetAlbumTracksException
import com.gauvain.seigneur.domain.provider.GetAlbumTracksProvider
import com.gauvain.seigneur.domain.useCase.GetAlbumTracksUseCase
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.given
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.mockito.*

class GetAlbumTracksUseCaseImplTest {

    @Mock
    private lateinit var provider: GetAlbumTracksProvider
    private lateinit var useCase: GetAlbumTracksUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun `When we receive success from provider adapter must return OutCome Success`() {
        runBlockingTest {
            given(
                provider.getTracks(
                    0L
                )
            ).willReturn(ProviderModelMock.createTrackListModel())
            useCase = GetAlbumTracksUseCase.create(provider)
            val outcome = useCase.invoke(0L)
            assertThat(outcome).isNotNull()
            assertThat(outcome).isEqualTo(Outcome.Success(OutComeModelMock.createTrackListOutCome()))
        }
    }
}