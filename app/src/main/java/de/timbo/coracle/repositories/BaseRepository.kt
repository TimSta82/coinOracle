package de.timbo.coracle.repositories

import de.timbo.coracle.api.CoinCapApiInterface
import de.timbo.coracle.api.CurrencyApiInterface
import de.timbo.coracle.api.ResponseEvaluator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import retrofit2.Response

abstract class BaseRepository : KoinComponent {

    protected val repositoryScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    protected val coinApi by inject<CoinCapApiInterface>()
    protected val currencyApi by inject<CurrencyApiInterface>()

    suspend fun <T> coinApiCall(block: suspend CoinCapApiInterface.() -> Response<T>): ResponseEvaluator.Result<T> {
        val result = runCatching { block(coinApi) }
        result.exceptionOrNull()?.printStackTrace()
        return ResponseEvaluator.evaluate(result.getOrNull())
    }

    suspend fun <T> currencyApiCall(block: suspend CurrencyApiInterface.() -> Response<T>): ResponseEvaluator.Result<T> {
        val result = runCatching { block(currencyApi) }
        result.exceptionOrNull()?.printStackTrace()
        return ResponseEvaluator.evaluate(result.getOrNull())
    }
}
