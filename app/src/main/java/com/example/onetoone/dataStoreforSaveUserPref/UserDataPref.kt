package com.example.onetoone.dataStoreforSaveUserPref


import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

val Context.dataStore by preferencesDataStore(name = "userData")

object UserPreferenceKeys{
    val isLogin = booleanPreferencesKey("isLogin")
    val USER_ID = stringPreferencesKey("user_id")
    val USER_NAME = stringPreferencesKey("user_name")
    val USER_EMAIL = stringPreferencesKey("user_email")
    val USER_PHONE = stringPreferencesKey("user_password")
}

class UserDataPref @Inject constructor(private val context: Context) {

    val isLoginFlow: Flow<Boolean?> = context.dataStore.data
        .map { preferences ->
            preferences[UserPreferenceKeys.isLogin] ?: false
        }
    val userIDFlow: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[UserPreferenceKeys.USER_ID] ?: "default"
        }
    val userNameFlow: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[UserPreferenceKeys.USER_NAME]
        }
    val userEmailFlow: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[UserPreferenceKeys.USER_EMAIL]
        }
    val userPhoneFlow: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[UserPreferenceKeys.USER_PHONE]
        }


    suspend fun saveIslogin(isLogin: Boolean){
        context.dataStore.edit { preferences ->
            preferences[UserPreferenceKeys.isLogin] = isLogin
        }
    }
    suspend fun saveUserId(userID: String) {
        context.dataStore.edit { preferences ->
            preferences[UserPreferenceKeys.USER_ID] = userID
        }
    }
    suspend fun saveUsername(username: String) {
        context.dataStore.edit { preferences ->
            preferences[UserPreferenceKeys.USER_NAME] = username
        }
    }
    suspend fun saveUserEmail(userEmail: String) {
        context.dataStore.edit { preferences ->
            preferences[UserPreferenceKeys.USER_EMAIL] = userEmail
        }
    }
    suspend fun saveUserPhone(userPhone: String) {
        context.dataStore.edit { preferences ->
            preferences[UserPreferenceKeys.USER_PHONE] = userPhone
        }
    }

}