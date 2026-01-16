# Manual de Usuario - Spkr

## 1. Introducción

**Spkr** es una aplicación de escritorio diseñada para el aprendizaje de idiomas mediante el uso de tarjetas de memoria (flashcards). La aplicación permite a los usuarios practicar vocabulario y gramática a través de diferentes tipos de ejercicios interactivos, ofreciendo múltiples estrategias de aprendizaje adaptadas a las necesidades de cada usuario.

### Características principales

- **Tres tipos de ejercicios:** Test de opciones múltiples, traducción escrita y rellenar huecos
- **Tres estrategias de aprendizaje:** Secuencial, aleatoria y repetición espaciada
- **Sistema de progreso:** Guarda automáticamente tu avance para continuar donde lo dejaste
- **Temporizador:** Controla el tiempo dedicado a cada sesión de estudio
- **Estadísticas detalladas:** Seguimiento de aciertos, errores, tiempo de uso y rachas de días consecutivos
- **Repaso de errores:** Sistema inteligente para reforzar las preguntas que más te cuestan
- **Importación y exportación:** Comparte cursos en formato JSON o YAML

### Objetivo del manual

Este manual tiene como objetivo guiar al usuario a través de todas las funcionalidades de la aplicación Spkr, explicando de forma detallada cada pantalla y las acciones disponibles en cada una de ellas.

> **⚠️ NOTA IMPORTANTE**
> 
> La base de datos viene inicializada con dos cursos y un usuario de prueba con las siguientes credenciales:
> 
> | Campo | Valor |
> |-------|-------|
> | **Usuario** | admin |
> | **Email** | admin@spkr.com |
> | **Contraseña** | 123 |
> 
> Puede utilizar estas credenciales para acceder directamente a la aplicación o crear su propia cuenta mediante el proceso de registro.

---

## 2. Funcionamiento

### 2.1 Pantalla de Bienvenida

Al iniciar la aplicación, se muestra la pantalla de bienvenida que permite al usuario elegir entre iniciar sesión con una cuenta existente o registrarse como nuevo usuario.

![Pantalla de Bienvenida](documentacion/imagenes/bienvenida.png)

**Elementos de la pantalla:**
- **Logo de la aplicación:** Identificación visual de Spkr
- **Botón "Iniciar Sesión":** Accede al formulario de inicio de sesión
- **Botón "Registrarse":** Accede al formulario de registro de nuevo usuario

---

### 2.2 Pantalla de Registro de Usuario

Esta pantalla permite crear una nueva cuenta de usuario en el sistema.

![Pantalla de Registro](imagenes/manual/registro.png)

**Campos del formulario:**
- **Nombre de usuario:** Identificador único para acceder a la aplicación
- **Correo electrónico:** Email asociado a la cuenta
- **Contraseña:** Clave de acceso (se oculta al escribir)
- **Confirmar contraseña:** Verificación de la contraseña introducida

**Acciones disponibles:**
- **Botón "Registrarse":** Crea la cuenta si todos los datos son válidos
- **Enlace "¿Ya tienes cuenta? Inicia sesión":** Vuelve a la pantalla de inicio de sesión

**Validaciones:**
- Todos los campos son obligatorios
- El nombre de usuario no debe existir previamente
- El correo electrónico debe tener un formato válido
- Las contraseñas deben coincidir

---

### 2.3 Pantalla de Inicio de Sesión

Permite a los usuarios registrados acceder a su cuenta.

![Pantalla de Inicio de Sesión](imagenes/manual/login.png)

**Campos del formulario:**
- **Nombre de usuario:** El identificador con el que se registró
- **Contraseña:** La clave de acceso asociada a la cuenta

**Acciones disponibles:**
- **Botón "Iniciar Sesión":** Valida las credenciales y accede a la aplicación
- **Enlace "¿No tienes cuenta? Regístrate":** Accede al formulario de registro

---

### 2.4 Menú Principal

Una vez iniciada la sesión, se muestra el menú principal con todas las opciones disponibles.

![Menú Principal](imagenes/manual/menu-principal.png)

**Secciones de la pantalla:**

**Barra superior:**
- Mensaje de bienvenida con el nombre del usuario
- Botón "Cerrar Sesión" para salir de la cuenta

**Panel izquierdo - Mis Cursos:**
- Lista de cursos importados en la biblioteca del usuario
- Botón "Importar Curso" para añadir nuevos cursos
- Botón "Exportar Curso" para guardar cursos como archivo

**Panel derecho - Acciones:**
- **Iniciar Curso:** Comienza o continúa el curso seleccionado
- **Estadísticas:** Muestra las estadísticas de uso del usuario
- **Repasar Errores:** Practica las preguntas falladas anteriormente

