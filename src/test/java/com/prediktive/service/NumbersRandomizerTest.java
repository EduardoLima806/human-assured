package com.prediktive.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class NumbersRandomizerTest {

    @Autowired
    private NumbersRandomizer numbersRandomizer;

    @Test
    void shouldRandom2or3Numbers() {
        List<Integer> integers = numbersRandomizer.random2or3Numbers();
        assertNotNull(integers);
        assertTrue(integers.size() >= 2 && integers.size() <= 3);
    }
}
