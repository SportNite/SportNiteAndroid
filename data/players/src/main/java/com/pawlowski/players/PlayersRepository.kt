package com.pawlowski.players

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.StoreRequest
import com.pawlowski.auth.ILightAuthManager
import com.pawlowski.domainutils.PagingKeyBasedFactory
import com.pawlowski.domainutils.toUiData
import com.pawlowski.models.AdvanceLevel
import com.pawlowski.models.Player
import com.pawlowski.models.PlayerDetails
import com.pawlowski.models.Sport
import com.pawlowski.models.params_models.PlayersFilter
import com.pawlowski.network.data.IGraphQLService
import com.pawlowski.utils.UiData
import com.pawlowski.utils.filterIfSuccess
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class PlayersRepository @Inject constructor(
    private val authManager: ILightAuthManager,
    private val playersStore: Store<PlayersFilter, List<Player>>,
    private val playerDetailsStore: Store<String, PlayerDetails>,
    private val graphQLService: IGraphQLService,
): IPlayersRepository {

    override fun getPlayers(
        sportFilter: Sport?,
        nameSearch: String?,
        level: AdvanceLevel?
    ): Flow<UiData<List<Player>>> {
        val myUid = authManager.getCurrentUserUid()!!
        return playersStore.stream(
            StoreRequest.cached(
                key = PlayersFilter(
                    sportFilter = sportFilter,
                    nameSearch = nameSearch,
                    level = level
                ), refresh = true
            )
        ).toUiData(filterPredicateOnListData = {
            it.uid != myUid
        }, isDataEmpty = { it.isNullOrEmpty() })
    }

    override fun getPlayerDetails(playerUid: String): Flow<UiData<PlayerDetails>> {
        return playerDetailsStore.stream(
            StoreRequest.cached(
                key = playerUid,
                refresh = true
            )
        ).toUiData()
    }

    override fun getPagedPlayers(filters: PlayersFilter): Flow<PagingData<Player>> {
        val myUid = authManager.getCurrentUserUid()!!
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                PagingKeyBasedFactory(
                    request = { page, pageSize ->
                        graphQLService.getPlayers(
                            filters = filters,
                            cursor = page,
                            pageSize = pageSize
                        ).filterIfSuccess {
                            it.uid != myUid
                        }
                    }
                )
            }
        ).flow
    }
}