package com.pawlowski.sportnite.fakes

import com.pawlowski.models.AdvanceLevel
import com.pawlowski.models.Sport
import com.pawlowski.models.User
import com.pawlowski.models.mappers.getSportFromSportId
import com.pawlowski.models.mappers.getUserForPreview
import com.pawlowski.models.params_models.UserUpdateInfoParams
import com.pawlowski.user.IUserRepository
import com.pawlowski.user.data.RegistrationProgress
import com.pawlowski.utils.Resource
import com.pawlowski.utils.UiData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

object FakeUserRepository: IUserRepository {
    override fun getUserSports(): Flow<UiData<List<Sport>>> = flow {
        emit(UiData.Success(
            isFresh = true,
            data = listOf(
                getSportFromSportId("TENNIS")
            )
        ))
    }

    override fun getInfoAboutMe(): Flow<User?> = flow {
        emit(getUserForPreview())
    }

    override suspend fun getUserRegistrationProgress(): Resource<RegistrationProgress> {
        TODO("Not yet implemented")
    }

    override suspend fun updateUserInfo(params: UserUpdateInfoParams): Resource<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun updateAdvanceLevelInfo(levels: Map<Sport, AdvanceLevel>): Resource<Unit> {
        TODO("Not yet implemented")
    }

    override fun signOut() {
        TODO("Not yet implemented")
    }

}