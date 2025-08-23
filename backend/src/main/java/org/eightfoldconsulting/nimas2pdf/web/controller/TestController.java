package org.eightfoldconsulting.nimas2pdf.web.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test")
public class TestController {
    
    @PostMapping("/simple")
    public String simplePost() {
        return "Simple POST endpoint works!";
    }
    
    @GetMapping("/simple")
    public String simpleGet() {
        return "Simple GET endpoint works!";
    }
}
