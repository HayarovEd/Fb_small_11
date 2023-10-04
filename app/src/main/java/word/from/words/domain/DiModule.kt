package word.from.words.domain

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import word.from.words.data.KeeperImpl
import word.from.words.data.ServiceImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DiModule {
    @Binds
    @Singleton
    abstract fun bindService(serviceImpl: ServiceImpl): Service

    @Binds
    @Singleton
    abstract fun bindKeeper(keeperImpl: KeeperImpl): Keeper
}