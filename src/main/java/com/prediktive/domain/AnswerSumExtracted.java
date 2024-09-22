package com.prediktive.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Builder
@Getter
public class AnswerSumExtracted {
    private final Integer sum;
}
