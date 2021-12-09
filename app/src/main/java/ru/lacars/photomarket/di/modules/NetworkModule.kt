package ru.lacars.photomarket.di.modules

import android.app.Application
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import ru.lacars.photomarket.BuildConfig
import ru.lacars.photomarket.R
import ru.lacars.photomarket.data.network.APIService
import ru.lacars.photomarket.data.network.AuthInterceptor
import javax.inject.Singleton

@Module
object NetworkModule {

    @Singleton
    @Provides
    fun provideApiService(): APIService {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        val httpClient = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(BuildConfig.PHOTO_API_KEY))
            .addInterceptor(logging)
            // .addNetworkInterceptor()
            .build()

        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://photo-api.webzap.ru")
            .client(httpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        return retrofit.create(APIService::class.java)
    }

    @Singleton
    @Provides
    fun provideImageLoader(application: Application): Glide {
        return Glide.get(application)
    }

    @Singleton
    @Provides
    fun provideGlideInstance(
        application: Application
    ) = Glide.with(application.baseContext).setDefaultRequestOptions(
        RequestOptions()
            .transform(RoundedCorners(50))
            .skipMemoryCache(true)
            .placeholder(R.drawable.ic_image)
            .error(R.drawable.ic_image)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
    )
}
