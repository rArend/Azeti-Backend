package com.renan.azetiTest.controller

import org.jsoup.Jsoup
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URL
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

/**
 * @author renan.arend@visual-meta.com
 * @date  20.03.2021
 */
@RestController
@RequestMapping("/title")
class PageController {

	@CrossOrigin(origins = ["http://localhost:3000"])
	@GetMapping
	fun getPageTitle(@RequestParam("page_address") pageAddress: String): ResponseEntity<String> {
		val client = HttpClient.newBuilder().build()
		try {
			var uri = pageAddress;
			if (!(pageAddress.contains("http://") || pageAddress.contains("https://") || pageAddress.contains("://"))) {
				uri = "http://" + pageAddress;
			}
			val request = HttpRequest.newBuilder()
					.uri(URL(uri).toURI())
					.build();
			val response = client.send(request, HttpResponse.BodyHandlers.ofString())
			if (response.statusCode().equals(HttpStatus.OK.value())) {
				val responseBody = Jsoup.parseBodyFragment(response.body());
				return ResponseEntity.status(HttpStatus.OK).body(responseBody.title());
			}
			return ResponseEntity.unprocessableEntity().body("Invalid Url");
		}catch (e: Exception) {
			return ResponseEntity.unprocessableEntity().body("Invalid Url");
		}

	}
}
