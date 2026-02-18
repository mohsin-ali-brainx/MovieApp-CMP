package com.brainx.local_datastore.setup

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.brainx.local_datastore.utils.DataStoreConstants


internal fun createDataStore(context: Context): DataStore<Preferences> {
    return createDataStore {
        context.filesDir.resolve(DataStoreConstants.DATA_STORE_FILE_NAME).absolutePath
    }
}