package com.fasten.wp4.iot.config;

//import static springfox.documentation.schema.AlternateTypeRules.newRule;

//import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import org.springframework.core.Ordered;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;

//import com.fasterxml.classmate.TypeResolver;
import com.google.common.collect.Sets;

//import springfox.documentation.builders.AlternateTypeBuilder;
//import springfox.documentation.builders.AlternateTypePropertyBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

//	private final TypeResolver typeResolver;
//	private final RepositoryRestConfiguration restConfiguration;
//
//	public SwaggerConfig(TypeResolver typeResolver, RepositoryRestConfiguration restConfiguration) {
//		this.typeResolver = typeResolver;
//		this.restConfiguration = restConfiguration;
//	}

	public static final Contact DEFAULT_CONTACT = new Contact(
			"Flexible and Autonomous Manufacturing Systems for Custom-Designed Products", 
			"http://www.fastenmanufacturing.eu", 
			"Fasten-wp4@lists.inesctec.pt"
			);

	@SuppressWarnings("rawtypes")
	public static final ApiInfo DEFAULT_API_INFO = new ApiInfo(
			"IoT REST OAS", 
			"Open API Specification REST for IoT event management", 
			"0.0.1-SNAPSHOT",
			"urn:tos", 
			DEFAULT_CONTACT, 
			"Apache 2.0", 
			"http://www.apache.org/licenses/LICENSE-2.0", 
			new ArrayList<VendorExtension>()
			);

	private static final Set<String> DEFAULT_PRODUCES_AND_CONSUMES = 
			new HashSet<String>(Arrays.asList("application/json"));

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.fasten.wp4"))
				.paths(PathSelectors.any())
				.build()
				.directModelSubstitute(LocalDate.class, String.class)
				.directModelSubstitute(Calendar.class, Date.class)
				.genericModelSubstitutes(ResponseEntity.class, Resource.class)
				.apiInfo(DEFAULT_API_INFO)
				.protocols(Sets.newHashSet("http"))
				.produces(DEFAULT_PRODUCES_AND_CONSUMES)
				.consumes(DEFAULT_PRODUCES_AND_CONSUMES)
				.forCodeGeneration(true)
				/*.alternateTypeRules(newRule(typeResolver.resolve(Pageable.class),
						pageableMixin(restConfiguration),
						Ordered.HIGHEST_PRECEDENCE))*/;
	}

	/*
	 * from https://stackoverflow.com/questions/35404329/swagger-documentation-for-spring-pageable-interface
	 * https://springfox.github.io/springfox/docs/current/#creating-a-convention
	 * https://github.com/springfox/springfox/issues/2623
	 * https://github.com/springfox/springfox/blob/ef1721afc4c910675d9032bee59aea8e75e06d27/springfox-data-rest/src/main/java/springfox/documentation/spring/data/rest/configuration/SpringDataRestConfiguration.java#L46-L64
	 * add <groupId>io.springfox</groupId>
		    <artifactId>springfox-data-rest</artifactId>
	 */
//	private Type pageableMixin(RepositoryRestConfiguration restConfiguration) {
//		return new AlternateTypeBuilder()
//				.fullyQualifiedClassName(
//						String.format("%s.generated.%s",
//								Pageable.class.getPackage().getName(),
//								Pageable.class.getSimpleName()))
//				.withProperties(Arrays.asList(
//						property(Integer.class,
//								restConfiguration.getPageParamName()),
//						property(Integer.class,
//								restConfiguration.getLimitParamName()),
//						property(String.class,
//								restConfiguration.getSortParamName())
//						))
//				.build();
//	}
//
//	private AlternateTypePropertyBuilder property(Class<?> type, String name) {
//		return new AlternateTypePropertyBuilder()
//				.withName(name)
//				.withType(type)
//				.withCanRead(true)
//				.withCanWrite(true);
//	}


}