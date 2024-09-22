package com.prediktive.controller;

import com.prediktive.config.JwtUtil;
import com.prediktive.domain.AnswerSumExtracted;
import com.prediktive.service.NumbersExtractor;
import com.prediktive.service.NumbersRandomizer;
import com.prediktive.domain.TokenValidationResult;
import com.prediktive.domain.UserData;
import com.prediktive.domain.api.MessageRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("message")
@RequiredArgsConstructor
@Slf4j
public class MessageController {

    private final JwtUtil jwtUtil;
    private final NumbersRandomizer numbersRandomizer;
    private final NumbersExtractor numbersExtractor;

    @GetMapping("/ask")
    public ResponseEntity<String> provideNewQuestion(HttpServletRequest request) {

        List<Integer> randomized = numbersRandomizer.random2or3Numbers();
        UserData userData = createUserData(request, randomized);

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Api-Token", jwtUtil.generateToken(userData));

        String randomizedStr = randomized.stream().map(String::valueOf)
                .collect(Collectors.joining(","));

        return ResponseEntity.ok().headers(headers).body("Here you go, solve the question: \"Please sum the numbers " + randomizedStr + "\"");
    }

    @PostMapping("/answer")
    public ResponseEntity<String> answerQuestion(HttpServletRequest request, @RequestBody MessageRequest messageRequest) {

        String apiToken = request.getHeader("X-Api-Token");
        UserData userData = createUserData(request, null);
        TokenValidationResult tokenValidationResult = jwtUtil.validateToken(apiToken, userData);

        if (!tokenValidationResult.isValid()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token is not valid.");
        }

        AnswerSumExtracted answerExtractedNumbers = numbersExtractor.extractNumbersFromText(messageRequest.getMessage());

        if (answerExtractedNumbers == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Answer format wrong. Valid example: The original question was “Please sum the numbers 9,5,3” and the answer is 15.");
        }

        log.info("Sum from the previous question: {}.", tokenValidationResult.getSum());
        log.info("Extracted sum from answer: {}.", answerExtractedNumbers.getSum());

        if (!tokenValidationResult.getSum().equals(answerExtractedNumbers.getSum())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("That’s wrong. Please try again.");
        }

        return ResponseEntity.ok("That’s great!");
    }

    private static UserData createUserData(HttpServletRequest request, List<Integer> numbers) {
        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        }

        return UserData.builder()
                .sessionId(ipAddress)
                .ip(request.getRemoteAddr())
                .userAgent(request.getHeader("User-Agent"))
                .numbers(numbers)
                .build();
    }
}
