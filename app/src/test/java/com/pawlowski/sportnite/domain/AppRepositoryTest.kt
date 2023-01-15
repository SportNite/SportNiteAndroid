@file:OptIn(ExperimentalCoroutinesApi::class)

package com.pawlowski.sportnite.domain

import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test

internal class AppRepositoryTest {

    @Test
    fun getIncomingMeetings() = runTest {
        val sut = AppRepository(
            userInfoUpdateCache = mockk(),
            authManager = mockk(),
            firebaseStoragePhotoUploader = mockk(),
            ioDispatcher = StandardTestDispatcher(scheduler = testScheduler, "ioDispatcher"),
            playersStore = mockk(),
            offersStore = mockk(),
            gameOffersToAcceptStore = mockk(),
            playerDetailsStore = mockk(),
            meetingsStore = mockk(),
            meetingsInMemoryCache = mockk(),
            offersInMemoryCache = mockk(),
            offersToAcceptMemoryCache = mockk(),
            graphQLService = mockk()
        )
        val result = sut.getIncomingMeetings(sportFilter = null)


    }

    @Test
    fun getUserSports() {
    }

    @Test
    fun getPlayers() {
    }

    @Test
    fun getGameOffers() {
    }

    @Test
    fun getMyGameOffers() {
    }

    @Test
    fun getOffersToAccept() {
    }

    @Test
    fun getSportObjects() {
    }

    @Test
    fun getPlayerDetails() {
    }

    @Test
    fun getMeetingDetails() {
    }

    @Test
    fun getInfoAboutMe() {
    }

    @Test
    fun addGameOffer() {
    }

    @Test
    fun sendOfferToAccept() {
    }

    @Test
    fun acceptOfferToAccept() {
    }

    @Test
    fun signOut() {
    }

    @Test
    fun updateUserInfo() {
    }

    @Test
    fun updateAdvanceLevelInfo() {
    }
}