package io.github.aprietop;

import io.micronaut.context.annotation.Value;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest
public class JasyptPropertyInjectionTest {

    @Value("${my.secret.property}")
    private String property;

    @Test
    void injection() {
        assertEquals("some-text", property);
    }
}
