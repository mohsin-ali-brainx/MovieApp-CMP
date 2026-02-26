package com.brainx.datasource.local_pref

import com.brainx.datasource.AppConfig
import com.brainx.local_datastore.provider.DatastoreFileProvider

class DatastoreFileProviderImp : DatastoreFileProvider {
    override fun getDatastoreName(): String {
        return AppConfig.Datastore.DATA_STORE_FILE_NAME
    }
}