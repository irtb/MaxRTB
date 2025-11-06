package com.maxrtb.zx.listener

import android.util.SparseArray

object ZXListenerManager {
    private val listeners = SparseArray<ZXBaseListener>()
    private val lock = Any()
    
    fun registerListener(requestId: Int, listener: ZXBaseListener) {
        synchronized(lock) {
            listeners.put(requestId, listener)
        }
    }
    
    fun getListener(requestId: Int): ZXBaseListener? {
        synchronized(lock) {
            return listeners.get(requestId)
        }
    }
    
    fun removeListener(requestId: Int) {
        synchronized(lock) {
            listeners.remove(requestId)
        }
    }
    
    fun clearAll() {
        synchronized(lock) {
            listeners.clear()
        }
    }
    
    fun size(): Int {
        synchronized(lock) {
            return listeners.size()
        }
    }
}
