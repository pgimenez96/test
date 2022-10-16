package com.pgimenez.test.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pgimenez.test.api.dto.ApiResponseDto;
import com.pgimenez.test.api.service.ApiService;

@RestController("ApiController")
@RequestMapping("/api")
public class ApiControllerImpl implements ApiController{

    @Autowired
    private ApiService service;
    
    @Override
    @GetMapping(value = "/consulta")
    public List<ApiResponseDto> searchPost(
            @RequestParam(value = "q", required = false) String searchData,
            @RequestParam(value = "f", required = false) boolean returnPhoto
        ) throws Exception {

        return service.getPosts(searchData, returnPhoto);

    }

}
