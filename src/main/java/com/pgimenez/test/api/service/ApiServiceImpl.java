package com.pgimenez.test.api.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.pgimenez.test.api.dto.ApiResponseDto;
import com.pgimenez.test.api.exception.ApiException;
import com.pgimenez.test.api.exception.ApiExceptionHandler;
import com.pgimenez.test.api.util.ApiUtil;

@Service("ApiService")
public class ApiServiceImpl implements ApiService {

    private static final Logger log = LoggerFactory.getLogger(ApiServiceImpl.class);

    @Override
    public List<ApiResponseDto> getPosts(String searchData, boolean returnPhoto) throws ApiException {

        if (searchData == null || searchData.isEmpty()) {
            throw new ApiException(ApiExceptionHandler.G268, "No se ha especificado parámetro de búsqueda", null);
        }

        try {

            String dataString = getDataFromWeb(searchData);
            if (dataString == null) {
                throw new ApiException(ApiExceptionHandler.G267, "No se pudo obtener publicaciones de la web", null);
            }

            return getPostFromData(dataString, returnPhoto);
            
        } catch (Exception ex) {
            log.debug("Error in getNewPosts: ", ex);

            if (ex instanceof ApiException) {
                throw new ApiException(ex.getMessage(), "error in getPosts - " + ex.getMessage(), ex);
            }

            throw new ApiException(ApiExceptionHandler.G100, "error in getPosts - " + ex.getMessage(), ex);
        }
    }

    // Retorna lista de DTOs con los datos obtenidos del dataString
    private List<ApiResponseDto> getPostFromData(String dataString, boolean returnPhoto) throws ApiException {

        JSONArray postArray = ApiUtil.getJsonArrayFromStringData(dataString);
        if (postArray.length() == 0) {
            throw new ApiException(ApiExceptionHandler.G267, "No se ha encontrado noticias para la busqueda indicada", null);
        }

        try {

            List<ApiResponseDto> responseList = new ArrayList<>();
            for (int i = 0; i < postArray.length(); i++) {
                String data = postArray.getString(i);
                ApiResponseDto responseDto = getResponseData(data, returnPhoto);
                if (responseDto != null) { // Solo si tiene datos agrega a la lista
                    responseList.add(responseDto);
                }
            }
            return responseList;

        } catch (Exception ex) {
            log.debug("Error in getPostFromData: ", ex);

            if (ex instanceof ApiException) {
                throw new ApiException(ex.getMessage(), "error in getPostFromData - " + ex.getMessage(), ex);
            }

            throw new ApiException(ApiExceptionHandler.G100, "error in getPostFromData - " + ex.getMessage(), ex);
        }
    }

    // Retorna DTO con los datos obtenidos de la cadena que recibe por parámetro, en caso de error retorna null
    private ApiResponseDto getResponseData(String data, boolean returnPhoto) {
        try {

            // Convierte la cadena a objeto JSON para obtener sus datos
            JSONObject postObject = ApiUtil.getJsonObjectFromStringData(data);
            JSONObject titleJson = ApiUtil.getJsonObjectFromStringData(postObject.getString("headlines"));
            JSONObject summaryJson = ApiUtil.getJsonObjectFromStringData(postObject.getString("subheadlines"));
            JSONObject promoItems = ApiUtil.getJsonObjectFromStringData(postObject.getString("promo_items"));
            JSONObject photoJson = ApiUtil.getJsonObjectFromStringData(promoItems.getString("basic"));

            ApiResponseDto dataDto = new ApiResponseDto();
            dataDto.setDate(ApiUtil.getDataFromJsonObject(postObject, "publish_date"));
            dataDto.setLink(ApiUtil.getDataFromJsonObject(postObject, "_website_urls"));
            dataDto.setTitle(ApiUtil.getDataFromJsonObject(titleJson, "basic"));
            dataDto.setSummary(ApiUtil.getDataFromJsonObject(summaryJson, "basic"));
            dataDto.setPhotoLink(ApiUtil.getDataFromJsonObject(photoJson, "url"));
            dataDto.setPhotoContent(getPostImage(dataDto.getPhotoLink(), returnPhoto));
            dataDto.setContentTypePhoto(getPostImageContentType(dataDto.getPhotoLink(), returnPhoto));
            return dataDto;

        } catch (Exception ex) {
            log.debug("Error in getResponseData: ", ex);
            log.error("No se pudo obtener datos del objecto → {}", data);
        }
        return null;
    }

