package com.brainx.datasource

sealed interface AppConfig {
    data object Datastore : AppConfig{

        /*
        * Note:
        * File name extension should be preferences_pb if you don’t follow format extension you will got this issue
        *
        * " java.lang.IllegalStateException: File extension for file: /data/user/0/com.kaito.kmoney/files/app.preference
        *  does not match required extension for Preferences file: preferences_pb"
        *
        * */

        internal const val DATA_STORE_FILE_NAME = "movie_app.preferences_pb"
    }
    data object RoomDatabase : AppConfig{
        internal const val DATABASE_NAME = "movie_app_db"
    }

    data object Staging : AppConfig
    data object Dev : AppConfig
    data object Local : AppConfig

}