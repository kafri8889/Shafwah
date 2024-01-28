package data.model.response

/**
 * Response api umum dengan format
 *
 * ```
 * {
 * 	"message": "message",
 * 	"status": 200,
 * 	"data": null
 * }
 * ```
 */
interface CommonSingleResponse<T> {
	val message: String
	val status: Int
	val data: T?
}
