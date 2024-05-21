
## Micronaut Jasypt Library

This Micronaut library provides easy-to-use integration with Jasypt for encrypting and decrypting properties in your Micronaut applications. Inspired by [jasypt-spring-boot](https://github.com/ulisesbocchio/jasypt-spring-boot), this library simplifies the setup and usage of Jasypt in Micronaut projects.

### Features

- Seamless integration with Micronaut configuration.
- Encrypt and decrypt properties in application configuration files.
- Support for various encryption algorithms and configuration options.

### Getting Started

#### 1. Add the Dependency

To use the Micronaut Jasypt library, add the following dependency to your `build.gradle` file:

```groovy
dependencies {
    implementation 'io.github.aprietop:jasypt-micronaut:1.0.0'
}
```

#### 2. Configuration

Configure the library in your `application.yml` or `application.properties` file. The essential configuration properties include the encryption password and the algorithm.

##### application.yml

```yaml
jasypt:
  encryptor:
    password: your-encryption-password
```

##### application.properties

```properties
jasypt.encryptor.password=your-encryption-password
```

#### 3. Usage

Encrypt your sensitive properties using Jasypt CLI or any other method. Encrypted properties should be prefixed with `ENC(` and suffixed with `)`.

##### Example

Encrypt a property value:

```sh
jasypt encrypt input="my-secret-value" password="your-encryption-password"
```

Output:

```
ENC(GO9jHf7yVuP4E7oGzQmYkQ==)
```

Use the encrypted value in your `application.yml` or `application.properties`:

```yaml
my:
  secret:
    property: ${ENC(GO9jHf7yVuP4E7oGzQmYkQ==)}
```

```properties
my.secret.property=${ENC(GO9jHf7yVuP4E7oGzQmYkQ==)}
```

#### 4. Accessing Decrypted Properties

You can inject and use the decrypted properties in your Micronaut beans just like any other configuration property:

```java
import io.micronaut.context.annotation.Value;
import jakarta.inject.Singleton;

@Singleton
public class MyService {

    @Value("${my.secret.property}")
    private String secretProperty;

    public void printSecret() {
        System.out.println("Decrypted property value: " + secretProperty);
    }
}
```

### Contributions

Contributions are welcome! Please fork the repository and submit pull requests for any enhancements or bug fixes.

### License

This project is licensed under the Apache-2.0 License. See the [LICENSE](LICENSE) file for details.

---

By following the steps outlined in this README, you should be able to easily integrate and use the Micronaut Jasypt library in your Micronaut applications to securely manage sensitive properties.
