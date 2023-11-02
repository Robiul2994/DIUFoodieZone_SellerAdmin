package com.diu.mlab.foodie.admin.di

import android.content.Context
import com.diu.mlab.foodie.admin.data.data_source.NotificationApi
import com.diu.mlab.foodie.admin.data.repo.AdminRepoImpl
import com.diu.mlab.foodie.admin.data.repo.AuthRepoImpl
import com.diu.mlab.foodie.admin.data.repo.SellerRepoImpl
import com.diu.mlab.foodie.admin.domain.repo.AdminRepo
import com.diu.mlab.foodie.admin.domain.repo.AuthRepo
import com.diu.mlab.foodie.admin.domain.repo.SellerRepo
import com.diu.mlab.foodie.admin.domain.use_cases.*
import com.diu.mlab.foodie.admin.domain.use_cases.admin.*
import com.diu.mlab.foodie.admin.domain.use_cases.seller.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth() = Firebase.auth


    @Provides
    @Singleton
    fun provideFirebaseDatabase() = Firebase.database

    @Provides
    @Singleton
    fun provideFirebaseFirestore() = Firebase.firestore

    @Provides
    @Singleton
    fun provideFirebaseStorage() = Firebase.storage

    @Provides
    @Singleton
    fun provideAdminRepo(firestore: FirebaseFirestore, realtime: FirebaseDatabase, storage: FirebaseStorage, @ApplicationContext context: Context) : AdminRepo = AdminRepoImpl(firestore, realtime, storage, context)

    @Provides
    @Singleton
    fun provideAuthRepo(firebaseAuth: FirebaseAuth,firestore: FirebaseFirestore, storage: FirebaseStorage, @ApplicationContext context: Context): AuthRepo =
        AuthRepoImpl(firebaseAuth,firestore, storage, context)

    @Provides
    @Singleton
    fun provideSellerRepo(realtime: FirebaseDatabase,
                          firestore: FirebaseFirestore,
                          storage: FirebaseStorage,
                          @ApplicationContext context: Context,
                          api: NotificationApi): SellerRepo =
        SellerRepoImpl(realtime, firestore, storage, context, api)

    @Provides
    @Singleton
    fun providePortalApi(): NotificationApi {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client: OkHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()

        return Retrofit.Builder()
            .baseUrl(NotificationApi.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NotificationApi::class.java)
    }
//    =
//        Retrofit.Builder()
//            .baseUrl(NotificationApi.BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//            .create(NotificationApi::class.java)

//    @Provides
//    @Singleton
//    fun provideAdminUseCases(repo: AdminRepo) =
//        AdminUseCases(
//            GetMyProfile(repo),
//            GetSuperUserList(repo),
//            ChangeSuperUserStatus(repo)
//    )

//    @Provides
//    @Singleton
//    fun provideAuthUseCases(repo: AuthRepo) =
//        AuthUseCases(
//            FirebaseLogin(repo),
//            FirebaseSignup(repo),
//            GoogleSignIn(repo)
//        )

//    @Provides
//    @Singleton
//    fun provideSellerUseCase(repo: SellerRepo) =
//        SellerUseCase(
//            AddFood(repo),
//            GetFoodList(repo),
//            GetShopProfile(repo),
//            RemoveFood(repo),
//            UpdateFood(repo),
//            UpdateShopProfile(repo)
//
//        )




}