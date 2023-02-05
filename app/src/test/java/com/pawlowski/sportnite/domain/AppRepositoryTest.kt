
package com.pawlowski.sportnite.domain

import com.dropbox.android.external.store4.Store
import com.pawlowski.models.*
import com.pawlowski.sportnite.data.auth.IAuthManager
import com.pawlowski.sportnite.data.auth.UserInfoUpdateCache
import com.pawlowski.sportnite.data.firebase_storage.FirebaseStoragePhotoUploader
import com.pawlowski.sportnite.data.local.MeetingsInMemoryCache
import com.pawlowski.sportnite.data.local.OffersInMemoryCache
import com.pawlowski.sportnite.data.local.OffersToAcceptMemoryCache
import com.pawlowski.network.data.IGraphQLService
import com.pawlowski.models.params_models.MeetingsFilter
import com.pawlowski.models.params_models.OffersFilter
import com.pawlowski.models.params_models.PlayersFilter
import com.pawlowski.sportnite.presentation.models.*
import com.pawlowski.utils.Resource
import com.pawlowski.utils.onError
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class AppRepositoryTest {


    @RelaxedMockK
    private lateinit var userInfoUpdateCacheMock: UserInfoUpdateCache
    @RelaxedMockK
    private lateinit var authManager: IAuthManager
    @RelaxedMockK
    private lateinit var firebaseStoragePhotoUploader: FirebaseStoragePhotoUploader
    @RelaxedMockK
    private lateinit var graphQLService: IGraphQLService
    @RelaxedMockK
    private lateinit var playersStore: Store<PlayersFilter, List<Player>>
    @RelaxedMockK
    private lateinit var offersStore: Store<OffersFilter, List<GameOffer>>
    @RelaxedMockK
    private lateinit var gameOffersToAcceptStore: Store<OffersFilter, List<GameOfferToAccept>>
    @RelaxedMockK
    private lateinit var playerDetailsStore: Store<String, PlayerDetails>
    @RelaxedMockK
    private lateinit var meetingsStore: Store<MeetingsFilter, List<Meeting>>
    @RelaxedMockK
    private lateinit var meetingsInMemoryCache: MeetingsInMemoryCache
    @RelaxedMockK
    private lateinit var offersInMemoryCache: OffersInMemoryCache
    @RelaxedMockK
    private lateinit var offersToAcceptMemoryCache: OffersToAcceptMemoryCache

    @Before
    fun setUp() = MockKAnnotations.init(this, relaxUnitFun = true) // turn relaxUnitFun on for all mocks


    private fun getSUT(testScheduler: TestCoroutineScheduler): AppRepository {
        return AppRepository(
            userInfoUpdateCache = userInfoUpdateCacheMock,
            authManager = authManager,
            firebaseStoragePhotoUploader = firebaseStoragePhotoUploader,
            ioDispatcher = StandardTestDispatcher(scheduler = testScheduler, "ioDispatcher"),
            playersStore = playersStore,
            offersStore = offersStore,
            gameOffersToAcceptStore = gameOffersToAcceptStore,
            playerDetailsStore = playerDetailsStore,
            meetingsStore = meetingsStore,
            meetingsInMemoryCache = meetingsInMemoryCache,
            offersInMemoryCache = offersInMemoryCache,
            offersToAcceptMemoryCache = offersToAcceptMemoryCache,
            graphQLService = graphQLService
        )
    }

    @Test
    fun getIncomingMeetings() = runTest {

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
    fun sendOfferToAccept() = runTest {
        val sut = getSUT(testScheduler)
        coEvery {
            graphQLService.sendOfferToAccept(any())
        } returns Resource.Success("id")

        val offerId = "offerId"
        val result = sut.sendOfferToAccept(offerUid = offerId)

        result.onError { _, _ -> assert(false) }
        coVerify {
            graphQLService.sendOfferToAccept(offerUid = offerId)
        }

        coVerify {
            offersInMemoryCache.updateElements(any())
        }
    }

    @Test
    fun acceptOfferToAccept() = runTest {
        //TODO: find out how to test Store
//        val sut = getSUT(testScheduler)
//        coEvery {
//            graphQLService.acceptOfferToAccept(any())
//        } returns Resource.Success(Unit)
//
//        coEvery {
//            meetingsStore.fresh(any())
//        } returns listOf()
//
//        val offerToAcceptId = "offerToAcceptId"
//        val result = sut.acceptOfferToAccept(offerToAcceptUid = offerToAcceptId)
//
//        result.onError { _, _ -> assert(false) }
//        coVerify {
//            graphQLService.sendOfferToAccept(offerUid = offerToAcceptId)
//        }
//
//
//        coVerify {
//            offersInMemoryCache.deleteElementFromAllKeys(any())
//        }
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