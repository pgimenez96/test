package com.pgimenez.test.api.service;

import java.util.List;

import com.pgimenez.test.api.dto.ApiResponseDto;
import com.pgimenez.test.api.exception.ApiException;

public interface ApiService {
    
    /**
     * Método que permite buscar mediante un texto publicaciones de noticias en la página web de ABC
     * @param searchData Texto de busqueda
     * @return Lista de las noticias encontradas en formato JSON
     */
    public List<ApiResponseDto> getPosts(String searchData, boolean returnPhoto) throws ApiException;

}
