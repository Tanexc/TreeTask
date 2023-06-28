package ru.tanexc.tree.core.utils


import java.security.MessageDigest

object HashTool {

    private val digest = MessageDigest.getInstance(HASH_ALGORYTHM)

    fun getHashByteArray(value: String): ByteArray {
        return digest.digest(value.toByteArray())
    }

    fun getHashPart(values: List<String>, length: Int): String {
        val hash = getHashByteArray(values.toString()).fold("") { str, it -> str + "%02x".format(it) }
        return if (length > hash.length) {
            hash
        } else {
            hash.substring(hash.length - length, hash.length)
        }
    }
}