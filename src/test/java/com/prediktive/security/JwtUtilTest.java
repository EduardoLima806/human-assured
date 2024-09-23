package com.prediktive.security;

import com.prediktive.domain.TokenValidationResult;
import com.prediktive.domain.UserData;
import com.prediktive.domain.UserDataMock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class JwtUtilTest {

    @Autowired
    private JwtUtil jwtUtil;

    @Test
    void shouldGenerateAndValidateToken() {
        UserData userData = UserDataMock.createUserDataInstance();
        String token = jwtUtil.generateToken(userData);
        assertNotNull(token);

        TokenValidationResult tokenValidationResult = jwtUtil.validateToken(token, userData);
        assertTrue(tokenValidationResult.isValid());
        assertEquals(UserDataMock.getSum(), tokenValidationResult.getSum());

        String extractedSubject = jwtUtil.extractSubject(token);
        assertEquals(userData.getSessionId(), extractedSubject);
    }

    @Test
    void shouldGenerateAndTokenInvalidSessionIDChanged() {
        UserData userData = UserDataMock.createUserDataInstance();
        String token = jwtUtil.generateToken(userData);
        assertNotNull(token);

        UserData userDataOtherInstance = UserDataMock.createUserDataDiffSessionIdInstance();

        TokenValidationResult tokenValidationResult = jwtUtil.validateToken(token, userDataOtherInstance);
        assertFalse(tokenValidationResult.isValid());
    }

    @Test
    void shouldGenerateAndTokenInvalidIPChanged() {
        UserData userData = UserDataMock.createUserDataInstance();
        String token = jwtUtil.generateToken(userData);
        assertNotNull(token);

        UserData userDataOtherInstance = UserDataMock.createUserDataDiffIPInstance();

        TokenValidationResult tokenValidationResult = jwtUtil.validateToken(token, userDataOtherInstance);
        assertFalse(tokenValidationResult.isValid());
    }

    @Test
    void shouldGenerateAndTokenInvalidUserAgentChanged() {
        UserData userData = UserDataMock.createUserDataInstance();
        String token = jwtUtil.generateToken(userData);
        assertNotNull(token);

        UserData userDataOtherInstance = UserDataMock.createUserDataDiffUserAgentInstance();

        TokenValidationResult tokenValidationResult = jwtUtil.validateToken(token, userDataOtherInstance);
        assertFalse(tokenValidationResult.isValid());
    }
}
