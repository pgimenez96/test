package com.pgimenez.test.api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.pgimenez.test.api.dto.ApiResponseDto;
import com.pgimenez.test.api.dto.PostRequest;

public interface ApiController {
    
    public List<ApiResponseDto> searchPost(String searchData, boolean returnPhoto) throws Exception;

    public ResponseEntity<Object> validationPost(PostRequest postRequest);

}
