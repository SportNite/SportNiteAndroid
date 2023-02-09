package com.pawlowski.notifications

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.pawlowski.domainutils.PagingKeyBasedFactory
import com.pawlowski.models.UserNotification
import com.pawlowski.network.data.IGraphQLService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class NotificationsRepository @Inject constructor(
    private val graphQLService: IGraphQLService,
    ): INotificationsRepository {

    override fun getPagedNotifications(): Flow<PagingData<UserNotification>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                PagingKeyBasedFactory(
                    request = { page, pageSize ->
                        graphQLService.getNotifications(
                            cursor = page,
                            pageSize = pageSize
                        )
                    }
                )
            }
        ).flow
    }

}