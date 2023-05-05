package com.ncookie.imad.controller;

import com.ncookie.imad.dto.TestDto;
import com.ncookie.imad.response.TestResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {
    @GetMapping("/")
    public ResponseEntity<?> getTestInfo(@RequestParam Long id) throws Exception {
        TestDto dto = new TestDto(1L);
        return ResponseEntity.ok(TestResponse.from(dto));
    }
}
