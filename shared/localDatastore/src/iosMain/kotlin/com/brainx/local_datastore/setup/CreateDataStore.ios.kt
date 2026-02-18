package com.brainx.local_datastore.setup

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.brainx.local_datastore.utils.DataStoreConstants
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSTemporaryDirectory
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask

/**
 * Create a DataStore instance for iOS with a predefined file path
 */
//fun createDataStore(): DataStore<Preferences> {
//    return createDataStore {
//        // Use a temporary directory path for iOS
//        "${NSTemporaryDirectory()}/${DataStoreConstants.DATA_STORE_FILE_NAME}"
//    }
//}

@OptIn(ExperimentalForeignApi::class)
internal fun createDataStore(): DataStore<Preferences> = createDataStore(
    producePath = {
        val documentDirectory: NSURL? = NSFileManager.defaultManager.URLForDirectory(
            directory = NSDocumentDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = false,
            error = null,
        )
        requireNotNull(documentDirectory).path + "/${DataStoreConstants.DATA_STORE_FILE_NAME}"
    }
)