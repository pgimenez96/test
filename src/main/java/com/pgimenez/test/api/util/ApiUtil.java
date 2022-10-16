package com.pgimenez.test.api.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;

import com.pgimenez.test.api.exception.ApiException;
import com.pgimenez.test.api.exception.ApiExceptionHandler;

public class ApiUtil {

    private ApiUtil() {}

    // Convierte cadena de texto a JSON Object, el texto debe tener el formato permitido
    public static JSONObject getJsonObjectFromStringData(String jsonString) throws ApiException {
        try {
            return new JSONObject(jsonString);
        } catch (Exception e) {
            throw new ApiException(ApiExceptionHandler.G100, "Formato de json no es valido para la cadena → " + jsonString, e);
        }
    }

    // Convierte cadena de texto a JSON Array, el texto debe tener el formato permitido
    public static JSONArray getJsonArrayFromStringData(String arrayString) throws ApiException {
        try {
            return new JSONArray(arrayString);
        } catch (Exception e) {
            throw new ApiException(ApiExceptionHandler.G100, "Formato de json array no es valido para la cadena → " + arrayString, e);
        }
    }

    // Retorna valor del atributo que se recibe como parametro "key"
    public static String getDataFromJsonObject ( JSONObject object, String key ) {
        try {

            String dataStr = String.valueOf(object.get(key));
            if ( dataStr == null) dataStr = "";

            return dataStr;

        } catch (Exception ignored)  {/*Ignore exception*/}

        return "";
    }

    // Consulta y retorna archivo en array de bytes de la URL que recibe por parámetro
    public static byte[] getImageFromNetByUrl(String urlString) throws Exception {  
        try {  
            URL url = new URL(urlString);  
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();  
            conn.setRequestMethod("GET");  
            conn.setConnectTimeout(300000);

            InputStream inStream = conn.getInputStream();
            return readInputStream(inStream);
        } catch (Exception e) {  
            throw new Exception("No se pudo obtener imagen de la ruta " + urlString);
        }  
    }

    // Consulta y retorna el tipo de contenido de la URL que recibe por parámetro
    public static String getContentTypeFromNetByUrl(String urlString) throws Exception {
        try {
            URL url = new URL(urlString);  
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();  
            conn.setRequestMethod("GET");  
            conn.setConnectTimeout(300000);
            return conn.getContentType();
        } catch (Exception e) {
            throw new Exception("No se pudo obtener ContentType de la ruta " + urlString);
        }
    }

    // Lee flujo de entrada → inStream
    public static byte[] readInputStream(InputStream inStream) throws Exception {  
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();  
        byte[] buffer = new byte[10240];  
        int len = 0;  
        while ((len = inStream.read(buffer)) != -1) {  
            outStream.write(buffer, 0, len);  
        }  
        inStream.close();  
        return outStream.toByteArray();  
    }
    
}
