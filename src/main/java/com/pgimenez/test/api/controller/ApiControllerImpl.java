package com.pgimenez.test.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pgimenez.test.api.dto.ApiResponseDto;
import com.pgimenez.test.api.dto.PostRequest;
import com.pgimenez.test.api.service.ApiService;
@Validated
@RestController("apiController")
@RequestMapping("/api")
public class ApiControllerImpl implements ApiController {

    private ApiService service;
    
    @Autowired
    public ApiControllerImpl(ApiService service) {
        this.service = service;
    }
    
    @Override
    @GetMapping(value = "/consulta")
    public List<ApiResponseDto> searchPost(
            @RequestParam(value = "q", required = false) String searchData,
            @RequestParam(value = "f", required = true) boolean returnPhoto
        ) throws Exception {
        return service.getPosts(searchData, returnPhoto);
    }

    @Override
    @PostMapping(value = "/post")
    public ResponseEntity<Object> validationPost(@RequestBody PostRequest postRequest) {
        return ResponseEntity.ok().body(postRequest);
    }

}
