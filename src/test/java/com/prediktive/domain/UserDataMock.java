package com.prediktive.domain;

import java.util.List;

public class UserDataMock {

    private static final String SESSION_ID = "0a5b64d7594a6741eb87f40f7588db96e0b514cce6e7b1cba7d59f59353b0c1d";
    private static final String SESSION_ID_1 = "0b5b64d7594a6741eb87f40f7588db96e0b544cce6e7b1cba7d59f59353b0c1f";
    private static final String IP = "127.79.137.171";
    private static final String IP_1 = "127.22.122.17";
    private static final String USER_AGENT = "Mozilla/5.0 (Linux; Linux x86_64) Gecko/20100101 Firefox/53.3";
    private static final String USER_AGENT_1 = "Mozilla/5.0 (compatible; MSIE 8.0; Windows; Windows NT 10.1; WOW64 Trident/4.0)";
    private static final List<Integer> NUMBERS = List.of(5, 7, 8);

    public static UserData createUserDataInstance() {
        return UserData.builder()
                .sessionId(SESSION_ID)
                .ip(IP)
                .userAgent(USER_AGENT)
                .numbers(NUMBERS)
                .build();
    }

    public static Integer getSum() {
        return NUMBERS.stream().mapToInt(Integer::intValue).sum();
    }

    public static UserData createUserDataDiffSessionIdInstance() {
        return UserData.builder()
                .sessionId(SESSION_ID_1)
                .ip(IP)
                .userAgent(USER_AGENT)
                .numbers(NUMBERS)
                .build();
    }

    public static UserData createUserDataDiffIPInstance() {
        return UserData.builder()
                .sessionId(SESSION_ID)
                .ip(IP_1)
                .userAgent(USER_AGENT)
                .numbers(NUMBERS)
                .build();
    }

    public static UserData createUserDataDiffUserAgentInstance() {
        return UserData.builder()
                .sessionId(SESSION_ID)
                .ip(IP)
                .userAgent(USER_AGENT_1)
                .numbers(NUMBERS)
                .build();
    }
}
