/*
 * Copyright 2024 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.aprietop;

import io.micronaut.context.env.PropertyExpressionResolver;
import io.micronaut.core.convert.ConversionService;
import io.micronaut.core.value.PropertyResolver;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

import java.util.Optional;

/**
 * Jasypt property resolver for decrypting properties in application property files like {@code application.properties} or {@code application.yml} using Jasypt.
 * <p>
 * This resolver requires a Jasypt encryption password to be specified in the properties file with the default key {@code jasypt.encryption.password}.
 * The password is used to decrypt any properties that are encrypted using Jasypt.
 * <p>
 * Example configuration in {@code application.yml}:
 *
 * <pre>
 * jasypt:
 *   encryption:
 *     password: your-encryption-password
 * </pre>
 * <p>
 * Example encrypted property:
 *
 * <pre>
 * my:
 *   secret:
 *     property: ${ENC(GO9jHf7yVuP4E7oGzQmYkQ==)}
 * </pre>
 * <p>
 * This class provides methods to check for the presence of encrypted properties and decrypt them as needed.
 * <p>
 * Usage example:
 *
 * <pre>
 * {@code
 * @Singleton
 * public class MyService {
 *
 *     @Value("${my.secret.property}")
 *     private String secretProperty;
 *
 *     public void printSecret() {
 *         System.out.println("Decrypted property value: " + secretProperty);
 *     }
 * }
 * }
 * </pre>
 *
 * @author Armando Prieto
 * @since 0.1.0
 */
public class JasyptPropertyExpressionResolver implements PropertyExpressionResolver {

    private static final String JASYPT_PREFIX = "ENC(";
    public static final String JASYPT_PASSWORD_PROPERTY_NAME = "jasypt.encryption.password";
    private final StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();

    @Override
    public <T> Optional<T> resolve(PropertyResolver propertyResolver,
                                   ConversionService conversionService,
                                   String expression,
                                   Class<T> requiredType) {
        if (isJasyptExpression(expression)) {
            if (!encryptor.isInitialized()) {
                propertyResolver.getProperty(JASYPT_PASSWORD_PROPERTY_NAME, String.class)
                        .ifPresent(encryptor::setPassword);
            }
            return Optional.of(conversionService.convertRequired(decrypt(expression), requiredType));
        }
        return Optional.empty();
    }

    boolean isJasyptExpression(String expression) {
        return expression.startsWith(JASYPT_PREFIX);
    }

    String decrypt(String expression) {
        return this.encryptor.decrypt(extractValue(expression));
    }

    String extractValue(String expression) {
        return expression.substring(JASYPT_PREFIX.length(), expression.length() - 1);
    }
}
