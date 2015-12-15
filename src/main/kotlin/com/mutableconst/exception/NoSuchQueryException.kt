package com.mutableconst.exception

import java.util.*

class NoSuchQueryException(private val exceptionMessage: String) : NoSuchElementException(exceptionMessage) {
    override fun toString(): String = exceptionMessage
}
