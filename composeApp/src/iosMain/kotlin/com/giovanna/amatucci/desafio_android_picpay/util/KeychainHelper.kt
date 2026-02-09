package com.giovanna.amatucci.desafio_android_picpay.util

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
@OptIn(ExperimentalForeignApi::class)
class KeychainHelper {
    private val service = "com.giovanna.picpay.database"
    private val account = "db_key"
    fun saveKey(key: ByteArray) {
        val query = createQuery()
        query.setObject(anObject = key.toNSData(), forKey = kSecValueData.toNsCopying())
        SecItemDelete(query.asCFDictionary())
        SecItemAdd(query.asCFDictionary(), null)
    }
    fun getKey(): ByteArray? {
        val query = createQuery()
        query.setObject(
            anObject = kCFBooleanTrue.toNsObject(), forKey = kSecReturnData.toNsCopying()
        )
        query.setObject(
            anObject = kSecMatchLimitOne.toNsObject(), forKey = kSecMatchLimit.toNsCopying()
        )

        memScoped {
            val result = alloc<platform.CoreFoundation.CFTypeRefVar>()
            val status = SecItemCopyMatching(query.asCFDictionary(), result.ptr)

            if (status == errSecSuccess) {
                val data = CFBridgingRelease(result.value) as? NSData
                return data?.toByteArray()
            }
        }
        return null
    }

    private fun createQuery(): NSMutableDictionary {
        val query = NSMutableDictionary()
        query.setObject(
            anObject = kSecClassGenericPassword.toNsObject(), forKey = kSecClass.toNsCopying()
        )
        query.setObject(anObject = service as Any, forKey = kSecAttrService.toNsCopying())
        query.setObject(anObject = account as Any, forKey = kSecAttrAccount.toNsCopying())
        return query
    }

    private fun Any?.toNsCopying(): platform.Foundation.NSCopyingProtocol {
        val ptr = (this as? kotlinx.cinterop.CPointer<*>)
            ?: return this as platform.Foundation.NSCopyingProtocol
        return CFBridgingRelease(CFRetain(ptr)) as platform.Foundation.NSCopyingProtocol
    }

    private fun Any?.toNsObject(): Any {
        val ptr = (this as? kotlinx.cinterop.CPointer<*>) ?: return this as Any
        return CFBridgingRelease(CFRetain(ptr)) as Any
    }

    private fun NSMutableDictionary.asCFDictionary(): CFDictionaryRef? {
        return (this as Any).reinterpret()
    }

    private inline fun <reified T : kotlinx.cinterop.CPointed> Any.reinterpret(): kotlinx.cinterop.CPointer<T>? {
        return CFBridgingRetain(this) as? kotlinx.cinterop.CPointer<T>
    }

    private fun CFBridgingRetain(obj: Any): kotlinx.cinterop.CPointer<*>? {
        return platform.Foundation.CFBridgingRetain(obj)
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
                platform.posix.memcpy(
                    pinned.addressOf(0), this@toByteArray.bytes, this@toByteArray.length.toULong()
                )
            }
        }
    }
}