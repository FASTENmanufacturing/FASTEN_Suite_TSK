Database OAS:

java -jar swagger-codegen-cli-2.4.7.jar generate -DapiDocs=false -DmodelDocs=false -D io.swagger.parser.util.RemoteUrl.trustAll=true -D hideGenerationTimestamp=true -i http://localhost:8081/v2/api-docs --api-package com.fasten.wp4.database.client.api --model-package com.fasten.wp4.database.client.model --invoker-package com.fasten.wp4.database.client.invoker --group-id com.fasten.wp4 --artifact-id database-client --artifact-version 0.0.1-SNAPSHOT -l java --additional-properties dateLibrary=legacy,java8=true,serializableModel=true,booleanGetterPrefix -o ../database-client

Open Route Service geolocate:

java -jar swagger-codegen-cli-2.4.7.jar generate -DapiDocs=false -DmodelDocs=false -D io.swagger.parser.util.RemoteUrl.trustAll=true -D hideGenerationTimestamp=true -i geocode-oas.json --api-package org.ors.geocode.client.api --model-package org.ors.geocode.client.model --invoker-package org.ors.geocode.client.invoker --group-id org.ors --artifact-id geocode-client --artifact-version 0.0.1-SNAPSHOT -l java --additional-properties dateLibrary=legacy,java8=true,serializableModel=true,booleanGetterPrefix -o ../geocode-client

Open Route Service matrix:

java -jar swagger-codegen-cli-2.4.7.jar generate -DapiDocs=false -DmodelDocs=false -D io.swagger.parser.util.RemoteUrl.trustAll=true -D hideGenerationTimestamp=true -i matrix-oas.json --api-package org.ors.matrix.client.api --model-package org.ors.matrix.client.model --invoker-package org.ors.matrix.client.invoker --group-id org.ors --artifact-id matrix-client --artifact-version 0.0.1-SNAPSHOT -l java --additional-properties dateLibrary=legacy,java8=true,serializableModel=true,booleanGetterPrefix -o ../matrix-client

Previsor OAS:

java -jar swagger-codegen-cli-2.4.7.jar generate -DapiDocs=false -DmodelDocs=false -D io.swagger.parser.util.RemoteUrl.trustAll=true -D hideGenerationTimestamp=true -i predictive-oas.json --api-package com.fasten.wp4.predictive.client.api --model-package com.fasten.wp4.predictive.client.model --invoker-package com.fasten.wp4.predictive.client.invoker --group-id com.fasten.wp4 --artifact-id predictive-client --artifact-version 0.0.1-SNAPSHOT -l java --additional-properties dateLibrary=legacy,java8=true,serializableModel=true,booleanGetterPrefix -o ../predictive-client

Operational Optimizator OAS:

java -jar swagger-codegen-cli-2.4.7.jar generate -DapiDocs=false -DmodelDocs=false -D io.swagger.parser.util.RemoteUrl.trustAll=true -D hideGenerationTimestamp=true -i operational-optimization-oas.json --api-package com.fasten.wp4.optimizator.operational.client.api --model-package com.fasten.wp4.optimizator.operational.client.model --invoker-package com.fasten.wp4.optimizator.operational.client.invoker --group-id com.fasten.wp4 --artifact-id optimizator-operational-client --artifact-version 0.0.1-SNAPSHOT -l java --additional-properties dateLibrary=legacy,java8=true,serializableModel=true,booleanGetterPrefix -o ../optimizator-operational-client 
 
Tactical Optimizator OAS:

java -jar swagger-codegen-cli-2.4.7.jar generate -DapiDocs=false -DmodelDocs=false -D io.swagger.parser.util.RemoteUrl.trustAll=true -D hideGenerationTimestamp=true -i http://localhost:5050/v2/api-docs --api-package com.fasten.wp4.optimizator.tactical.client.api --model-package com.fasten.wp4.optimizator.tactical.client.model --invoker-package com.fasten.wp4.optimizator.tactical.client.invoker --group-id com.fasten.wp4 --artifact-id optimizator-tactical-client --artifact-version 0.0.1-SNAPSHOT -l java --additional-properties dateLibrary=legacy,java8=true,serializableModel=true,booleanGetterPrefix -o ../optimizator-tactical-client1

Email OAS:

java -jar swagger-codegen-cli-2.4.7.jar generate -DapiDocs=false -DmodelDocs=false -D io.swagger.parser.util.RemoteUrl.trustAll=true -D hideGenerationTimestamp=true -i http://localhost:8088/v2/api-docs --api-package com.fasten.wp4.email.client.api --model-package com.fasten.wp4.email.client.model --invoker-package com.fasten.wp4.email.client.invoker --group-id com.fasten.wp4 --artifact-id email-client --artifact-version 0.0.1-SNAPSHOT -l java --additional-properties dateLibrary=legacy,java8=true,serializableModel=true,booleanGetterPrefix -o ../email-client

Directions OAS:

