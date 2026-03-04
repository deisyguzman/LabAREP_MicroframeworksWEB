# Web Framework for REST Services and Static File Management

## Descripción del Proyecto

Este proyecto implementa un framework web personalizado en Java que permite el desarrollo de aplicaciones web con servicios REST backend y gestión de archivos estáticos. El framework está construido desde cero utilizando sockets TCP y proporciona una API sencilla e intuitiva para definir servicios REST usando funciones lambda.

### Características Principales

- **Servicios REST con Funciones Lambda**: Define endpoints REST de manera simple usando expresiones lambda
- **Extracción de Parámetros Query**: Accede fácilmente a parámetros de consulta en las solicitudes HTTP
- **Gestión de Archivos Estáticos**: Sirve archivos HTML, CSS, JavaScript e imágenes desde un directorio configurable
- **Arquitectura Limpia**: Separación de responsabilidades con clases dedicadas para Request, Response y Server

## Arquitectura del Sistema

### Componentes Principales

#### 1. HttpServer
La clase principal del framework que:
- Escucha conexiones HTTP en el puerto 8080
- Gestiona el routing de solicitudes a endpoints REST o archivos estáticos
- Mantiene un registro de endpoints definidos por el usuario
- Sirve archivos estáticos desde el classpath

#### 2. HttpRequest
Representa una solicitud HTTP entrante con:
- Parsing automático de query parameters
- Método `getValues(String name)` para extraer valores de parámetros
- Acceso a la ruta y método HTTP

#### 3. HttpResponse
Encapsula la respuesta HTTP con:
- Código de estado HTTP
- Tipo de contenido
- Cuerpo de la respuesta

#### 4. WebMethod
Interfaz funcional que define el contrato para los endpoints REST:
```java
@FunctionalInterface
public interface WebMethod {
    String execute(HttpRequest req, HttpResponse resp);
}
```

### Diagrama de Arquitectura

```
Cliente HTTP (Navegador)
        ↓
   HttpServer (Puerto 8080)
        ↓
    [Routing]
    /         \
REST Endpoint  Archivo Estático
    ↓              ↓
WebMethod      File System
    ↓              ↓
HttpResponse   HttpResponse
```

### Flujo de Procesamiento de Solicitudes

1. **Recepción**: El servidor acepta una conexión TCP en el puerto 8080
2. **Parsing**: La solicitud HTTP se analiza para extraer método, ruta y query parameters
3. **Routing**: Se determina si la solicitud es para un endpoint REST o un archivo estático
4. **Procesamiento**:
   - **REST**: Se ejecuta la función lambda correspondiente con HttpRequest y HttpResponse
   - **Estático**: Se lee el archivo del classpath y se sirve con el content-type apropiado
5. **Respuesta**: Se construye y envía la respuesta HTTP al cliente

## Instalación y Configuración

### Requisitos Previos

- Java 17 o superior
- Maven 3.6 o superior
- Git

### Clonar el Repositorio

```bash
git clone https://github.com/tu-usuario/tu-repositorio.git
cd tu-repositorio
```

### Compilar el Proyecto

```bash
mvn clean compile
```

### Ejecutar la Aplicación

```bash
mvn exec:java -Dexec.mainClass="com.arep.lab.MathService"
```

O alternativamente:

```bash
mvn clean package
java -cp target/classes com.arep.lab.MathService
```

## Uso del Framework

### Ejemplo Básico

```java
import static com.arep.lab.HttpServer.get;
import static com.arep.lab.HttpServer.staticfiles;

public class MyApp {
    public static void main(String[] args) throws IOException {
        // Definir ubicación de archivos estáticos
        staticfiles("/webroot/public");
        
        // Definir endpoints REST
        get("/App/hello", (req, resp) -> "Hello " + req.getValues("name"));
        
        get("/App/pi", (req, resp) -> {
            return String.valueOf(Math.PI);
        });
        
        // Iniciar servidor
        HttpServer.startServer();
    }
}
```

### Definir Servicios REST

#### Endpoint Simple
```java
get("/App/hello", (req, resp) -> "Hello World!");
```

#### Endpoint con Parámetros Query
```java
get("/App/greet", (req, resp) -> {
    String name = req.getValues("name");
    return "Hello, " + name + "!";
});
```

#### Endpoint con Lógica Compleja
```java
get("/App/calculate", (req, resp) -> {
    String operation = req.getValues("op");
    String numStr = req.getValues("num");
    
    try {
        double num = Double.parseDouble(numStr);
        double result;
        
        switch(operation) {
            case "square":
                result = num * num;
                break;
            case "sqrt":
                result = Math.sqrt(num);
                break;
            default:
                return "Unknown operation";
        }
        
        return "Result: " + result;
    } catch (NumberFormatException e) {
        return "Invalid number format";
    }
});
```

### Configurar Archivos Estáticos

```java
// Los archivos se buscarán en src/main/resources/webroot/public/
staticfiles("/webroot");
```

Estructura de archivos:
```
src/main/resources/
    webroot/
        public/
            index.html
            styles.css
            script.js
            images/
                logo.png
```

## Endpoints de Ejemplo

La aplicación de ejemplo (MathService) proporciona los siguientes endpoints:

### 1. Saludo Personalizado
```
GET http://localhost:8080/App/hello?name=Pedro
Respuesta: Hello Pedro
```

### 2. Valor de PI
```
GET http://localhost:8080/App/pi
Respuesta: 3.141592653589793
```

### 3. Número de Euler
```
GET http://localhost:8080/App/euler
Respuesta: Euler's number: 2.718281828459045
```

### 4. Calcular Cuadrado
```
GET http://localhost:8080/App/square?num=5
Respuesta: The square of 5.0 is 25.0
```

