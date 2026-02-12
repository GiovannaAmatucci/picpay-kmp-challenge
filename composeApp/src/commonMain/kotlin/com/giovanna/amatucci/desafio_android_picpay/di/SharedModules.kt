package com.giovanna.amatucci.desafio_android_picpay.di

import com.giovanna.amatucci.desafio_android_picpay.Platform
import com.giovanna.amatucci.desafio_android_picpay.data.local.db.AppDatabase
import com.giovanna.amatucci.desafio_android_picpay.data.remote.api.PicPayApi
import com.giovanna.amatucci.desafio_android_picpay.data.remote.api.PicPayApiImpl
import com.giovanna.amatucci.desafio_android_picpay.data.remote.network.PicPayHttpClient
import com.giovanna.amatucci.desafio_android_picpay.data.repository.ContactsRepositoryImpl
import com.giovanna.amatucci.desafio_android_picpay.domain.repository.ContactsRepository
import com.giovanna.amatucci.desafio_android_picpay.domain.usecase.FetchContactsUseCase
import com.giovanna.amatucci.desafio_android_picpay.domain.usecase.FetchContactsUseCaseImpl
import com.giovanna.amatucci.desafio_android_picpay.getPlatform
import com.giovanna.amatucci.desafio_android_picpay.presentation.feature.ContactsViewModel
import io.ktor.client.HttpClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val networkModule = module {
    single<HttpClient> {
        PicPayHttpClient(config = get(), logWriter = get()).invoke()
    }
    single<PicPayApi> { PicPayApiImpl(get(), get()) }
}
val domainModule = module {
    single<ContactsRepository> { ContactsRepositoryImpl(get(), get(), get(), get(), get()) }
    factory<FetchContactsUseCase> { FetchContactsUseCaseImpl(get()) }
    single { get<AppDatabase>().contactUserDao() }
}
val viewModelModule = module {
    viewModelOf(::ContactsViewModel)
}
val coreModule = module {
    single { Dispatchers.IO }
    single<Platform> { getPlatform() }
}
val sharedModules = listOf(networkModule, domainModule, viewModelModule, coreModule)