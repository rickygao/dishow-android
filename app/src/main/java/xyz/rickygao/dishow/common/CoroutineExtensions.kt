package xyz.rickygao.dishow.common

import kotlinx.coroutines.experimental.Deferred

suspend fun <T> Deferred<T>.awaitAndHandle(handler: AwaitHandler = {}): T? =
        try {
            await()
        } catch (t: Throwable) {
            handler(t)
            null
        }

typealias AwaitHandler = suspend (Throwable) -> Unit