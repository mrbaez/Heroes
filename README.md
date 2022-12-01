# Prueba técnica spring boot: W2m

## Consigna

Desarrollar, utilizando Spring Boot 2 y Java 11, una API que permita hacer un mantenimiento de súper
héroes.
Este mantenimiento debe permitir:
* Consultar todos los súper héroes.
* Consultar un único súper héroe por id.
* Consultar todos los súper héroes que contienen, en su nombre, el valor de un parámetro
  enviado en la petición. Por ejemplo, si enviamos “man” devolverá “Spiderman”, “Superman”,
  “Manolito el fuerte”, etc.
* Modificar un súper héroe.
* Eliminar un súper héroe.
* Test unitarios de algún servicio.


## Tecnologías utilizadas para implementar la solución.

* Lenguaje: java versión 11
* Framework: spring boot.
* Librerias: mockito para los tests, jacoco para la cobertura, springdoc-openapi-ui para documentar los servicios rests, lombok, flyway
* Docker


## Instalación  del proyecto
1. **Descargar código fuente**

```console
git clone https://github.com/baezjm/heroes.git
```

2. **Compilar**

```console
mvn clean install
```

3. **Correr el proyecto localmente**

```console
mvn spring-boot:run
```

4. **Generar imagen de docker**

```console
docker build -t w2m/heroes .
```

5. **Levantar imagen de docker**

```console
docker run -p 8080:8080 w2m/heroes
```

6. **Urls**

   [Documentación de la api: Swagger](http://localhost:8080/swagger-ui.html)

![](/swagger.png)

## Tests

**Ejecución de tests: el mismo corre jacoco para medir cobertura**
```console
 mvn clean test
```

![](/jacoco.png)

El resultado queda en:

```console
 ../target/site/jacoco/index.html
```
