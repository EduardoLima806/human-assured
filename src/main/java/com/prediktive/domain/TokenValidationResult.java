package com.prediktive.domain;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TokenValidationResult {

    private final boolean isValid;
    private final Integer sum;
}
