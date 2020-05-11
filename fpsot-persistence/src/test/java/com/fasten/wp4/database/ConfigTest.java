package com.fasten.wp4.database;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootApplication
class TestApplication {
	public static void main(String[] args) {
		SpringApplication.run(TestApplication.class, args);
	}
}

@Ignore
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplication.class)
public class ConfigTest
{
	@Autowired
	private ApplicationContext context;

	@Test
	public void testBeans() throws Exception
	{

		System.out.println("beans: " + context.getBeanDefinitionCount());
		for (String name : context.getBeanDefinitionNames()) {
			System.out.println(name);
		}
	}


}


