package com.example.library

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.reactive.server.WebTestClient
import java.util.*

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LibraryApplicationTests {

	@Autowired
	lateinit var webTestClient : WebTestClient

	@Test
	fun `Get Books - Success`() {
		webTestClient
				.get()
				.uri("/books")
				.header("Authorization", getBasicAuthorization())
				.exchange()
				.expectStatus()
				.isOk()
				.expectBody()
				.json("""[{"id":1,"title":"Titanic","year":1999,"genre":{"name":"Romantic","description":"Romantic"},"cast":[{"name":"Lionardo Dicaprio","inAs":"Jack","noOfAwards":1},{"name":"Kate Winslet","inAs":"Rose","noOfAwards":2}]}]""");
	}

	@Test
	fun `Get Books - Failure (No Authorization)`() {
		webTestClient
				.get()
				.uri("/books")
				.exchange()
				.expectStatus()
				.isUnauthorized()
	}

	@Test
	fun `Get Book - Success`() {
		webTestClient
				.get()
				.uri("/books/1")
				.header("Authorization", getBasicAuthorization())
				.exchange()
				.expectStatus()
				.isOk()
				.expectBody()
				.json("""{"id":1,"title":"Titanic","year":1999,"genre":{"name":"Romantic","description":"Romantic"},"cast":[{"name":"Lionardo Dicaprio","inAs":"Jack","noOfAwards":1},{"name":"Kate Winslet","inAs":"Rose","noOfAwards":2}]}""")

	}

	@Test
	fun `Rate Book - Success`() {
		webTestClient
				.put()
				.uri("/books/1/rate?comment=Ok Book&rating=3")
				.header("Authorization", getBasicAuthorization())
				.exchange()
				.expectStatus()
				.isOk()
				.expectBody()
				.json("""{"id":1,"title":"Titanic","year":1999,"genre":{"name":"Romantic","description":"Romantic"},"ratings":[{"comment":"Good Book","rating":5},{"comment":"Ok Book","rating":3}],"cast":[{"name":"Lionardo Dicaprio","inAs":"Jack","noOfAwards":1},{"name":"Kate Winslet","inAs":"Rose","noOfAwards":2}]}""")
	}

	fun getBasicAuthorization() : String {
		val plainCreds = "user:password"
		val plainCredsBytes = plainCreds.toByteArray()
		val base64CredsBytes = Base64.getEncoder().encode(plainCredsBytes)
		val base64Creds = String(base64CredsBytes)

		return "Basic $base64Creds"
	}

}
