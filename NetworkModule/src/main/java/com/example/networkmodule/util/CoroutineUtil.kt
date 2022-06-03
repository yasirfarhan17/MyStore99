package com.example.networkmodule.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun <T> io(block: suspend CoroutineScope.() -> T): T {
    return withContext(Dispatchers.IO) {
        block.invoke(this)
    }
}

suspend fun <T> computation(block: suspend CoroutineScope.() -> T): T {
    return withContext(Dispatchers.Default) {
        block.invoke(this)
    }
}

suspend fun <T> ui(block: suspend CoroutineScope.() -> T): T {
    return withContext(Dispatchers.Main) {
        block.invoke(this)
    }
}