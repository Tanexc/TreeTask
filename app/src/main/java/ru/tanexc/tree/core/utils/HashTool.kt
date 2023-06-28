package ru.tanexc.tree.core.utils

import java.security.MessageDigest

object HashTool {

    private val digest = MessageDigest.getInstance(HASH_ALGORYTHM)

    fun getHashByteArray(value: String): ByteArray {
        return digest.digest(value.toByteArray())
    }

    fun getHashPart(values: List<String>, length: Int): String {
        val hash = getHashByteArray(values.toString())
        return if (length > hash.size) {
            hash.toString()
        } else {
            hash.copyOfRange(hash.size - length, hash.size).toString()
        }
    }
}