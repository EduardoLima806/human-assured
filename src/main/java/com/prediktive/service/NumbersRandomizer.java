package com.prediktive.service;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

@Component
public class NumbersRandomizer {

    private final Integer MAX_NUMBER = 10; // max number generated

    public List<Integer> random2or3Numbers() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        int numbersToGenerate = random.nextInt(3) + 2;
        return IntStream.range(0, numbersToGenerate)
                .map(e -> random.nextInt(MAX_NUMBER) + 1)
                .boxed()
                .toList();
    }

}
