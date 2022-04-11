package com.example.giftexchange

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.*
import org.springframework.web.servlet.function.RequestPredicates.contentType
import java.time.OffsetDateTime
import java.util.*

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
internal class GiftexchangeApplicationTests @Autowired constructor(
	val mockMvc: MockMvc,
	val objectMapper: ObjectMapper
) {

	val baseUrl = "/gift_exchange"

	@Test
	fun `can post gift shuffle and get the shuffle`() {
		val giverId = UUID.randomUUID().toString()
		val receiverid = UUID.randomUUID().toString()
		val giftExchange = GiftExchange(
			null,
			giverId,
			receiverid,
			OffsetDateTime.now()
		)

		mockMvc.post("$baseUrl/shuffle") {
			contentType = MediaType.APPLICATION_JSON
			content = objectMapper.writeValueAsString(giftExchange)
		}
			.andDo { print()}
			.andExpect { status { isOk() }}
			.andExpect {
				contentType(MediaType.APPLICATION_JSON)
				jsonPath("$.message") { value("Gift assigned") }
				jsonPath("$.data.giverid") { value(giverId) }
				jsonPath("$.data.receiverid") { value(receiverid) }
			}

		mockMvc.get("http://localhost:8080/gift_exchange")
			.andDo { print()}
			.andExpect { status { isOk()} }
			.andExpect {
				contentType(MediaType.APPLICATION_JSON)
				jsonPath("$[0].giverid") { value(giverId) }
				jsonPath("$[0].receiverid") { value(receiverid) }
			}
	}

	@Test
	fun `cannot post gift shuffle for a same receiver who get the gift less than 3 years`() {
		val giverId = UUID.randomUUID().toString()
		val receiverid = UUID.randomUUID().toString()
		val giftExchange = GiftExchange(
			null,
			giverId,
			receiverid,
			OffsetDateTime.now().minusWeeks(2)
		)

		mockMvc.post("$baseUrl/shuffle") {
			contentType = MediaType.APPLICATION_JSON
			content = objectMapper.writeValueAsString(giftExchange)
		}
			.andDo { print()}
			.andExpect { status { isOk() }}
			.andExpect {
				contentType(MediaType.APPLICATION_JSON)
				jsonPath("$.message") { value("Gift assigned") }
				jsonPath("$.data.giverid") { value(giverId) }
				jsonPath("$.data.receiverid") { value(receiverid) }
			}

		mockMvc.post("$baseUrl/shuffle") {
			contentType = MediaType.APPLICATION_JSON
			content = objectMapper.writeValueAsString(giftExchange)
		}
			.andDo { print()}
			.andExpect { status { isBadRequest() }}
			.andExpect {
				contentType(MediaType.APPLICATION_JSON)
				jsonPath("$.message") { value("Giver already gifted to receiver less than 3 years") }
			}
	}

	@Test
	fun `can post a family member and get the member`() {
		val fullName = "John Adams"
		val familyMember = FamilyMember(
			null,
			fullName
		)

		mockMvc.post("/members") {
			contentType = MediaType.APPLICATION_JSON
			content = objectMapper.writeValueAsString(familyMember)
		}
			.andDo { print()}
			.andExpect { status { isOk() }}
			.andExpect {
				contentType(MediaType.APPLICATION_JSON)
				jsonPath("$.message") { value("Family member created") }
				jsonPath("$.data.fullname") { value(fullName) }
			}

		mockMvc.get("/members")
			.andDo { print()}
			.andExpect { status { isOk()} }
			.andExpect {
				contentType(MediaType.APPLICATION_JSON)
				jsonPath("$[0].fullname") { value(fullName) }
			}
	}
}
