package com.prediktive.domain;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class UserData {

    private final String sessionId;
    private final String ip;
    private final String userAgent;
    private final List<Integer> numbers;
}
