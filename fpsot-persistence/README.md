# persistence

Persistence components

- Library version: 0.0.1-SNAPSHOT

Esta é a Biblioteca Spring Autoconfigurable que adiciona os itens de persistencia ao projeto

*Para uso em Web Services e Apps baseados em Sring e Spring Boot*

[TOC]

## Exigências

A compilação exige:

1. Java 1.8
2. Maven

## Instalação

### Maven 

Adicione a dependencia no seu pom.xml:

```xml
<dependency>
  	<groupId>com.fasten.wp4</groupId>
	<artifactId>persistence</artifactId>
	<version>0.0.1-SNAPSHOT</version>
  	<scope>compile</scope>
</dependency>
```

### Configuração das propriedades
As propriedades devem ser configuradas ou sobrescritas em seu src/main/resources/application.properties

#### Valores definidos :

```properties
# Show all queries
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.format_sql=false
#logging.level.org.hibernate.type=trace


## Spring DATASOURCE (DataSourceAutoConfiguration & DataSourceProperties)
spring.datasource.url=${DATASOURCE_URL}
spring.datasource.username=${DATASOURCE_USER}
spring.datasource.password=${DATASOURCE_PASSWORD}

# The SQL dialect makes Hibernate generate better SQL for the chosen database
spring.jpa.properties.hibernate.dialect=com.fasten.wp4.database.config.PostgreSQL95ArrayAndJsonDialect
spring.jpa.properties.hibernate.temp.use_jdbc_metadata_defaults=false
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQL9Dialect

# Hibernate ddl auto (create, create-drop, validate, update)
spring.jpa.hibernate.ddl-auto=update

# create.sql
#spring.jpa.generate-ddl=true
#spring.jpa.properties.javax.persistence.schema-generation.create-source=metadata
#spring.jpa.properties.javax.persistence.schema-generation.scripts.action=create
#spring.jpa.properties.javax.persistence.schema-generation.scripts.create-target=create.sql

# data.sql init database
#spring.datasource.initialization-mode=always
#spring.datasource.data=classpath:data_postgres.sql
#spring.datasource.sql-script-encoding=UTF-8
```
