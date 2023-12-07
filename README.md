# Proyecto Fivet

## Descripción del trabajo:
Proyecto Fivet para la asignatura del Proyecto Desarrollo e Integración de Soluciones año 2022, con todas las dependencias instaladas mediante gradle, el archivo .proto para generar llamados a los métodos de algunas clases del proyecto en particular.

El archivo .proto se distribuye de la siguiente manera en código:

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

## Integrante:
Marcelo Andrés Lam Biaggini
