package com.myapp.blog.config;

import io.cucumber.spring.CucumberContextConfiguration;

import com.myapp.blog.RestfulWebServicesApplication;
import org.springframework.boot.test.context.SpringBootTest;

@CucumberContextConfiguration
@SpringBootTest(classes = RestfulWebServicesApplication.class)
public class CucumberSpringConfiguration {
}
