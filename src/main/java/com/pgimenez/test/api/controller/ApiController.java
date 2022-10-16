package com.pgimenez.test.api.controller;

import java.util.List;

import com.pgimenez.test.api.dto.ApiResponseDto;

public interface ApiController {
    
    public List<ApiResponseDto> searchPost(String searchData, boolean returnPhoto) throws Exception;

}
