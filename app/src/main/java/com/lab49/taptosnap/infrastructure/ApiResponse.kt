package com.lab49.taptosnap.infrastructure

import com.squareup.moshi.Json
import java.io.Serializable

/**
 * Created by Edwin S. Cowart
 * Lab49 Take-Home
 * Tap To Snap
 */

enum class ResponseType {
    Success, Informational, Redirection, ClientError, ServerError
}

interface Response

abstract class ApiResponse<T>(val responseType: ResponseType): Response {
    abstract val statusCode: Int
    abstract val headers: Map<String,List<String>>
}

class Success<T>(
    val data: T,
    override val statusCode: Int = -1,
    override val headers: Map<String, List<String>> = mapOf()
): ApiResponse<T>(ResponseType.Success)

class Informational<T>(
    val statusText: String,
    override val statusCode: Int = -1,
    override val headers: Map<String, List<String>> = mapOf()
) : ApiResponse<T>(ResponseType.Informational)

class Redirection<T>(
    override val statusCode: Int = -1,
    override val headers: Map<String, List<String>> = mapOf()
) : ApiResponse<T>(ResponseType.Redirection)

class ClientError<T>(
    val message: String? = null,
    val body: ErrorBody? = null,
    override val statusCode: Int = -1,
    override val headers: Map<String, List<String>> = mapOf()
) : ApiResponse<T>(ResponseType.ClientError)

class ServerError<T>(
    val message: String? = null,
    val body: ErrorBody? = null,
    override val statusCode: Int = -1,
    override val headers: Map<String, List<String>>
): ApiResponse<T>(ResponseType.ServerError)

// TODO - Deserialize the APIs application/json response for error messages
data class ErrorBody (
    @Json(name = "TODO") val TODO: String
) : Serializable
