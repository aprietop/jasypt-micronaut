package io.github.aprietop;

import io.micronaut.context.env.PropertyExpressionResolver;
import io.micronaut.core.convert.ConversionService;
import io.micronaut.core.value.PropertyResolver;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

import java.util.Optional;

public class JasyptPropertyExpressionResolver implements PropertyExpressionResolver {

    private static final String JASYPT_PREFIX = "ENC(";
    public static final String JASYPT_PASSWORD_PROPERTY_NAME = "jasypt.encryption.password";
    private final StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();


    @Override
    public <T> Optional<T> resolve(PropertyResolver propertyResolver,
                                   ConversionService conversionService,
                                   String expression,
                                   Class<T> requiredType) {
        if (expression.startsWith(JASYPT_PREFIX)) {
            if (!encryptor.isInitialized()) {
                propertyResolver.getProperty(JASYPT_PASSWORD_PROPERTY_NAME, String.class)
                        .ifPresent(encryptor::setPassword);
            }
            String decrypted = this.encryptor.decrypt(expression.substring(JASYPT_PREFIX.length(), expression.length() - 1));
            return Optional.of(conversionService.convertRequired(decrypted, requiredType));
        }
        return Optional.empty();
    }
}
