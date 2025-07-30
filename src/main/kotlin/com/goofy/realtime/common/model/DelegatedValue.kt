package com.goofy.realtime.common.model

abstract class DelegatedValue<T>(
    open val value: T,
) {
    override fun toString(): String {
        return value.toString()
    }

    override fun equals(other: Any?): Boolean {
        return when (other is DelegatedValue<*> && this::class == other::class) {
            true -> value == other.value
            else -> super.equals(other)
        }
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }
}
