package com.example.onetoone.di

import android.content.Context
import com.example.onetoone.loginScreen.LoginViewmodel
import com.example.onetoone.registrationScreen.RegistrationViewmodel
import com.example.onetoone.repositary.Repository
import com.google.firebase.auth.FirebaseAuth
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
    fun loginViewmdelInstance(repository: Repository): LoginViewmodel{
        return LoginViewmodel(repository)
    }

    @Singleton
    @Provides
    fun registerViewmodelInstance(repository: Repository) : RegistrationViewmodel{
        return RegistrationViewmodel(repository)
    }

    @Singleton
    @Provides
    fun getRepositoryInstance(@ApplicationContext context: Context,auth: FirebaseAuth): Repository{
        return Repository(context, auth)
    }

    @Singleton
    @Provides
    fun authFirebaseInstance() : FirebaseAuth{
        return FirebaseAuth.getInstance()
    }
}