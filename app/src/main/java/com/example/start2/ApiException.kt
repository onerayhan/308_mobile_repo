package com.example.start2

import retrofit2.Call
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class ApiException(message: String) : Exception(message)

// Define an extension function to convert Call<T> to a deferred.
suspend fun <T> Call<T>.await(): retrofit2.Response<T> {
    return kotlinx.coroutines.suspendCancellableCoroutine { cont ->
        enqueue(object : retrofit2.Callback<T> {
            override fun onResponse(call: Call<T>, response: retrofit2.Response<T>) {
                cont.resume(response)
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                cont.resumeWithException(t)
            }
        })

        cont.invokeOnCancellation {
            try {
                cancel()
            } catch (ex: Throwable) {
                // Ignore cancellation exceptions
            }
        }
    }
}