# Proyecto Fivet

### Descripción del trabajo:

**_Traducido del español:_**

**_Este es el proyecto Fivet para la asignatura del curso Desarrollo e Integración de Soluciones año 2022, con todas las dependencias instaladas mediante gradle, el archivo .proto para generar llamados a los métodos de algunas clases de dicho proyecto en particular._**

### Casos de uso para la realización de una ficha veterinaria (FiVet)

1.- **Crear Ficha Veterinaria:** **_Comienza cuando el veterinario desea crear una ficha para un paciente que no tiene una. El veterinario tendrá que registrar los datos del paciente y opcionalmente los del dueño. Para los datos del paciente, el veterinario tendrá que ingresar el nombre, especie, fecha de nacimiento o edad, raza, sexo, color, y podrá tomar una foto en el momento, o asociar un archivo de imagen desde el dispositivo o por medio de un link. Con respecto al dueño, interesa registrar el nombre, dirección, teléfono fijo, teléfono móvil, email y RUT. Además tendrá que indicar si el tipo de ficha es interno o externo (en caso de una consulta para un paciente que no es cliente de la veterinaria)_.**

2.- **Buscar Ficha Veterinaria:** **_El veterinario desea ver la ficha de un paciente. Para ello la búsqueda se realiza por RUT del dueño, nombre del paciente o número de ficha. En caso de encontrar más de una (nombre de paciente) se debe mostrar un resumen de todas las encontradas, el veterinario podrá seleccionar una de esa lista. En caso de encontrar sólo una, se despliega la información de la ficha._**

3.- **Agregar Control a un Paciente:** **_El veterinario desea agregar un control a un paciente que puede tener ficha o no. Para ello, en el caso de que tenga, se realiza el caso de uso Buscar Ficha Veterinaria. En caso de no tener se
realiza el caso de uso Crear Ficha Veterinaria. El Veterinario ingresará los datos de un control, esto es: fecha, temperatura, peso, altura, diagnóstico, nombre del veterinario que la realizó, la fecha del próximo control si es que se aplica y si es necesario asociar a un examen con su nombre y fecha en que se realizó._**

4.- **Crear Carnet de un Paciente:** **_El veterinario desea crear un carnet a un paciente interno o un paciente nuevo. Para ello, en el caso de que sea un paciente interno, se realiza el caso de uso Buscar Ficha Veterinaria. En caso de ser nuevo, se realiza el caso de uso Crear Ficha Veterinaria. El veterinario extrae los datos que se requieren para ingresarlos al carnet, con respecto al dueño, estos son: propietario (nombre del dueño), domicilio, teléfono y a los que respecta al paciente: número de ficha, nombre del paciente, especie, sexo, edad y foto._**

- **_El proyecto se divide por algunos de sus atributos a considerar:_**

    - **_Control._**
    - **_Examen._**
    - **_Ficha médica._**
    - **_Foto (No lo toma en cuenta al testearlo pero se considera en el proyecto)._**
    - **_Persona._**

 ## Qué atributos tomaría en cuenta para cada clase del proyecto?

  - **_Para la clase Control:_**
   
    - Fecha.
    - Temperatura.
    - Peso.
    - Altura.
    - Diagnóstico.
    - Veterinario.
    - Ficha Médica.
   
  - _Para la clase Examen:_

    - Nombre. (Nombre del examen médico).
    - Fecha. (Fecha en la que se realizó el examen).

  - _Para la clase Persona:_

    - RUT.
    - Nombre.
    - Dirección.
    - Teléfono Fijo.
    - Teléfono Móvil.
    - Correo.
    - Contraseña.

  - _Para la clase Foto:_

    - URL.

  - _Para la clase Ficha Médica:_

    - Número de ficha del paciente.
    - Nombre del paciente.
    - Especie del paciente.
    - Fecha de nacimiento.
    - Raza del paciente.
    - Sexo del paciente.
    - Tipo del paciente.
    - Dueño del paciente.
    - Cantidad de controles del paciente.


## Respecto al servicio de una ficha veterinaria...

- El servicio de una ficha veterinaria se componen de 2 modalidades:

  - Cliente (FivetClient).
  - Servidor (FivetServer).

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

## Arquitectura de un ORM

_Acerca de cómo se construye un programa siguiendo este modelo que está aquí abajo_:

![image](https://github.com/MARSFOREVER472/fivet/assets/69094327/f13efed6-93a8-4410-a76f-88f947b3031b)


**Espero que esto les sirva de apoyo!!!**

_Muchas gracias!!!_

## Integrante:
Marcelo Andrés Lam Biaggini
