package com.brainx.local_datastore.setup

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.brainx.local_datastore.provider.DatastoreFileProvider
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSTemporaryDirectory
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask

/**
 * Create a DataStore instance for iOS with a predefined file path
 */

@OptIn(ExperimentalForeignApi::class)
internal fun createDataStore(datastoreFileProvider: DatastoreFileProvider): DataStore<Preferences> = createDataStore(
    producePath = {
        val documentDirectory: NSURL? = NSFileManager.defaultManager.URLForDirectory(
            directory = NSDocumentDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = false,
            error = null,
        )
        check(
            value = datastoreFileProvider.getDatastoreName().isNullOrBlank(),
            lazyMessage = {
                "Datastore File name is null. Please provide a valid file name by implementing the interface DatastoreFileProvider and adding it to DI Module"
            }
        )
        requireNotNull(documentDirectory).path + "/${datastoreFileProvider.getDatastoreName()}"
    }
)