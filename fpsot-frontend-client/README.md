# fpsot-frontend-client

Fpsot Frontend OAS
- API version: 0.0.1-SNAPSHOT

Open API Specification REST for FPSOT Frontend Notify

  For more information, please visit [http://www.fastenmanufacturing.eu](http://www.fastenmanufacturing.eu)

*Automatically generated by the [Swagger Codegen](https://github.com/swagger-api/swagger-codegen)*


## Requirements

Building the API client library requires:
1. Java 1.7+
2. Maven/Gradle

## Installation

To install the API client library to your local Maven repository, simply execute:

```shell
mvn clean install
```

To deploy it to a remote Maven repository instead, configure the settings of the repository and execute:

```shell
mvn clean deploy
```

Refer to the [OSSRH Guide](http://central.sonatype.org/pages/ossrh-guide.html) for more information.

### Maven users

Add this dependency to your project's POM:

```xml
<dependency>
  <groupId>com.fasten.wp4</groupId>
  <artifactId>fpsot-frontend-client</artifactId>
  <version>0.0.1-SNAPSHOT</version>
  <scope>compile</scope>
</dependency>
```

### Gradle users

Add this dependency to your project's build file:

```groovy
compile "com.fasten.wp4:fpsot-frontend-client:0.0.1-SNAPSHOT"
```

### Others

At first generate the JAR by executing:

```shell
mvn clean package
```

Then manually install the following JARs:

* `target/fpsot-frontend-client-0.0.1-SNAPSHOT.jar`
* `target/lib/*.jar`

## Getting Started

Please follow the [installation](#installation) instruction and execute the following Java code:

```java

import com.fasten.wp4.fpsot.frontend.client.invoker.*;
import com.fasten.wp4.fpsot.frontend.client.invoker.auth.*;
import com.fasten.wp4.fpsot.frontend.client.model.*;
import com.fasten.wp4.fpsot.frontend.client.api.FrontendNotifyControllerApi;

import java.io.File;
import java.util.*;

public class FrontendNotifyControllerApiExample {

    public static void main(String[] args) {
        
        FrontendNotifyControllerApi apiInstance = new FrontendNotifyControllerApi();
        String eventName = "\"return_historical_demand\""; // String | eventName
        try {
            apiInstance.kafkaTopicNameGet(eventName);
        } catch (ApiException e) {
            System.err.println("Exception when calling FrontendNotifyControllerApi#kafkaTopicNameGet");
            e.printStackTrace();
        }
    }
}

```

## Documentation for API Endpoints

All URIs are relative to *http://localhost:8087/fpsot/notify*

Class | Method | HTTP request | Description
------------ | ------------- | ------------- | -------------
*FrontendNotifyControllerApi* | [**kafkaTopicNameGet**](docs/FrontendNotifyControllerApi.md#kafkaTopicNameGet) | **GET** /kafka/{topicName} | 


## Documentation for Models



## Documentation for Authorization

All endpoints do not require authorization.
Authentication schemes defined for the API:

## Recommendation

It's recommended to create an instance of `ApiClient` per thread in a multithreaded environment to avoid any potential issues.

## Author

Fasten-wp4@lists.inesctec.pt

