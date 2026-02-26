package com.brainx.local_datastore.setup

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.brainx.local_datastore.provider.DatastoreFileProvider


internal fun createDataStore(context: Context,datastoreProvider: DatastoreFileProvider): DataStore<Preferences> {
    return createDataStore {
        check(
            value = datastoreProvider.getDatastoreName().isNullOrBlank(),
            lazyMessage = {
                "Datastore File name is NullOrBlank. Please provide a valid file name by implementing the interface DatastoreFileProvider and adding it to DI Module"
            }
        )
        context.filesDir.resolve(
            datastoreProvider.getDatastoreName()?:""
        ).absolutePath
    }
}