package com.prediktive.domain.api;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MessageResponse {
    private String message;
}
