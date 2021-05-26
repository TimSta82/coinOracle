package de.timbo.coracle.repositories

import de.timbo.coracle.api.CoinCapApiInterface
import de.timbo.coracle.api.ResponseEvaluator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import retrofit2.Response

abstract class BaseRepository : KoinComponent {

    protected val repositoryScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    protected val api by inject<CoinCapApiInterface>()

    suspend fun <T> apiCall(block: suspend CoinCapApiInterface.() -> Response<T>): ResponseEvaluator.Result<T> {
        val result = runCatching { block(api) }
        result.exceptionOrNull()?.printStackTrace()
        return ResponseEvaluator.evaluate(result.getOrNull())
    }
}
