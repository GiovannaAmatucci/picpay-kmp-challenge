package com.giovanna.amatucci.desafio_android_picpay.util

import kotlinx.cinterop.CPointed
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.usePinned
import kotlinx.cinterop.value
import platform.CoreFoundation.CFDictionaryRef
import platform.CoreFoundation.CFRetain
import platform.CoreFoundation.kCFBooleanTrue
import platform.Foundation.CFBridgingRelease
import platform.Foundation.NSCopyingProtocol
import platform.Foundation.NSData
import platform.Foundation.NSMutableDictionary
import platform.Foundation.create
import platform.Security.SecItemAdd
import platform.Security.SecItemCopyMatching
import platform.Security.SecItemDelete
import platform.Security.errSecSuccess
import platform.Security.kSecAttrAccount
import platform.Security.kSecAttrService
import platform.Security.kSecClass
import platform.Security.kSecClassGenericPassword
import platform.Security.kSecMatchLimit
import platform.Security.kSecMatchLimitOne
import platform.Security.kSecReturnData
import platform.Security.kSecValueData
import platform.posix.memcpy

@OptIn(ExperimentalForeignApi::class)
class KeychainHelper {
    private val service = "com.giovanna.picpay.database"
    private val account = "db_key"

    fun saveKey(key: ByteArray) {
        val query = createQuery().apply {
            setObject(value = key.toNSData(), forKey = kSecValueData.bridgeKey())
        }

        SecItemDelete(query as CFDictionaryRef)
        SecItemAdd(query as CFDictionaryRef, null)
    }

    fun getKey(): ByteArray? {
        val query = createQuery().apply {
            setObject(value = kCFBooleanTrue.bridge(), forKey = kSecReturnData.bridgeKey())
            setObject(value = kSecMatchLimitOne.bridge(), forKey = kSecMatchLimit.bridgeKey())
        }

        memScoped {
            val result = alloc<platform.CoreFoundation.CFTypeRefVar>()
            val status = SecItemCopyMatching(query as CFDictionaryRef, result.ptr)

            if (status == errSecSuccess) {
                val data = CFBridgingRelease(result.value) as? NSData
                return data?.toByteArray()
            }
        }
        return null
    }

    private fun createQuery(): NSMutableDictionary {
        return NSMutableDictionary().apply {
            setObject(value = kSecClassGenericPassword.bridge(), forKey = kSecClass.bridgeKey())
            setObject(value = service, forKey = kSecAttrService.bridgeKey())
            setObject(value = account, forKey = kSecAttrAccount.bridgeKey())
        }
    }

    private fun CPointer<*>?.bridge(): Any {
        if (this == null) return ""
        val genericPtr = this as CPointer<CPointed>
        return CFBridgingRelease(CFRetain(genericPtr)) as Any
    }

    private fun CPointer<*>?.bridgeKey(): NSCopyingProtocol {
        if (this == null) throw IllegalStateException("Null key")
        val genericPtr = this as CPointer<CPointed>
        return CFBridgingRelease(CFRetain(genericPtr)) as NSCopyingProtocol
    }

    private fun ByteArray.toNSData(): NSData = memScoped {
        NSData.create(
            bytes = this@toNSData.usePinned { it.addressOf(0) },
            length = this@toNSData.size.toULong()
        )
    }

    private fun NSData.toByteArray(): ByteArray = ByteArray(this.length.toInt()).apply {
        if (this.isNotEmpty()) {
            usePinned { pinned ->
                memcpyChain(
                    pinned.addressOf(0), this@toByteArray.bytes, this@toByteArray.length.toULong()
                )
            }
        }
    }
}

@OptIn(ExperimentalForeignApi::class)
private fun memcpyChain(dest: CPointer<*>, src: CPointer<*>, size: ULong) {
    memcpy(dest, src, size)
}