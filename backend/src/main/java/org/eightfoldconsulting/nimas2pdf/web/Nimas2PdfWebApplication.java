package org.eightfoldconsulting.nimas2pdf.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Main Spring Boot application for NIMAS2PDF Web Application.
 * This application provides REST endpoints for NIMAS to PDF conversion
 * and serves a React frontend.
 */
@SpringBootApplication
@EnableAsync
public class Nimas2PdfWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(Nimas2PdfWebApplication.class, args);
    }
}
