package data.model.response

/**
 * Response api umum dengan format
 *
 * ```
 * {
 * 	"message": "message",
 * 	"status": 200,
 * 	"data": []
 * }
 * ```
 */
interface CommonMultipleResponse<E> {
	val message: String
	val status: Int
	val data: List<E>
}