---

### 2.5 Estadísticas

Muestra un resumen del progreso y rendimiento del usuario.

![Pantalla de Estadísticas](imagenes/manual/estadisticas.png)

**Información mostrada:**

**Diagrama circular:**
- Representación visual del porcentaje de aciertos vs errores
- Porcentaje de aciertos destacado

**Tarjetas de estadísticas:**
- **Ejercicios:** Número total de ejercicios completados
- **Tiempo de uso:** Tiempo total dedicado al estudio (en minutos)
- **Racha actual:** Días consecutivos usando la aplicación
- **Mejor racha:** Récord de días consecutivos

---

### 2.6 Gestión de Cursos

#### 2.6.1 Importar Curso

Permite añadir nuevos cursos a la biblioteca desde archivos JSON o YAML.

![Importar Curso](imagenes/manual/importar-curso.png)

**Proceso de importación:**
1. Pulsar el botón "Importar Curso" en el menú principal
2. Seleccionar el archivo del curso (formato .json o .yaml)
3. El sistema valida el formato y estructura del archivo
4. Si es válido, el curso aparece en la lista de cursos

**Formatos soportados:**
- JSON (.json)
- YAML (.yaml, .yml)

#### 2.6.2 Exportar Curso

Permite guardar un curso como archivo para compartirlo o hacer copia de seguridad.

![Exportar Curso](imagenes/manual/exportar-curso.png)

**Proceso de exportación:**
1. Seleccionar el curso a exportar en la lista
2. Pulsar el botón "Exportar Curso"
3. Elegir el formato de exportación (JSON o YAML)
4. Seleccionar la ubicación y nombre del archivo
5. El curso se guarda en el formato seleccionado

---

### 2.7 Iniciar un Curso

#### 2.7.1 Selección del Curso

Para comenzar a practicar, selecciona un curso de tu biblioteca haciendo clic sobre él y pulsa "Iniciar Curso".

![Selección de Curso](imagenes/manual/seleccion-curso.png)

#### 2.7.2 Continuar o Reiniciar

Si el curso tiene progreso guardado, se mostrará un diálogo con dos opciones:

![Continuar o Reiniciar](imagenes/manual/continuar-reiniciar.png)

- **Continuar:** Retoma el curso desde donde lo dejaste, manteniendo aciertos, errores, tiempo y estrategia
- **Empezar de cero:** Reinicia el progreso completamente

#### 2.7.3 Selección de Estrategia

Antes de comenzar, debes elegir una estrategia de aprendizaje:

![Selección de Estrategia](imagenes/manual/seleccion-estrategia.png)

**Estrategias disponibles:**

| Estrategia | Descripción |
|------------|-------------|
| **Secuencial** | Las preguntas aparecen en el orden definido en el curso |
| **Aleatoria** | Las preguntas aparecen en orden aleatorio |
| **Repetición Espaciada** | Las preguntas falladas se repiten al final de la sesión |

#### 2.7.4 Ventana de Preparación

Tras seleccionar la estrategia, se muestra una ventana de preparación:

![Ventana de Preparación](imagenes/manual/preparacion.png)

- Muestra el nombre del curso y la estrategia seleccionada
- El botón "Empezar" inicia el temporizador y los ejercicios

---

### 2.8 Realización del Curso

#### 2.8.1 Interfaz de Ejercicios

Durante la realización del curso, la interfaz muestra:

![Interfaz de Ejercicios](imagenes/manual/interfaz-ejercicios.png)

**Elementos de la pantalla:**

**Barra superior:**
- Número de pregunta actual / total de preguntas
- Temporizador con el tiempo transcurrido
- Botón "Pausar" para detener el temporizador
- Botón "Guardar y Salir" para guardar el progreso y salir

**Barra de progreso:**
- Indicador visual del avance en el curso

**Contadores:**
- Aciertos (en verde)
- Errores (en rojo)

**Área de ejercicio:**
- Enunciado de la pregunta
- Opciones o campo de respuesta según el tipo de ejercicio

**Botón "Siguiente":**
- Avanza a la siguiente pregunta (se activa tras responder)

#### 2.8.2 Controles durante el ejercicio

**Pausar/Reanudar:**

![Botón Pausar](imagenes/manual/boton-pausar.png)

- El botón naranja "Pausar" detiene el temporizador
- Cambia a verde "Reanudar" para continuar

**Guardar y Salir:**

![Botón Guardar y Salir](imagenes/manual/boton-guardar.png)

- Guarda el progreso actual (pregunta, aciertos, errores, tiempo)
- Cierra la ventana de ejercicios
- Permite retomar el curso posteriormente

---

### 2.9 Tipos de Ejercicios

