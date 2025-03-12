# **_Proyecto Fivet_**

### **_PROJECT DESCRIPTION:_**

**_This is the Fivet project for the subject of the Development and Integration of Solutions course in 2022, with all the dependencies installed through a ```.gradle``` extension file, another ```.proto``` file to generate calls to the methods of some classes of said particular project._**

### **_Use cases for creating a veterinary record (FiVet):_**

1.- **_Create Veterinary Record:_** **_It begins when the veterinarian wants to create a record for a patient who does not have one. The veterinarian will have to record the patient's data and optionally the owner's data. For patient data, the veterinarian will have to enter the name, species, date of birth or age, breed, sex, color, and can take a photo at the time, or associate an image file from the device or through a link Regarding the owner, it is interesting to register the name, address, landline, mobile phone, email and RUT. You will also have to indicate whether the type of record is internal or external (in the case of a consultation for a patient who is not a client of the veterinary clinic)._**

2.- **_Search Veterinary Record:_** **_The veterinarian wants to see a patient's record. To do this, the search is carried out by RUT of the owner, name of the patient or record number. If more than one (patient name) is found, a summary of all those found must be shown, the veterinarian can select one from that list. If only one is found, the information on the file is displayed._**

3.- **_Add Control to a Patient:_** **_The veterinarian wants to add a control to a patient who may or may not have a record. To do this, if it has one, the Search Veterinary Record use case is carried out. If not available, the Create Veterinary Record use case is carried out. The Veterinarian will enter the data of a control, that is: date, temperature, weight, height, diagnosis, name of the veterinarian who performed it, the date of the next control if applicable and if it is necessary to associate an exam with your name. and date it was carried out._**

