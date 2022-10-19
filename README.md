# Test

Proyecto de prueba de API Rest con Java, Spring Boot y Maven

## Requisitos

Para construir y ejecutar la aplicación necesita:

- [java jdk 11](https://www.oracle.com/java/technologies/javase/jdk11-archive-downloads.html)
- [Maven 3](https://maven.apache.org)

## Ejecutando la aplicación localmente

Hay varias formas de ejecutar una aplicación Spring Boot en su máquina local. Una forma es ejecutar el método `main` en la clase `TestApplication.java` desde su IDE.

Alternativamente, puede usar el [complemento Spring Boot Maven](https://docs.spring.io/spring-boot/docs/current/reference/html/build-tool-plugins-maven-plugin.html) así:

```shell
mvn spring-boot:run
```


## Para invocar a la API desarrollada

Para probar la API mediante Swagger UI se debe levantar el proyecto e ingresar al siguiente URL: http://localhost:8080/swagger-ui.html

 - Método: GET
 - Request URL: http://localhost:8080/api/consulta?q=odesur&f=true
    - q: texto de busqueda, es parámetro requerido para invocar a la API.
    - f: parámetro que indica si debe retornar la imágen de la publicacón codificada en Base64 (es opcional)

## Ejemplo de respuesta

```
[
  {
    "date": "2022-10-16T17:11:57.853Z",
    "link": "[\"/nacionales/2022/10/16/visitantes-se-despiden-de-asuncion-destacando-hospitalidad-paraguaya/\"]",
    "photoLink": "https://cloudfront-us-east-1.images.arcpublishing.com/abccolor/VJX4SPHFONG27PJTFUMNWF4CYM.jpg",
    "title": "Visitantes se despiden de Asunción, destacando hospitalidad paraguaya",
    "summary": "La hospitalidad paraguaya es lo más valorado entre los turistas que se están despidieron hoy de Asunción, tras culminar los Juegos Odesur 2022. Los visitantes pasean por la Loma San Jerónimo, Costanera de Asunción, Panteón de los Héroes, calle Palma y otros sitios turísticos.",
    "photoContent": "",
    "contentTypePhoto": ""
  },
  {
    "date": "2022-10-16T13:25:23.903Z",
    "link": "[\"/nacionales/2022/10/16/video-de-tirika-emotiva-despedida-con-recuerdos-de-sus-genialidades/\"]",
    "photoLink": "https://cloudfront-us-east-1.images.arcpublishing.com/abccolor/LOBWUZH4CFCZNEHIPA4YTH6HZQ.jfif",
    "title": "Video: la emotiva despedida a Tiríka con recuerdos de sus genialidades ",
    "summary": "Un vídeo de Tiríka muestra su emotiva despedida en que repasa todos los recuerdos de sus genialidades que quedarán en la memoria de los paraguayos. La mascota oficial de los Juegos Odesur Asu 2022 se ganó el corazón de todos. Agradeció en su cuenta de Instagram y una “lluvia” de mensajes de cariño llegaron a través de la red social. ",
    "photoContent": "",
    "contentTypePhoto": ""
  }
]
```