    // Retorna cadena de texto que contiene la imagen codificada en Base64
    private String getPostImage(String urlString, boolean returnPhoto) {
        try {
            /*
             * La imagen codificada se retorna solo bajo petición,
             * si returnPhoto es igual a TRUE
             */
            if ( !returnPhoto ) return "";
            
            if (urlString == null || urlString.isEmpty()) {
                throw new Exception("URL de imagen nulo o vacio");
            }

            byte[] imagaBytes = ApiUtil.getImageFromNetByUrl(urlString);

            return Base64.getEncoder().encodeToString(imagaBytes);

        } catch (Exception ex) {
            log.error("Error in getPostImage: ", ex);
        }
        return "";
    }

    // Retorna el tipo de contenido de la imagen que se recibe por parámetro
    private String getPostImageContentType(String urlString, boolean returnContentType) {
        try {
            /*
             * El tipo de contenido se retorna solo bajo petición,
             * si returnContentType es igual a TRUE
             */
            if ( !returnContentType ) return "";
            
            if (urlString == null || urlString.isEmpty()) {
                throw new Exception("URL de imagen nulo o vacio");
            }

            return ApiUtil.getContentTypeFromNetByUrl(urlString);

        } catch (Exception ex) {
            log.error("Error in getPostImage: ", ex);
        }
        return "";
    }

    /*
     * Realiza la petición a la WS de ABC para obtener la página, recibe por
     * parámetro el dato a buscar y retorna cadena de texto en formato de JSON ARRAY
     */
    private String getDataFromWeb(String search) throws Exception {
        BufferedReader br = null;
        try {
            
            String urlWS = search == null || search.isEmpty() ?
                            "https://www.abc.com.py/buscar/" :
                            "https://www.abc.com.py/buscar/" + search;

            URL url = new URL ( urlWS );
            HttpURLConnection httpConnection = (HttpURLConnection)url.openConnection();
            httpConnection.setConnectTimeout(300000); // 5 min por defecto, pueden ser menos
            httpConnection.setRequestProperty("Content-Type", "application/json; utf-8");
            httpConnection.setRequestProperty("Accept", "application/json");
            httpConnection.setRequestMethod("GET");
            httpConnection.setDoOutput(true);
  
            br = new BufferedReader(new InputStreamReader(
                httpConnection.getInputStream(), StandardCharsets.UTF_8));
  
            String responseLine;
            StringBuilder response = new StringBuilder();
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
  
            // Retorna la estructura de datos recuperado del HTML
            return getStringDataFromHTML( response.toString() );
  
        }catch (Exception ex){
            log.debug("Error in getNewPostsFromWeb: ", ex);
            throw new Exception("error in getNewPostsFromWeb - " + ex.getMessage());
        } finally {
            if (br != null) {
                br.close();
            }
        }
    }
 
    // Parsea el String data que se encuentra en la página HTML
    private String getStringDataFromHTML(String htmlString) throws Exception {

        if (htmlString == null) {
            throw new Exception("error in getStringDataFromHTML - htmlString is null");
        }

        try {

            /* // TODO:
             * Investigar una forma más optima de recuperar los datos de la página,
             * actualmente se parsea el HTML que se recibe como cadena de texto y 
             * con esto nos arriesgamos que con cualquier cambio en la página se rompa el proceso.
             */

            String parseValueStart = "Fusion.globalContent={\"data\":";
            String parseValueEnd= "}}}]";

            String arrayString = htmlString.substring(
                    htmlString.indexOf(parseValueStart) + parseValueStart.length(),
                    htmlString.indexOf(parseValueEnd) + parseValueEnd.length()
                );
            
            log.debug("Array string data: {}", arrayString);
            return arrayString;

        } catch (Exception ex) {
            log.debug("Error in getStringDataFromHTML: ", ex);
            throw new Exception("error in getStringDataFromHTML - " + ex.getMessage());
        }

    }
    
}
