package com.example.onetoone.di

import android.content.Context
import com.example.onetoone.chatRoomScreen.ChatRoomViewmodel
import com.example.onetoone.dataStoreforSaveUserPref.UserDataPref
import com.example.onetoone.homeScreen.HomeViewmodel
import com.example.onetoone.loginScreen.LoginViewmodel
import com.example.onetoone.profileScreen.ProfileViewmodel
import com.example.onetoone.registrationScreen.RegistrationViewmodel
import com.example.onetoone.repositary.Repository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class FirstActivitymodule {


    @Singleton
    @Provides
    fun getUserDataPre(@ApplicationContext context: Context) : UserDataPref {
        return UserDataPref(context)
    }

    @Singleton
    @Provides
    fun profileViewmodel(userDataPref: UserDataPref): ProfileViewmodel{
        return ProfileViewmodel(userDataPref)
    }

    @Singleton
    @Provides
    fun loginViewmdelInstance(repository: Repository,userDataPref: UserDataPref): LoginViewmodel{
        return LoginViewmodel(repository,userDataPref)
    }

    @Singleton
    @Provides
    fun registerViewmodelInstance(repository: Repository,userDataPref: UserDataPref) : RegistrationViewmodel{
        return RegistrationViewmodel(repository,userDataPref)
    }

    @Singleton
    @Provides
    fun homeViewmodelInstance(repository: Repository): HomeViewmodel{
        return HomeViewmodel(repository)
    }

    @Singleton
    @Provides
    fun chatRoomViewmodel(repository: Repository,userDataPref: UserDataPref): ChatRoomViewmodel{
        return ChatRoomViewmodel(repository,userDataPref)
    }

    @Singleton
    @Provides
    fun getRepositoryInstance(@ApplicationContext context: Context,auth: FirebaseAuth,database: FirebaseDatabase,userDataPref: UserDataPref): Repository{
        return Repository(context, auth,database,userDataPref)
    }

    @Singleton
    @Provides
    fun authFirebaseInstance() : FirebaseAuth{
        return FirebaseAuth.getInstance()
    }

    @Singleton
    @Provides
    fun firebaseDatabaseInstance(): FirebaseDatabase{
        return FirebaseDatabase.getInstance()
    }
}