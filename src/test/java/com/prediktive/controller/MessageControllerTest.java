package com.prediktive.controller;

import com.prediktive.domain.UserData;
import com.prediktive.domain.UserDataMock;
import com.prediktive.domain.api.MessageRequest;
import com.prediktive.security.JwtUtil;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class MessageControllerTest {

    @Autowired
    private JwtUtil jwtUtil;
    @LocalServerPort
    private Integer port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void shouldProvideANewQuestion() {
        String bodyMatcher = "^(Here you go, solve the question: \"Please sum the numbers )(\\d+(?:,\\d+)*)(\")$";
        String jwtTokenMatcher = "^[A-Za-z0-9-_]+(\\.[A-Za-z0-9-_]+){2}$";
        given()
                .when()
                .get("/human-assured/api/v1/message/ask")
                .then()
                .statusCode(200)
                .header("X-Api-Token", Matchers.matchesRegex(jwtTokenMatcher))
                .body("message", Matchers.matchesRegex(bodyMatcher));
    }

    @Test
    void shouldAnswerQuestionValid() {
        UserData userData = UserDataMock.createUserDataInstance();
        String token = jwtUtil.generateToken(userData);
        MessageRequest messageRequest = new MessageRequest("The original question was “Please sum the numbers 5,7,8” and the answer is 20.");

        given()
                .headers(
                        "X-Session-ID", userData.getSessionId(),
                        "X-Forwarded-For", userData.getIp(),
                        "User-Agent", userData.getUserAgent()
                )
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .header("X-Api-Token", token)
                .body(messageRequest)
                .when()
                .post("/human-assured/api/v1/message/answer")
                .then()
                .statusCode(200)
                .body("message", Matchers.equalTo("That’s great!"));
    }

    @Test
    void shouldAnswerQuestionTokenNotValidDiffSession() {
        UserData userData = UserDataMock.createUserDataInstance();
        String token = jwtUtil.generateToken(userData);
        MessageRequest messageRequest = new MessageRequest("The original question was “Please sum the numbers 5,7,8” and the answer is 20.");

        given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .header("X-Api-Token", token)
                .body(messageRequest)
                .when()
                .post("/human-assured/api/v1/message/answer")
                .then()
                .statusCode(400)
                .body("message", Matchers.equalTo("Token is not valid."));
    }

    @Test
    void shouldAnswerQuestionTokenNotValidDiffUserAgent() {
        UserData userData = UserDataMock.createUserDataInstance();
        String token = jwtUtil.generateToken(userData);
        MessageRequest messageRequest = new MessageRequest("The original question was “Please sum the numbers 5,7,8” and the answer is 20.");

        given()
                .accept(ContentType.JSON)
                .headers(
                        "X-Session-ID", userData.getSessionId(),
                        "X-Forwarded-For", userData.getIp(),
                        "User-Agent", "user agent changed"
                )
                .contentType(ContentType.JSON)
                .header("X-Api-Token", token)
                .body(messageRequest)
                .when()
                .post("/human-assured/api/v1/message/answer")
                .then()
                .statusCode(400)
                .body("message", Matchers.equalTo("Token is not valid."));
    }

    @Test
    void shouldAnswerQuestionFormatResponseNotValid() {
        UserData userData = UserDataMock.createUserDataInstance();
        String token = jwtUtil.generateToken(userData);
        MessageRequest messageRequest = new MessageRequest("The original question was “Please sum the numbers 5,7,8” and 20 is the result.");

        given()
                .accept(ContentType.JSON)
                .headers(
                        "X-Session-ID", userData.getSessionId(),
                        "X-Forwarded-For", userData.getIp(),
                        "User-Agent", userData.getUserAgent()
                )
                .contentType(ContentType.JSON)
                .header("X-Api-Token", token)
                .body(messageRequest)
                .when()
                .post("/human-assured/api/v1/message/answer")
                .then()
                .statusCode(400)
                .body("message", Matchers.equalTo("Answer format wrong. Valid example: The original question was “Please sum the numbers 9,5,3” and the answer is 15."));
    }

    @Test
    void shouldAnswerQuestionReponseWrong() {
        UserData userData = UserDataMock.createUserDataInstance();
        String token = jwtUtil.generateToken(userData);
        MessageRequest messageRequest = new MessageRequest("The original question was “Please sum the numbers 5,7,8” and the answer is 15.");

        given()
                .headers(
                        "X-Session-ID", userData.getSessionId(),
                        "X-Forwarded-For", userData.getIp(),
                        "User-Agent", userData.getUserAgent()
                )
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .header("X-Api-Token", token)
                .body(messageRequest)
                .when()
                .post("/human-assured/api/v1/message/answer")
                .then()
                .statusCode(400)
                .body("message", Matchers.equalTo("That’s wrong. Please try again."));
    }

    @Test
    void shouldAnswerQuestionTokenExpired() {
        UserData userData = UserDataMock.createUserDataInstance();
        String expiredToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI1RjdCN0FGMTZFNTQ0QjZFQzgyM0REREYwOTlCQjM3NiIsImlwIjoiMDowOjA6MDowOjA6MDoxIiwibnVtYmVycyI6WzEsMl0sInVzZXJBZ2VudCI6IlBvc3RtYW5SdW50aW1lLzcuMzkuMCIsImV4cCI6MTcyNzA1NTEwMSwiaWF0IjoxNzI3MDU1MDQxfQ.iHr1l6cgUc0UolevXTIkba706SD4jMU5pYCGmUdicrM";
        MessageRequest messageRequest = new MessageRequest("The original question was “Please sum the numbers 5,7,8” and the answer is 20.");

        given()
                .headers(
                        "X-Session-ID", userData.getSessionId(),
                        "X-Forwarded-For", userData.getIp(),
                        "User-Agent", userData.getUserAgent()
                )
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .header("X-Api-Token", expiredToken)
                .body(messageRequest)
                .when()
                .post("/human-assured/api/v1/message/answer")
                .then()
                .statusCode(400)
                .body("message", Matchers.equalTo("Token has expired. Please ask for another question."));
    }
}
