package com.pgimenez.test.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponseDto {
    
    private String date;
    private String link;
    private String photoLink;
    private String title;
    private String summary;
    private String photoContent;
    private String contentTypePhoto;

}
