package io.github.aprietop;

import io.micronaut.context.annotation.Value;
import io.micronaut.core.convert.ConversionService;
import io.micronaut.core.value.PropertyResolver;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest
class JasyptPropertyExpressionResolverTest {

    private final PropertyResolver propertyResolver;
    private final ConversionService conversionService;
    private final JasyptPropertyExpressionResolver resolver;
    private final String value;
    private final String expression;


    public JasyptPropertyExpressionResolverTest(PropertyResolver propertyResolver,
                                                ConversionService conversionService,
                                                @Value("jasypt.encrypted-value") String value) {
        this.propertyResolver = propertyResolver;
        this.conversionService = conversionService;
        this.value = value;
        expression = "ENC(%s)".formatted(value);
        resolver = new JasyptPropertyExpressionResolver();
    }

    @Test
    void isJasyptExpression() {
        assertTrue(resolver.isJasyptExpression(expression));
    }

    @Test
    void extract() {
        assertEquals(value, resolver.extractValue(expression));
    }

    @Test
    void decrypt() {
        Optional<String> resolved = resolver.resolve(propertyResolver, conversionService, expression, String.class);
        assertTrue(resolved.isPresent());
        assertEquals("some-text", resolved.get());
    }

}
