package com.goofy.realtime.exception

open class RealtimeException(
    val errorCode: ErrorCode,
    override val message: String? = errorCode.description,
    val extra: Map<String, Any>? = null,
) : RuntimeException(message ?: errorCode.description)

class NotFoundException(errorCode: ErrorCode) : RealtimeException(errorCode)

class InvalidTokenException(errorCode: ErrorCode) : RealtimeException(errorCode)

class InvalidRequestException(errorCode: ErrorCode, message: String? = null) : RealtimeException(errorCode, message)

class FailToCreateException(errorCode: ErrorCode) : RealtimeException(errorCode)

class AlreadyException(errorCode: ErrorCode) : RealtimeException(errorCode)

class NoAuthorityException(errorCode: ErrorCode) : RealtimeException(errorCode)

class FailToExecuteException(errorCode: ErrorCode) : RealtimeException(errorCode)

class RedisPubSubException(errorCode: ErrorCode) : RealtimeException(errorCode)

/** Image Exception */
class ImageUploadException : RealtimeException(ErrorCode.IMAGE_CLIENT_UPLOAD_ERROR)
