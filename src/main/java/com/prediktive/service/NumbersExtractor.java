package com.prediktive.service;

import com.prediktive.domain.AnswerSumExtracted;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class NumbersExtractor {
    private final String regex = "^(.*)?(answer is (\\d+))(.*)?$"; // only the answer is relevant to extract
    private final Pattern pattern = Pattern.compile(regex);

    public AnswerSumExtracted extractNumbersFromText(String sentence) {

        Matcher matcher = pattern.matcher(sentence);

        if (matcher.find()) {
            // Get the matched numbers string
            String resultToCheck = matcher.group(3);

            return AnswerSumExtracted.builder().sum(Integer.parseInt(resultToCheck)).build();
        } else {
            return null;
        }
    }
}