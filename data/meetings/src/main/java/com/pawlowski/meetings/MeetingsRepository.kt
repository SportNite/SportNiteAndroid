package com.pawlowski.meetings

import androidx.paging.PagingData
import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.StoreRequest
import com.pawlowski.domainutils.toUiData
import com.pawlowski.meetings.data.cache.MeetingsIntelligentInMemoryCache
import com.pawlowski.models.Meeting
import com.pawlowski.models.Sport
import com.pawlowski.models.params_models.MeetingsFilter
import com.pawlowski.utils.UiData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class MeetingsRepository @Inject constructor(
    private val meetingsStore: Store<MeetingsFilter, List<Meeting>>,
    private val meetingsInMemoryCache: MeetingsIntelligentInMemoryCache,
): IMeetingsRepository {

    override fun getMeetingDetails(meetingUid: String): Flow<UiData<Meeting>> = flow {
        //It's collected only from cache because it will be always there (meetings are always fetched before navigating to see their details)
        emit(UiData.Loading())
        meetingsInMemoryCache.observeData(key = null).map {
            it.first { meeting ->
                meeting.meetingUid == meetingUid
            }
        }.collect {
            emit(UiData.Success(isFresh = true, data = it))
        }
    }

    override fun getPagedMeetings(): Flow<PagingData<Meeting>> {
        TODO()
//        return Pager(
//            config = PagingConfig(
//                pageSize = 10,
//                enablePlaceholders = false
//            ),
//            pagingSourceFactory = {
//                PagingFactory(
//                    request = { page, pageSize ->
//                        executeApolloQuery(
//                            request = {
//                                apolloClient.query(
//                                    IncomingOffersQuery(
//                                        offersFilter = MeetingsFilter(null).toOfferFilterInput()
//                                    )
//                                ).execute()
//                            },
//                            mapper = {
//                                val data = it.incomingOffers.let { beforeMappingData ->
//                                    val userUid = authManager.getCurrentUserUid()!!
//                                    beforeMappingData.map {
//                                        it.toMeeting(userUid)
//                                    }
//                                }
//                                //val pageInfo = it.offers?.pageInfo!!
//                                PaginationPage(data = data, hasNextPage = pageInfo.hasNextPage, endCursor = /pageInfo.endCursor)
//                            }
//                        )
//
//                    }
//                )
//            }
//        ).flow
    }


    override fun getIncomingMeetings(sportFilter: Sport?): Flow<UiData<List<Meeting>>> {
        return meetingsStore.stream(
            StoreRequest.cached(
                key = MeetingsFilter(
                    sportFilter = sportFilter
                ), refresh = true
            )
        ).toUiData(isDataEmpty = { it.isNullOrEmpty() })
    }



}