4.- **_Create a Patient Card:_** **_The veterinarian wants to create a card for an internal patient or a new patient. To do this, in the event that it is an inpatient, the use case Search Veterinary Record is carried out. If it is new, the Create Veterinary File use case is carried out. The veterinarian extracts the data required to enter them into the card, with respect to the owner, these are: owner (owner's name), address, telephone number, and with respect to the patient: record number, patient name, species, sex, age and photo._**

**_The project is divided by some of its attributes to regard:_**

 - **_Patient monitoring (Control)._**
 - **_Patient examination (Examen)._**
 - **_Patient medical record (Ficha Medica)._**
 - **_Patient photo (It is not taken into account when testing it but it is considered in the project (Foto))._**
 - **_Person (Persona)._**

## **_What attributes would you take into account for each class in the project?_**

 - **_For the Control class (Control):_**
   
   - **_Date (Fecha)._**
   - **_Temperature (Temperatura)._**
   - **_Weight (Peso)._**
   - **_Height (Altura)._**
   - **_Diagnosis (Diagnostico)._**
   - **_Vet (Veterinario)._**
   - **_Medical record (Ficha Medica)._**
  
 - **_For the exam (Examen) class:_**

   - **_Name (Name of the medical exam (Nombre))._**
   - **_Date (Fecha (Date the exam was taken))._**
  
 - **_For the person class (Persona):_**

   - **_ID (RUT)._**
   - **_Name (Nombre)._**
   - **_Address (Direccion)._**
   - **_Landline number (Telefono fijo)._**
   - **_Mobile number (Telefono movil)._**
   - **_E-mail (Correo)._**
   - **_Password (Contraseña)._**
  
 - **_For photo class (Foto):_**

   - **_URL._**
  
 - **_For the medical record class (Ficha Medica):_**

   - **_Patient record number (Numero de ficha del paciente)._**
   - **_Patient name (Nombre del paciente)._**
   - **_Patient species (Especie del paciente)._**
   - **_Birthdate (Fecha de nacimiento)._**
   - **_Patient breed (Raza del paciente)._**
   - **_Patient gender (Sexo del paciente)._**
   - **_Patient type (Tipo del paciente al que pertenece)._**
   - **_Patient owner (Dueño del paciente)._**
   - **_Patient number controls (Cantidad de controles del paciente)._**

## **_Regarding the service of a veterinary record..._**

- **_The service of a veterinary record is made up of 2 modalities:_**
  
  - **_Client (FivetClient)._**
  - **_Server (FivetServer)._**
 
 - **_Also, in this project one of these methods is considered to be able to effectively perform unit testing:_**

   - **_Database (Database connections)._**
   - **_gRPC (GOOGLE Remote Procedure Call)._**
   - **_Problem General Model._**
  
 - **_The ```.proto``` file is distributed as follows in code:_**

### **_Message 1: Persona Entity_**

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

### **_Message 2: Medical Record Entity_**

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

### **_Message 3: Control Entity_**

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

### **_Depending on the person's gender_**

```
enum SexoEntity {
  UNDEFINED = 0;
  MACHO = 1;
  HEMBRA = 2;
}
```

### **_Message 4: Ficha Medica Reply Entity_**

```
message FichaMedicaReply {
  FichaMedicaEntity fichaMedica = 1;
}
```

### **_Message 5: To add a medical record from your entity (Ficha Medica)_**

```
message AddFichaReq {
  FichaMedicaEntity fichaMedica = 1;
}
```

### **_Message 6: To retrieve a medical record_**

```
message RetrieveFichaMedicaReq {
  int32 numeroFicha = 1;
}
```

### Message 7: To search for a medical record that is requested
```
message SearchFichaMedicaReq {
  string query = 1;
}
```
### Message 8: Add a requested person
```
message AddPersonaReq {
  PersonaEntity persona = 1;
}
```
### Message 9: Add a medical check from your entity
```
message AddControlReq {
  ControlEntity control = 1;
}
```
### Message 10: Request medical checks
```
message ControlRequest {
  ControlEntity control = 1;
}
```
### Message 11: To authenticate a user using their credentials
```
message AuthenticateReq {
  string login = 1;
  string password = 2;
}
```
### Message 12: To bring a person back from the system
```
message PersonaReply {
  PersonaEntity persona = 1;
}
```
### Through a veterinary record service
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
**_Traducido del español:_**

### Descripción del trabajo:

**_Este es el proyecto Fivet para la asignatura del curso Desarrollo e Integración de Soluciones año 2022, con todas las dependencias instaladas mediante un archivo de extensión .gradle, otro archivo .proto para generar llamados a los métodos de algunas clases de dicho proyecto en particular._**

### Casos de uso para la realización de una ficha veterinaria (FiVet)

1.- **Crear Ficha Veterinaria:** **_Comienza cuando el veterinario desea crear una ficha para un paciente que no tiene una. El veterinario tendrá que registrar los datos del paciente y opcionalmente los del dueño. Para los datos del paciente, el veterinario tendrá que ingresar el nombre, especie, fecha de nacimiento o edad, raza, sexo, color, y podrá tomar una foto en el momento, o asociar un archivo de imagen desde el dispositivo o por medio de un link. Con respecto al dueño, interesa registrar el nombre, dirección, teléfono fijo, teléfono móvil, email y RUT. Además tendrá que indicar si el tipo de ficha es interno o externo (en caso de una consulta para un paciente que no es cliente de la veterinaria)_.**

2.- **Buscar Ficha Veterinaria:** **_El veterinario desea ver la ficha de un paciente. Para ello la búsqueda se realiza por RUT del dueño, nombre del paciente o número de ficha. En caso de encontrar más de una (nombre de paciente) se debe mostrar un resumen de todas las encontradas, el veterinario podrá seleccionar una de esa lista. En caso de encontrar sólo una, se despliega la información de la ficha._**

3.- **Agregar Control a un Paciente:** **_El veterinario desea agregar un control a un paciente que puede tener ficha o no. Para ello, en el caso de que tenga, se realiza el caso de uso Buscar Ficha Veterinaria. En caso de no tener se
realiza el caso de uso Crear Ficha Veterinaria. El Veterinario ingresará los datos de un control, esto es: fecha, temperatura, peso, altura, diagnóstico, nombre del veterinario que la realizó, la fecha del próximo control si es que se aplica y si es necesario asociar a un examen con su nombre y fecha en que se realizó._**

4.- **Crear Carnet de un Paciente:** **_El veterinario desea crear un carnet a un paciente interno o un paciente nuevo. Para ello, en el caso de que sea un paciente interno, se realiza el caso de uso Buscar Ficha Veterinaria. En caso de ser nuevo, se realiza el caso de uso Crear Ficha Veterinaria. El veterinario extrae los datos que se requieren para ingresarlos al carnet, con respecto al dueño, estos son: propietario (nombre del dueño), domicilio, teléfono y a los que respecta al paciente: número de ficha, nombre del paciente, especie, sexo, edad y foto._**

**_El proyecto se divide por algunos de sus atributos a considerar:_**

  - **_Control del paciente._**
  - **_Examen del paciente._**
  - **_Ficha médica del paciente._**
  - **_Foto del paciente (No lo toma en cuenta al testearlo pero se considera en el proyecto)._**
  - **_Persona._**

 ## Qué atributos tomaría en cuenta para cada clase del proyecto?

  - **_Para la clase Control:_**
   
    - **_Fecha._**
    - **_Temperatura._**
    - **_Peso._**
    - **_Altura._**
    - **_Diagnóstico._**
    - **_Veterinario._**
    - **_Ficha Médica._**
   
  - **_Para la clase Examen:_**

    - **_Nombre (Nombre del examen médico)._**
    - **_Fecha (Fecha en la que se realizó el examen)._**

  - **_Para la clase Persona:_**

    - **_RUT._**
    - **_Nombre._**
    - **_Dirección._**
    - **_Teléfono Fijo._**
    - **_Teléfono Móvil._**
    - **_Correo._**
    - **_Contraseña._**

  - **_Para la clase Foto:_**

    - **_URL._**

  - **_Para la clase Ficha Médica:_**

    - **_Número de ficha del paciente._**
    - **_Nombre del paciente._**
    - **_Especie del paciente._**
    - **_Fecha de nacimiento._**
    - **_Raza del paciente._**
    - **_Sexo del paciente._**
    - **_Tipo del paciente._**
    - **_Dueño del paciente._**
    - **_Cantidad de controles del paciente._**


## Respecto al servicio de una ficha veterinaria...

- **_El servicio de una ficha veterinaria se componen de 2 modalidades:_**

  - **_Cliente (FivetClient)._**
  - **_Servidor (FivetServer)._**

- **_También, en este proyecto se considera uno de estos métodos para poder realizar efectivamente las pruebas unitarias:_**

  - **_Database (Conexiones a la base de datos)._**
  - **_gRPC (Protocolo de comunicación entre servicios)._**
  - **_Modelo general del problema._**
      
- **_El archivo .proto se distribuye de la siguiente manera en código:_**

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

**_Acerca de cómo se construye un programa siguiendo este modelo que está aquí abajo:_**

![image](https://github.com/MARSFOREVER472/fivet/assets/69094327/f13efed6-93a8-4410-a76f-88f947b3031b)


**Espero que esto les sirva de apoyo!!!**

**_Muchas gracias!!!_**

## Integrante:
Marcelo Andrés Lam Biaggini
