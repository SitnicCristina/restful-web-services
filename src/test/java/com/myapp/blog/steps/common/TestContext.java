package com.myapp.blog.steps.common;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class TestContext {

    private ResponseEntity<String> lastResponse;

    public ResponseEntity<String> getLastResponse() {
        return lastResponse;
    }

    public void setLastResponse(ResponseEntity<String> response) {
        this.lastResponse = response;
    }
}
