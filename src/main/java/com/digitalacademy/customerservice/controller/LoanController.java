package com.digitalacademy.customerservice.controller;

import com.digitalacademy.customerservice.api.LoanServiceApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/loan")
public class LoanController {
    private LoanServiceApi loanServiceApi;

    @Autowired
    private LoanController(LoanServiceApi loanServiceApi) {
        this.loanServiceApi = loanServiceApi;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getLoanInfo(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(loanServiceApi.getLoanInfo(id));
    }
}
