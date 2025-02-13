package cn.changjiahong.bamb.bamb.datastore

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlin.reflect.KProperty


class SyncDataStoreHelper<T>(private val data: DataStore<T>) {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return runBlocking { data.data.first() }
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        runBlocking {
            data.updateData { value }
        }
    }
}

fun <T> DataStore<T>.sync(): SyncDataStoreHelper<T> {
    return SyncDataStoreHelper(this)
}
