package com.brainx.datasource.local_pref


import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

internal class DatastorePreferenceManager (
    private val datastorePreference: DataStore<Preferences>
){
    private enum class SPKeys(val key:String){
        IS_LOGIN("is_login"),
        IS_FIRST_TIME_INSTALLED("is_first_time_installed"),
        ACCESS_TOKEN("access_token"),
        REFRESH_TOKEN("refresh_token")
    }
    
    private companion object{
        private val IS_LOGIN_PREF_KEY = booleanPreferencesKey(SPKeys.IS_LOGIN.key)
        private val IS_FIRST_TIME_INSTALLED_PREF_KEY = booleanPreferencesKey(SPKeys.IS_FIRST_TIME_INSTALLED.key)
        private val ACCESS_TOKEN_PREF_KEY = stringPreferencesKey(SPKeys.ACCESS_TOKEN.key)
        private val REFRESH_TOKEN_PREF_KEY = stringPreferencesKey(SPKeys.REFRESH_TOKEN.key)
    }

    suspend fun setIsLogin(value:Boolean) : Boolean{
        return try {
            datastorePreference.edit { preferences ->
                preferences[IS_LOGIN_PREF_KEY] = value
            }.apply { }
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun getIsLogin() =  datastorePreference.data.map { preferences->
        preferences[IS_LOGIN_PREF_KEY]
    }.first() ?: false

    suspend fun getIsFirstTimeInstalled() =  datastorePreference.data.map { preferences->
             preferences[IS_FIRST_TIME_INSTALLED_PREF_KEY]
    }.first() ?: false

    suspend fun setIsFirstTimeInstalled(value:Boolean) : Boolean{
        return try {
            datastorePreference.edit { preferences ->
                preferences[IS_FIRST_TIME_INSTALLED_PREF_KEY] = value
            }.apply { }
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun getAccessToken(): String? {
        return datastorePreference.data.map { preferences ->
            preferences[ACCESS_TOKEN_PREF_KEY]
        }.first()
    }

    suspend fun setAccessToken(value: String?): Boolean {
        return try {
            datastorePreference.edit { preferences ->
                if (value.isNullOrBlank()) preferences.remove(ACCESS_TOKEN_PREF_KEY)
                else preferences[ACCESS_TOKEN_PREF_KEY] = value
            }
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun getRefreshToken(): String? {
        return datastorePreference.data.map { preferences ->
            preferences[REFRESH_TOKEN_PREF_KEY]
        }.first()
    }

    suspend fun setRefreshToken(value: String?): Boolean {
        return try {
            datastorePreference.edit { preferences ->
                if (value.isNullOrBlank()) preferences.remove(REFRESH_TOKEN_PREF_KEY)
                else preferences[REFRESH_TOKEN_PREF_KEY] = value
            }
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun clearSession() {
        datastorePreference.edit { preferences -> 
            preferences.remove(IS_LOGIN_PREF_KEY)
            preferences.remove(ACCESS_TOKEN_PREF_KEY)
            preferences.remove(REFRESH_TOKEN_PREF_KEY)
        }.apply { }
    }
}