package com.prediktive.service;

import com.prediktive.domain.AnswerSumExtracted;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class NumbersExtractorTest {

    @Autowired
    private NumbersExtractor numbersExtractor;

    @Test
    void shouldExtractNumbersFromTextValidFormat1() {
        String validSentence = "Great. The original question was “Please sum the numbers 9,5,3” and the answer is 15.";
        AnswerSumExtracted answerSumExtracted = numbersExtractor.extractNumbersFromText(validSentence);
        assertNotNull(answerSumExtracted);
        Integer expectedSum = 15;
        assertEquals(expectedSum, answerSumExtracted.getSum());
    }

    @Test
    void shouldExtractNumbersFromTextValidFormat2() {
        String validSentence = "The answer is 20.";
        AnswerSumExtracted answerSumExtracted = numbersExtractor.extractNumbersFromText(validSentence);
        assertNotNull(answerSumExtracted);
        Integer expectedSum = 20;
        assertEquals(expectedSum, answerSumExtracted.getSum());
    }

    @Test
    void shouldExtractNumbersFromTextValidFormat3NoCheckTheSum() {
        String validSentence = "The original question was “Please sum the numbers 10,5,3” and the answer is 20.";
        AnswerSumExtracted answerSumExtracted = numbersExtractor.extractNumbersFromText(validSentence);
        assertNotNull(answerSumExtracted);
        Integer expectedSum = 20;
        assertEquals(expectedSum, answerSumExtracted.getSum()); // The previous question is just a reference. Only extract the sum is relevant (performance reasons)
    }
}
