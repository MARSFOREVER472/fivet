# Proyecto Fivet

## Descripción del trabajo:
Proyecto Fivet para la asignatura del Proyecto Desarrollo e Integración de Soluciones año 2022, con todas las dependencias instaladas mediante gradle, el archivo .proto para generar llamados a los métodos de algunas clases del proyecto en particular.

- El proyecto se divide por algunos de sus atributos a considerar:

    - Control.
    - Examen.
    - Ficha médica.
    - Foto (No lo toma en cuenta al testearlo pero se considera en el proyecto).
    - Persona.

 ## Qué atributos tomaría en cuenta para cada clase del proyecto?

  - Para la clase Control:
   
    - Fecha.
    - Temperatura.
    - Peso.
    - Altura.
    - Diagnóstico.
    - Veterinario.
    - Ficha Médica.
   
  - Para la clase Examen:

    - Nombre.
    - Fecha.

  - Para la clase Persona:

  - Para la clase Foto:

    - URL.

  - Para la clase Ficha Médica:


## Respecto al servicio de una ficha veterinaria...

- El servicio de una ficha veterinaria se componen de 2 modalidades:

  - Cliente.
  - Servidor.

- También, en este proyecto se considera uno de estos métodos para poder realizar efectivamente las pruebas unitarias:

  - Database (Conexiones a la base de datos).
  - gRPC (Protocolo de comunicación entre servicios).
  - Modelo general del problema.
      
- El archivo .proto se distribuye de la siguiente manera en código:

### Mensaje 1: Entidad Persona

```
message PersonaEntity {
  string rut = 1;
  string nombre = 2;
  string email = 3;
  string direccion = 4;
  string telefonoFijo = 5;
  string telefonoMovil = 6;
  string password = 7;
}
```

### Mensaje 2: Entidad Ficha Medica

```
message FichaMedicaEntity {
  int32 numeroFicha = 1;
  string nombrePaciente = 2;
  string especie = 3;
  string fechaNacimiento = 4;
  string raza = 5;
  SexoEntity sexo = 6;
  string color = 7;
  string tipo = 8;
  PersonaEntity duenio = 9;
  repeated ControlEntity controles = 10;
}
```

### Mensaje 3: Entidad Control

```
message ControlEntity {
  string fecha = 1;
  float temperatura = 2;
  float peso = 3;
  float altura = 4;
  string diagnostico = 5;
  PersonaEntity veterinario = 6;
  FichaMedicaEntity fichaMedica = 7;
}
```

### Según género de la persona

```
enum SexoEntity {
  UNDEFINED = 0;
  MACHO = 1;
  HEMBRA = 2;
}
```

### Mensaje 4: Respuesta de la Entidad Ficha médica

```
message FichaMedicaReply {
  FichaMedicaEntity fichaMedica = 1;
}
```

### Mensaje 5: Para añadir una ficha médica desde su entidad (Ficha Médica).

```
message AddFichaReq {
  FichaMedicaEntity fichaMedica = 1;
}
```

### Mensaje 6: Para recuperar una ficha médica.

```
message RetrieveFichaMedicaReq {
  int32 numeroFicha = 1;
}
```

### Mensaje 7: Para buscar una ficha médica a la cual se le solicita.

```
message SearchFichaMedicaReq {
  string query = 1;
}
```

### Mensaje 8: Agregar una persona solicitada.

```
message AddPersonaReq {
  PersonaEntity persona = 1;
}
```

### Mensaje 9: Añadir un control médico desde su entidad (Control).

```
message AddControlReq {
  ControlEntity control = 1;
}
```

### Mensaje 10: Solicitar controles médicos.

```
message ControlRequest {
  ControlEntity control = 1;
}
```

### Mensaje 11: Para autenticar a un usuario mediante sus credenciales.

```
message AuthenticateReq {
  string login = 1;
  string password = 2;
}
```

### Mensaje 12: Para traer de regreso a una persona desde el sistema.

```
message PersonaReply {
  PersonaEntity persona = 1;
}
```

### Mediante un servicio de una ficha veterinaria.

```
service FivetService {

  rpc authenticate(AuthenticateReq) returns(PersonaReply) {}

  rpc addControl(AddControlReq) returns(FichaMedicaReply) {}

  rpc retrieveFicha(RetrieveFichaMedicaReq) returns(FichaMedicaReply) {}

  rpc searchFicha(SearchFichaMedicaReq) returns (stream FichaMedicaEntity) {}

  rpc addFicha(AddFichaReq) returns(FichaMedicaReply) {}

  rpc addPersona(AddPersonaReq) returns (PersonaReply) {}

}
```

Espero que esto les sirva de apoyo!!!
Muchas gracias!!!

## Integrante:
Marcelo Andrés Lam Biaggini
