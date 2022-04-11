package com.example.giftexchange

import com.fatboyindustrial.gsonjavatime.OffsetDateTimeConverter
import com.google.gson.annotations.JsonAdapter
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.context.request.WebRequest
import java.time.OffsetDateTime
import java.util.*


@SpringBootApplication
class GiftexchangeApplication

fun main(args: Array<String>) {
	runApplication<GiftexchangeApplication>(*args)
}

@RestController
class FamilyMemberResource(val service: MemberService) {
	@GetMapping("members")
	fun index(): List<FamilyMember> = service.findMembers()

	@PostMapping("members")
	fun post(@RequestBody member: FamilyMember): ResponseEntity<Any> {
		val postedMember = service.post(member)
		return generateResponse("Family member created", HttpStatus.OK, postedMember)
	}
}

@RestController
@RequestMapping("/gift_exchange")
class GiftExchangeResource(val service: GiftService) {
	@GetMapping
	fun index(): List<GiftExchange> = service.findGiftExchange()

	@PostMapping("/shuffle")
	fun post(@RequestBody giftExchange: GiftExchange): ResponseEntity<Any> {
		// check if giver and receiver is the same
		if (giftExchange.giverid == giftExchange.receiverid)
			throw Exception("Cannot give a gift to themselves")
		// check if receiver already get the gift
		val alreadyGifted = index().sortedByDescending { it.time }.firstOrNull{
				   (it.receiverid == giftExchange.receiverid)
					&& (it.giverid == giftExchange.giverid)
					&& (OffsetDateTime.now().minusYears(3) < it.time)}
		if (alreadyGifted != null)
			throw Exception("Giver already gifted to receiver less than 3 years")

		val postedGiftExchange = service.post(giftExchange)
		return generateResponse("Gift assigned", HttpStatus.OK, postedGiftExchange)
	}
}

@ControllerAdvice
class GlobalExceptionHandler {
	@ExceptionHandler(Exception::class)
	fun globalExceptionHandler(ex: Exception, request: WebRequest): ResponseEntity<ErrorMessage?>? {
		val message = ErrorMessage(
			HttpStatus.BAD_REQUEST.value(),
			Date(),
			ex.message,
			request.getDescription(false)
		)
		return ResponseEntity<ErrorMessage?>(message, HttpStatus.BAD_REQUEST)
	}
}

fun generateResponse(message: String, status: HttpStatus, responseObj: Any): ResponseEntity<Any> {
	val map = mapOf("message" to message, "status" to status, "data" to responseObj)
	return ResponseEntity(map, status)
}

data class ErrorMessage(
	val httpStatus: Int,
	val date: Date,
	val message: String?,
	val description: String
)


@Table("FAMILYMEMBER")
data class FamilyMember(
	@Id val id: String?,
	val fullname: String
)

@Table("GIFTEXCHANGE")
data class GiftExchange(
	@Id val id: String?,
	val giverid: String,
	val receiverid: String,
	@JsonAdapter(OffsetDateTimeConverter::class)val time: OffsetDateTime
)