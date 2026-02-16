package com.brainx.local_datastore.utils

/*
* Note:
* File name extension should be preferences_pb if you don’t follow format extension you will got this issue
*
* " java.lang.IllegalStateException: File extension for file: /data/user/0/com.kaito.kmoney/files/app.preference
*  does not match required extension for Preferences file: preferences_pb"
*
* */
internal object DataStoreConstants {
    internal const val DATA_STORE_FILE_NAME = "ticket_tribe.preferences_pb"
}