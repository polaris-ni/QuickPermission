package com.lyni.permission.core

import android.os.Handler
import android.os.Looper
import java.util.*

internal object RequestManager {

    private var requests: Stack<Request> = Stack()
    private var currentRequest: Request? = null

    private val handler = Handler(Looper.getMainLooper())

    private val requestRunnable = Runnable {
        currentRequest?.start()
    }

    private val isCurrentRequestInvalid: Boolean
        get() = currentRequest?.let { System.currentTimeMillis() - it.requestTime > 5 * 1000L } ?: true

    fun pushRequest(request: Request) {
        requests.let {
            val index = it.indexOf(request)
            if (index >= 0) {
                val to = it.size - 1
                if (index != to) {
                    Collections.swap(requests, index, to)
                }
            } else {
                it.push(request)
            }

            if (it.isNotEmpty() && isCurrentRequestInvalid) {
                currentRequest = it.pop()
                handler.post(requestRunnable)
            }
        }
    }

    fun getCurrentRequest(): Request {
        if (currentRequest == null) {
            throw NullPointerException("Current request is null, please check.")
        }
        return currentRequest!!
    }

    fun startNextRequest() {
        currentRequest = null
        requests.let {
            currentRequest = if (it.empty()) null else it.pop()
            currentRequest?.let { handler.post(requestRunnable) }
        }
    }

}
