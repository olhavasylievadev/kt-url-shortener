package com.url_shortener.util

private const val BASE62_CHARS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
private const val BASE = 62

fun encodeBase62(num: Int): String {
    var n = num
    val sb = StringBuilder()
    while (n > 0) {
        sb.append(BASE62_CHARS[n % BASE])
        n /= BASE
    }
    return sb.reverse().toString()
}

fun decodeBase62(str: String): Int {
    var result = 0
    for (char in str) {
        result = result * BASE + BASE62_CHARS.indexOf(char)
    }
    return result
}