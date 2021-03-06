package com.example.ba_1963.android_searchproduct_mvp.dagger

import com.example.ba_1963.android_searchproduct_mvp.BuildConfig
import com.example.ba_1963.android_searchproduct_mvp.api.SearchApi
import com.example.ba_1963.android_searchproduct_mvp.data.SearchRepository
import com.example.ba_1963.android_searchproduct_mvp.data.SearchRepositoryInterface
import com.example.ba_1963.android_searchproduct_mvp.util.Constants
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val client = OkHttpClient().newBuilder()
        client.addInterceptor(HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        })
        return client.build()
    }

    @Provides
    @Singleton
    fun provideRetrofit() : Retrofit {
        return retrofit2.Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(Constants.BASE_URL)
                .client(provideOkHttpClient())
                .build()
    }

    @Provides
    @Singleton
    fun provideSearchApi() : SearchApi {
        return provideRetrofit().create(SearchApi::class.java)
    }

    @Provides
    @Singleton
    fun provideSearchRepository(): SearchRepositoryInterface {
        return SearchRepository(provideSearchApi())
    }
}