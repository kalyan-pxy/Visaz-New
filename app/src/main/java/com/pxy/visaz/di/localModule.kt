import com.pxy.visaz.data.local.LocalDataSource
import com.pxy.visaz.data.local.database.DatabaseConstants
import com.pxy.visaz.data.remote.RemoteDataSource
import com.pxy.visaz.data.repository.Repository
import com.pxy.visaz.setup.DatabaseFactory
import com.pxy.visaz.domain.IRepository
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

val localModule = module {
    single(named(DatabaseConstants.DATABASE_NAME)) {
        DatabaseFactory().getOrCreateDatabaseInstance(androidContext())
    }
    single(named("LOCAL_DATA_SOURCE")) { LocalDataSource(get(named(DatabaseConstants.DATABASE_NAME))) }
    single(named("REMOTE_DATA_SOURCE")) { RemoteDataSource(get(named("APP_SERVICE"))) }
    single<IRepository> {
        Repository(
            remoteDataSource = get(named("REMOTE_DATA_SOURCE")),
            localDataSource = get(named("LOCAL_DATA_SOURCE"))
        )
    }
}