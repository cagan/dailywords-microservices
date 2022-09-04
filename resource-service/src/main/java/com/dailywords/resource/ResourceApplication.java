package com.dailywords.resource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Scanner;

@SpringBootApplication
public class ResourceApplication {

	public static void main(String[] args) throws IOException {
		SpringApplication.run(ResourceApplication.class, args);
	}

}