### 5. Calcular Seno
```
GET http://localhost:8080/App/sin?angle=1.5708
Respuesta: sin(1.5708) = 0.9999999999952793
```

### 6. Calcular Coseno
```
GET http://localhost:8080/App/cos?angle=0
Respuesta: cos(0.0) = 1.0
```

### 7. Archivos Estáticos
```
GET http://localhost:8080/index.html
Respuesta: Página HTML con interfaz de usuario
```

## Pruebas Realizadas

### Pruebas Manuales

#### 1. Prueba de Endpoint Hello
```bash
curl "http://localhost:8080/App/hello?name=Pedro"
```
**Resultado Esperado**: `Hello Pedro`

#### 2. Prueba de Endpoint PI
```bash
curl "http://localhost:8080/App/pi"
```
**Resultado Esperado**: `3.141592653589793`

#### 3. Prueba de Cálculo de Cuadrado
```bash
curl "http://localhost:8080/App/square?num=7"
```
**Resultado Esperado**: `The square of 7.0 is 49.0`

#### 4. Prueba de Archivo Estático
Abrir en navegador: `http://localhost:8080/index.html`

**Resultado Esperado**: Página HTML con interfaz de usuario interactiva

### Pruebas desde el Navegador

1. Iniciar la aplicación: `mvn exec:java -Dexec.mainClass="com.arep.lab.MathService"`
2. Abrir navegador en `http://localhost:8080/index.html`
3. Interactuar con los enlaces de prueba en la interfaz
4. Usar la calculadora interactiva de cuadrados

### Capturas de Pantalla de Pruebas

Las pruebas demuestran:
- ✅ Funcionamiento correcto de endpoints REST con parámetros query
- ✅ Servicio correcto de archivos estáticos (HTML, CSS, JavaScript)
- ✅ Manejo apropiado de errores (parámetros faltantes, formatos inválidos)
- ✅ Respuestas HTTP con códigos de estado correctos

## Estructura del Proyecto

```
lab/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── arep/
│   │   │           └── lab/
│   │   │               ├── HttpServer.java          # Servidor principal
│   │   │               ├── HttpRequest.java         # Representación de solicitud
│   │   │               ├── HttpResponse.java        # Representación de respuesta
│   │   │               ├── WebMethod.java           # Interfaz funcional
│   │   │               ├── MathService.java         # Aplicación de ejemplo
│   │   │               ├── EchoClient.java          # Cliente de ejemplo
│   │   │               ├── EchoServer.java          # Servidor echo de ejemplo
│   │   │               ├── URLReader.java           # Utilidad de lectura URL
│   │   │               └── URLParser.java           # Utilidad de parsing URL
│   │   └── resources/
│   │       └── webroot/
│   │           └── public/
│   │               └── index.html                   # Página principal
│   └── test/
│       └── java/
│           └── com/
│               └── arep/
│                   └── lab/
│                       └── LabApplicationTests.java # Tests
├── pom.xml                                          # Configuración Maven
├── .gitignore                                       # Git ignore
└── README.md                                        # Este archivo
```

## Conceptos Técnicos Implementados

### 1. Protocolo HTTP
- Parsing de solicitudes HTTP (método, URI, headers)
- Construcción de respuestas HTTP con códigos de estado
- Manejo de diferentes tipos de contenido (HTML, CSS, JS, imágenes)

### 2. Arquitectura Cliente-Servidor
- Servidor basado en ServerSocket TCP
- Aceptación y manejo de múltiples conexiones secuenciales
- Comunicación bidireccional a través de streams

### 3. Programación Funcional
- Uso de interfaces funcionales (@FunctionalInterface)
- Expresiones lambda para definir comportamiento de endpoints
- Programación declarativa vs imperativa

### 4. Manejo de Recursos
- Carga de archivos desde classpath
- Gestión apropiada de streams (try-with-resources)
- Cierre correcto de sockets y conexiones

### 5. Arquitectura de Aplicaciones Distribuidas
- Separación de responsabilidades (Request, Response, Server)
- API pública clara y fácil de usar (get(), staticfiles())
- Extensibilidad para agregar nuevos endpoints

## Limitaciones Conocidas

1. **Concurrencia**: El servidor procesa solicitudes secuencialmente. Para producción, se necesitaría un pool de threads.
2. **Tipos de Contenido**: Soporta tipos comunes pero no todos los tipos MIME.
3. **Seguridad**: No implementa HTTPS ni autenticación.
4. **Cache**: No implementa mecanismos de cache HTTP.
5. **POST/PUT/DELETE**: Solo soporta método GET.

## Mejoras Futuras

- [ ] Soporte para métodos POST, PUT, DELETE
- [ ] Pool de threads para manejo concurrente de solicitudes
- [ ] Soporte para HTTPS
- [ ] Sistema de middleware/filtros
- [ ] Soporte para sesiones
- [ ] Compresión de respuestas (gzip)
- [ ] Logging estructurado
- [ ] Métricas y monitoreo

## Autor

**Nombre del Estudiante**  
Escuela Colombiana de Ingeniería Julio Garavito  
Arquitectura Empresarial (AREP)  
2026

## Licencia

Este proyecto es de código abierto y está disponible bajo la Licencia MIT.

## Referencias

- [Java Networking Tutorial](https://docs.oracle.com/javase/tutorial/networking/index.html)
- [HTTP/1.1 Specification (RFC 2616)](https://www.w3.org/Protocols/rfc2616/rfc2616.html)
- Benavides Navarro, L. D. (2020). "Introducción a esquemas de nombres, redes, clientes y servicios con Java". Escuela Colombiana de Ingeniería.
