# Spkr

<p align="center">
  <img width="256" height="256" alt="logoReadme" src="https://github.com/user-attachments/assets/4ec9da36-70c2-49af-8d6e-c90378bdd378" />
</p>

<h3 align="center">Spkr - Prácticas de PDS</h3>

<p align="center">
  Aplicación de aprendizaje de idiomas mediante ejercicios interactivos
</p>

---

## Acerca del Proyecto

**Spkr** es una aplicación de escritorio desarrollada en Java para el aprendizaje de idiomas. Inspirada en aplicaciones como Duolingo, permite a los usuarios practicar vocabulario y gramática mediante diferentes tipos de ejercicios interactivos.

### Características Principales

- **Tres tipos de ejercicios:** Test de opciones múltiples, traducción escrita y rellenar huecos
- **Tres estrategias de aprendizaje:** Secuencial, aleatoria y repetición espaciada
- **Sistema de progreso:** Guarda el avance para continuar donde lo dejaste
- **Temporizador:** Control del tiempo dedicado a cada sesión de estudio
- **Estadísticas detalladas:** Seguimiento de aciertos, errores, tiempo de uso y rachas
- **Repaso de errores:** Sistema inteligente para reforzar las preguntas falladas
- **Importación/Exportación:** Comparte cursos en formato JSON o YAML

### Funcionalidad Extra Implementada

- **Repaso de errores frecuentes:** Sistema inteligente que registra automáticamente todas las preguntas falladas durante la realización de cualquier curso. Los errores se acumulan en una lista global que recopila fallos de todos los cursos del usuario. Al acceder a la función "Repasar Errores", el usuario puede practicar específicamente aquellas preguntas que más le cuestan. Cada vez que se acierta una pregunta durante el repaso, su contador de errores se reduce. Cuando el usuario demuestra dominio de una pregunta (contador llega a cero), esta se elimina automáticamente de la lista de errores frecuentes. Este sistema permite un aprendizaje más eficiente al focalizar el estudio en las áreas de mayor dificultad.
---

 ## Arquitectura

  El proyecto implementa el patrón arquitectónico **MVC (Modelo-Vista-Controlador)** con estricta separación de
  responsabilidades:

  | Capa | Descripción |
  |------|-------------|
  | **Modelo** | Entidades de dominio persistidas con JPA (Usuario, Curso, Lección, Pregunta, Progreso, Estadísticas) |
  | **Vista** | Interfaces gráficas Swing que delegan toda la lógica al controlador |
  | **Controlador** | Clase centralizada `SpkrApp` que encapsula el acceso al modelo y gestiona la lógica de negocio |

  ### Patrones de Diseño Aplicados

  | Patrón | Aplicación |
  |--------|------------|
  | **MVC** | Arquitectura general de la aplicación |
  | **Strategy** | Estrategias de aprendizaje intercambiables (Secuencial, Aleatoria, Repetición Espaciada) |
  | **Singleton** | Gestor de persistencia (`GestorPersistencia`) |
  | **DTO** | Comunicación de resultados entre capas (`ResultadoRespuesta`) |
  | **Repository** | Catálogos para acceso a datos (`CatalogoUsuarios`, `CatalogoCursos`) |


## Equipo de Desarrollo

<table>
  <tr>
    <td align="center">
      <a href="https://github.com/OkeV2">
        <img src="https://github.com/OkeV2.png" width="100" height="100" alt="OkeV2"/><br />
        <b>Ismel Alejandro Oquendo Rodríguez</b><br />
        @OkeV2
      </a>
    </td>
  </tr>
</table>

### Profesor Responsable

| Nombre | GitHub | Web |
|--------|--------|-----|
| Jesús Sánchez Cuadrado | [@jesusc](https://github.com/jesusc) | [Página personal](https://jesusc.github.io/) |

---

## Documentación

### Casos de Uso

Los casos de uso del sistema se encuentran documentados en los issues del repositorio:

- [Issue principal de Casos de Uso](https://github.com/OkeV2/spkr-pds-2025/issues/1)


### Modelo de Dominio

El modelo de dominio y diagramas UML se encuentran en:

- [Diagramas de Diseño](diseño/modelo/)

### Manual de Usuario

- [Manual de Usuario](documentacion/manual/manualUsuario.md)

---

## Inicio Rápido

### Requisitos Previos

- **Java:** JDK 17 o superior
- **Maven:** 3.6 o superior

### Instalación

1. **Clonar el repositorio:**
```bash
   git clone https://github.com/OkeV2/spkr-pds-2025.git
   cd spkr-pds-2025
```

2. **Compilar el proyecto:**
```bash
   cd spkr
   mvn clean install
```

3. **Ejecutar la aplicación:**
```bash
   mvn exec:java "-Dexec.mainClass=es.um.pds.spkr.Main"
```

### Usuario de Prueba

La base de datos viene inicializada con un usuario de prueba:

| Campo | Valor |
|-------|-------|
| Usuario | demo |
| Email | demo@spkr.com |
| Contraseña | demo123 |

---

## Estructura del Proyecto
```
  spkr-pds-2025/
  ├── diseño/
  │   └── modelo/                 # Diagramas UML y modelo de dominio
  ├── documentacion/
  │   ├── imagenes/               # Capturas de pantalla
  │   └── manual/                 # Manual de usuario
  ├── spkr/                       # Proyecto Maven
  │   ├── src/
  │   │   ├── main/java/es/um/pds/spkr/
  │   │   │   ├── catalogo/       # Repositorios de datos
  │   │   │   ├── cargador/       # Importación/Exportación de cursos
  │   │   │   ├── estrategia/     # Estrategias de aprendizaje
  │   │   │   ├── gui/            # Vistas (interfaces gráficas)
  │   │   │   ├── modelo/         # Entidades del dominio
  │   │   │   ├── persistencia/   # Gestión de base de datos
  │   │   │   ├── util/           # Utilidades y estilos
  │   │   │   ├── Main.java       # Punto de entrada
  │   │   │   └── SpkrApp.java    # Controlador principal
  │   │   └── test/               # Tests unitarios
  │   └── pom.xml
  └── README.md

```

---

## Tecnologías Utilizadas

- **Java 17** - Lenguaje de programación
- **Maven** - Gestión de dependencias y construcción
- **JPA/Hibernate** - Persistencia de datos
- **H2 Database** - Base de datos embebida
- **Swing** - Interfaz gráfica de usuario
- **Jackson** - Procesamiento de JSON
- **SnakeYAML** - Procesamiento de YAML
- **JUnit 5** - Testing unitario

---

## Testing

Para ejecutar los tests unitarios:
```bash
cd spkr
mvn test
```

---

## Asignatura

Proceso de Desarrollo de Software - Curso 2024/25  
Grado en Ingeniería Informática
Universidad de Murcia

---

## Licencia

Este proyecto ha sido desarrollado con fines educativos como parte de la asignatura PDS.