#### 2.9.1 Ejercicio Tipo Test

Muestra una pregunta con tres opciones de respuesta.

![Ejercicio Test](imagenes/manual/ejercicio-test.png)

**Cómo responder:**
1. Lee la pregunta en el enunciado
2. Haz clic en la opción que consideres correcta
3. La respuesta se valida automáticamente

**Feedback visual:**
- Respuesta correcta: Se resalta en verde
- Respuesta incorrecta: Se resalta en rojo y se muestra la correcta en verde

![Test Correcto](imagenes/manual/test-correcto.png)
![Test Incorrecto](imagenes/manual/test-incorrecto.png)

#### 2.9.2 Ejercicio de Traducción

Muestra una palabra o frase para traducir.

![Ejercicio Traducción](imagenes/manual/ejercicio-traduccion.png)

**Cómo responder:**
1. Lee la palabra o frase a traducir
2. Escribe la traducción en el campo de texto
3. Pulsa la tecla **Enter** para validar

**Nota:** La validación ignora mayúsculas y minúsculas.

#### 2.9.3 Ejercicio de Rellenar Huecos

Muestra una frase con una palabra oculta que debes completar.

![Ejercicio Huecos](imagenes/manual/ejercicio-huecos.png)

**Cómo responder:**
1. Lee la frase con el hueco (representado por ___)
2. Escribe la palabra que falta en el campo de texto
3. Pulsa la tecla **Enter** para validar

---

### 2.10 Reanudar Curso

Si has guardado el progreso de un curso, puedes reanudarlo en cualquier momento.

![Reanudar Curso](imagenes/manual/reanudar-curso.png)

**Proceso:**
1. Selecciona el curso en tu biblioteca
2. Pulsa "Iniciar Curso"
3. Elige "Continuar" en el diálogo
4. Se restaurará:
   - La pregunta donde lo dejaste
   - Tus aciertos y errores
   - El tiempo transcurrido
   - La estrategia de aprendizaje

---

### 2.11 Finalización del Curso

Al completar todas las preguntas del curso, se muestra un resumen de resultados.

![Resultado Final](imagenes/manual/resultado-final.png)

**Información mostrada:**
- Número de aciertos
- Número de errores
- Porcentaje de aciertos
- Tiempo total empleado

**Después de finalizar:**
- El tiempo se suma a las estadísticas globales
- Las preguntas falladas se guardan para repaso
- El progreso del curso se reinicia

---

### 2.12 Repasar Errores Frecuentes

Esta función permite practicar las preguntas que has fallado anteriormente.

![Repasar Errores](imagenes/manual/repasar-errores.png)

**Cómo funciona:**
1. Pulsa "Repasar Errores" en el menú principal
2. Se muestran las preguntas que has fallado en cualquier curso
3. Por cada acierto, el contador de errores de esa pregunta se reduce
4. Cuando domines una pregunta (contador = 0), se elimina de la lista

**Si no tienes errores:**

![Sin Errores](imagenes/manual/sin-errores.png)

Se mostrará un mensaje indicando que no hay errores pendientes para repasar.

---

## 3. Anexo: Formato de Cursos

Los cursos pueden crearse manualmente en formato JSON o YAML. A continuación se muestra la estructura esperada:

### Formato JSON
```json
{
  "titulo": "Inglés Básico",
  "descripcion": "Curso de inglés para principiantes",
  "idioma": "Inglés",
  "lecciones": [
    {
      "nombre": "Lección 1 - Animales",
      "descripcion": "Aprende nombres de animales",
      "orden": 1,
      "preguntas": [
        {
          "tipo": "test",
          "enunciado": "¿Cómo se dice 'perro' en inglés?",
          "orden": 1,
          "opcionCorrecta": "dog",
          "opcionIncorrecta1": "cat",
          "opcionIncorrecta2": "bird"
        },
        {
          "tipo": "traduccion",
          "enunciado": "Traduce: 'cat'",
          "orden": 2,
          "respuestaCorrecta": "gato"
        },
        {
          "tipo": "huecos",
          "enunciado": "Completa: The ___ is flying",
          "orden": 3,
          "fraseCompleta": "The bird is flying",
          "palabraOculta": "bird"
        }
      ]
    }
  ]
}
```

### Campos por tipo de pregunta

| Tipo | Campos requeridos |
|------|-------------------|
| `test` | `enunciado`, `opcionCorrecta`, `opcionIncorrecta1`, `opcionIncorrecta2` |
| `traduccion` | `enunciado`, `respuestaCorrecta` |
| `huecos` | `enunciado`, `fraseCompleta`, `palabraOculta` |

---

**Versión:** 1.0  
**Última actualización:** Enero 2026
