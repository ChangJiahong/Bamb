package cn.changjiahong.bamb.bamb.datastore

import androidx.datastore.core.DataMigration
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.core.okio.OkioSerializer
import androidx.datastore.core.okio.OkioStorage
import cn.changjiahong.bamb.bamb.file.FileStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import okio.FileSystem
import okio.Path.Companion.toPath
import okio.SYSTEM
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty
import kotlin.reflect.typeOf

object DataStores

inline fun <reified T : Any> dataStore(
    defaultValue: T,
    serializer: OkioSerializer<T> = SettingSerializable(typeOf<T>(), defaultValue),
    corruptionHandler: ReplaceFileCorruptionHandler<T>? = null,
    noinline produceMigrations: () -> List<DataMigration<T>> = { listOf() },
    scope: CoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
): ReadOnlyProperty<DataStores,DataStore<T>> {
    return DataStoreSingletonDelegate(
        T::class.qualifiedName?:"q", serializer, corruptionHandler, produceMigrations, scope
    )
}


/**
 * Delegate class to manage DataStore as a singleton.
 */
class DataStoreSingletonDelegate<T>(
    private val fileName: String,
    private val serializer: OkioSerializer<T>,
    private val corruptionHandler: ReplaceFileCorruptionHandler<T>?,
    private val produceMigrations: () -> List<DataMigration<T>>,
    private val scope: CoroutineScope
):ReadOnlyProperty<DataStores,DataStore<T>> {

    private var INSTANCE: DataStore<T>? = null

    override fun getValue(thisRef: DataStores, property: KProperty<*>): DataStore<T> {
        return INSTANCE ?: run {
            if (INSTANCE == null) {
                INSTANCE = DataStoreFactory.create(
                    storage = OkioStorage(FileSystem.SYSTEM, serializer = serializer) {
                        "${FileStorage.fileDirectory()}/datastore/$fileName".toPath()
                    },
                    corruptionHandler = corruptionHandler,
                    migrations = produceMigrations(),
                    scope = scope
                )
            }
            INSTANCE!!
        }
    }
}