java -jar swagger-codegen-cli-2.4.7.jar generate -DapiDocs=false -DmodelDocs=false -D io.swagger.parser.util.RemoteUrl.trustAll=true -D hideGenerationTimestamp=true -i directions-oas.json --api-package org.ors.direction.operational.client.api --model-package org.ors.direction.client.model --invoker-package org.ors.direction.client.invoker --group-id org.ors --artifact-id direction-client --artifact-version 0.0.1-SNAPSHOT -l java --additional-properties dateLibrary=legacy,java8=true,serializableModel=true,booleanGetterPrefix -o ../direction-client
 
Fpsot Frontend Client:
java -jar swagger-codegen-cli-2.4.7.jar generate -DapiDocs=false -DmodelDocs=false -D io.swagger.parser.util.RemoteUrl.trustAll=true -D hideGenerationTimestamp=true -i fpsot-frontend-client.json --api-package com.fasten.wp4.fpsot.frontend.client.api --model-package com.fasten.wp4.fpsot.frontend.client.model --invoker-package com.fasten.wp4.fpsot.frontend.client.invoker --group-id com.fasten.wp4 --artifact-id fpsot-frontend-client --artifact-version 0.0.1-SNAPSHOT -l java --additional-properties dateLibrary=legacy,java8=true,serializableModel=true,booleanGetterPrefix -o ../fpsot-frontend-client

IoT Kafka Client:
java -jar swagger-codegen-cli-2.4.7.jar generate -DapiDocs=false -DmodelDocs=false -D io.swagger.parser.util.RemoteUrl.trustAll=true -D hideGenerationTimestamp=true -i http://localhost:8082/v2/api-docs --api-package com.fasten.wp4.iot.kafka.client.api --model-package com.fasten.wp4.iot.kafka.client.model --invoker-package com.fasten.wp4.iot.kafka.client.invoker --group-id com.fasten.wp4 --artifact-id iot-kafka-client --artifact-version 0.0.1-SNAPSHOT -l java --additional-properties dateLibrary=legacy,java8=true,serializableModel=true,booleanGetterPrefix -o ../iot-kafka-client

Probability Distribution
java -jar swagger-codegen-cli-2.4.7.jar generate -DapiDocs=false -DmodelDocs=false -D io.swagger.parser.util.RemoteUrl.trustAll=true -D hideGenerationTimestamp=true -i probability-distribution-oas.json --api-package com.fasten.wp4.probabilitydistribution.client.api --model-package com.fasten.wp4.probabilitydistribution.client.model --invoker-package com.fasten.wp4.probabilitydistribution.client.invoker --group-id com.fasten.wp4 --artifact-id probability-distribution-client --artifact-version 0.0.1-SNAPSHOT -l java --additional-properties dateLibrary=legacy,java8=true,serializableModel=true,booleanGetterPrefix -o ../probability-distribution-client 

Fpsot OAS:
java -jar swagger-codegen-cli-2.4.7.jar generate -DapiDocs=false -DmodelDocs=false -D io.swagger.parser.util.RemoteUrl.trustAll=true -D hideGenerationTimestamp=true -i http://localhost:3030/v2/api-docs --api-package com.fasten.wp4.fpsot.client.api --model-package com.fasten.wp4.fpsot.client.model --invoker-package com.fasten.wp4.fpsot.client.invoker --group-id com.fasten.wp4 --artifact-id fpsot-client --artifact-version 0.0.1-SNAPSHOT -l java --additional-properties dateLibrary=legacy,java8=true,serializableModel=true,booleanGetterPrefix -o ../fpsot-client

Fasten Gateway:
java -jar swagger-codegen-cli-2.4.7.jar generate -DapiDocs=false -DmodelDocs=false -D io.swagger.parser.util.RemoteUrl.trustAll=true -D hideGenerationTimestamp=true -i http://ec2-3-88-109-165.compute-1.amazonaws.com:5000/api/v1/swagger.json --api-package com.fasten.wp5.fastengateway.client.api --model-package com.fasten.wp5.fastengateway.client.model --invoker-package com.fasten.wp5.fastengateway.client.invoker --group-id com.fasten.wp5 --artifact-id fastengateway-client --artifact-version 0.0.1-SNAPSHOT -l java --additional-properties dateLibrary=legacy,java8=true,serializableModel=true,booleanGetterPrefix -o ../fastengateway-client

Orion client (ngsi v2 fiware broker):
java -jar swagger-codegen-cli-2.4.7.jar generate -DapiDocs=false -DmodelDocs=false -D io.swagger.parser.util.RemoteUrl.trustAll=true -D hideGenerationTimestamp=true -i ngsi-v2.json --api-package com.fasten.wp4.orion.client.api --model-package com.fasten.wp4.orion.client.model --invoker-package com.fasten.wp4.orion.client.invoker --group-id com.fasten.wp4 --artifact-id orion-client --artifact-version 0.0.1-SNAPSHOT -l java --additional-properties dateLibrary=legacy,java8=true,serializableModel=true,booleanGetterPrefix -o ../orion-client1
