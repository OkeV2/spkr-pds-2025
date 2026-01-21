package es.um.pds.spkr;

import es.um.pds.spkr.catalogo.CatalogoCursos;
import es.um.pds.spkr.catalogo.CatalogoUsuarios;
import es.um.pds.spkr.modelo.Usuario;
import es.um.pds.spkr.modelo.Estadisticas;
import es.um.pds.spkr.modelo.Biblioteca;
import es.um.pds.spkr.modelo.ErrorFrecuente;
import es.um.pds.spkr.modelo.Pregunta;
import es.um.pds.spkr.modelo.Curso;
import es.um.pds.spkr.modelo.Leccion;
import es.um.pds.spkr.modelo.Progreso;
import es.um.pds.spkr.modelo.ResultadoRespuesta;
import es.um.pds.spkr.modelo.PreguntaTest;
import es.um.pds.spkr.modelo.PreguntaTraduccion;
import es.um.pds.spkr.modelo.PreguntaHuecos;
import es.um.pds.spkr.cargador.CargadorCursosJSON;
import es.um.pds.spkr.cargador.CargadorCursosYAML;
import es.um.pds.spkr.estrategia.EstrategiaAleatoria;
import es.um.pds.spkr.estrategia.EstrategiaAprendizaje;
import es.um.pds.spkr.estrategia.EstrategiaRepeticionEspaciada;
import es.um.pds.spkr.estrategia.EstrategiaSecuencial;
import es.um.pds.spkr.persistencia.GestorPersistencia;
import es.um.pds.spkr.persistencia.InicializadorDatos;

import java.util.List;

public class SpkrApp {

    private CatalogoUsuarios catalogoUsuarios;
    private CatalogoCursos catalogoCursos;
    private Usuario usuarioActual;

    // Estado de sesión de ejercicio (controlado por el controlador, no por la vista)
    private List<Pregunta> preguntasSesion;
    private int indicePreguntaActual;
    private int aciertosSesion;
    private int erroresSesion;
    private Progreso progresoActual;
    private EstrategiaAprendizaje estrategiaActual;
    private int segundosSesion;
    private Pregunta preguntaActualSesion; // Pregunta actual para encapsular acceso (MVC)
    private TipoPregunta tipoPreguntaActualSesion; // Tipo de pregunta actual (MVC)

    // Estado de sesión de repaso
    private List<ErrorFrecuente> erroresRepasoSesion;
    private int indiceRepasoActual;
    private int aciertosRepaso;
    private int erroresRepaso;
    
    public SpkrApp() {
        this.catalogoUsuarios = new CatalogoUsuarios();
        this.catalogoCursos = new CatalogoCursos();
        this.usuarioActual = null;
        InicializadorDatos.inicializar(catalogoUsuarios);
    }
    
    public boolean registrarUsuario(String nombreUsuario, String email, String password) {
        if (catalogoUsuarios.existeUsuario(nombreUsuario)) {
            return false;
        }
        if (catalogoUsuarios.existeEmail(email)) {
            return false;
        }
        
        Usuario nuevoUsuario = new Usuario(nombreUsuario, email, password);
        nuevoUsuario.setEstadisticas(new Estadisticas());
        nuevoUsuario.setBiblioteca(new Biblioteca());
        catalogoUsuarios.addUsuario(nuevoUsuario);
        return true;
    }
    
    public boolean login(String nombreUsuario, String password) {
        if (catalogoUsuarios.validarCredenciales(nombreUsuario, password)) {
            this.usuarioActual = catalogoUsuarios.getUsuario(nombreUsuario);
            usuarioActual.getEstadisticas().actualizarRacha();
            catalogoUsuarios.actualizarUsuario(usuarioActual);
            return true;
        }
        return false;
    }
    
    public void logout() {
        if (usuarioActual != null) {
            catalogoUsuarios.actualizarUsuario(usuarioActual);
        }
        this.usuarioActual = null;
    }
    
    public void guardarProgreso() {
        if (usuarioActual != null) {
            catalogoUsuarios.actualizarUsuario(usuarioActual);
        }
    }
    
    public boolean estaLogueado() {
        return this.usuarioActual != null;
    }
    
    public void cerrar() {
        if (usuarioActual != null) {
            catalogoUsuarios.actualizarUsuario(usuarioActual);
        }
        GestorPersistencia.getInstancia().cerrar();
    }
    
    public void eliminarErrorFrecuente(ErrorFrecuente error) {
        if (usuarioActual != null) {
            usuarioActual.removeErrorFrecuente(error);
            GestorPersistencia.getInstancia().eliminar(error);
        }
    }

    public void procesarAciertoRepaso(ErrorFrecuente error) {
        if (usuarioActual != null && error != null) {
            error.reducirErrores();
            if (error.estaDominada()) {
                eliminarErrorFrecuente(error);
            }
        }
    }

    public void registrarErrorEjercicio(Pregunta pregunta) {
        if (usuarioActual == null || pregunta == null) {
            return;
        }
        List<ErrorFrecuente> erroresFrecuentes = usuarioActual.getErroresFrecuentes();
        for (ErrorFrecuente ef : erroresFrecuentes) {
            if (ef.getPregunta().equals(pregunta)) {
                ef.incrementarErrores();
                return;
            }
        }
        ErrorFrecuente nuevoError = new ErrorFrecuente(pregunta);
        usuarioActual.addErrorFrecuente(nuevoError);
    }

    public void incrementarEjerciciosCompletados() {
        if (usuarioActual != null) {
            usuarioActual.getEstadisticas().incrementarEjercicios();
        }
    }

    public void incrementarTiempoEstadisticas(int segundos) {
        if (usuarioActual != null) {
            int minutos = segundos / 60;
            if (minutos > 0) {
                usuarioActual.getEstadisticas().incrementarTiempo(minutos);
            }
        }
    }

    public List<Curso> getCursosBiblioteca() {
        if (usuarioActual == null) {
            return List.of();
        }
        return usuarioActual.getBiblioteca().getCursos();
    }

    public Curso getCursoBiblioteca(int index) {
        List<Curso> cursos = getCursosBiblioteca();
        if (index >= 0 && index < cursos.size()) {
            return cursos.get(index);
        }
        return null;
    }

    public Progreso buscarProgresoCurso(Curso curso) {
        if (usuarioActual == null || curso == null) {
            return null;
        }
        for (Progreso p : usuarioActual.getProgresos()) {
            if (p.getCurso() != null && p.getCurso().getTitulo() != null &&
                p.getCurso().getTitulo().equals(curso.getTitulo())) {
                return p;
            }
        }
        return null;
    }

    public Progreso crearNuevoProgreso(Curso curso) {
        if (usuarioActual == null || curso == null) {
            return null;
        }
        Progreso nuevoProgreso = new Progreso(curso);
        usuarioActual.addProgreso(nuevoProgreso);
        guardarProgreso();
        return nuevoProgreso;
    }

    public int calcularTotalPreguntas(Curso curso) {
        if (curso == null) {
            return 0;
        }
        int total = 0;
        for (Leccion l : curso.getLecciones()) {
            total += l.getPreguntas().size();
        }
        return total;
    }

    public int obtenerNumeroLecciones(Curso curso) {
        if (curso == null) {
            return 0;
        }
        return curso.getLecciones().size();
    }

    public boolean cursoTieneLecciones(Curso curso) {
        if (curso == null) {
            return false;
        }
        return !curso.getLecciones().isEmpty();
    }

    public List<Pregunta> obtenerTodasLasPreguntas(Curso curso) {
        if (curso == null) {
            return List.of();
        }
        List<Pregunta> todasLasPreguntas = new java.util.ArrayList<>();
        for (Leccion leccion : curso.getLecciones()) {
            todasLasPreguntas.addAll(leccion.getPreguntas());
        }
        return todasLasPreguntas;
    }

    public boolean tieneErroresFrecuentes() {
        return usuarioActual != null && !usuarioActual.getErroresFrecuentes().isEmpty();
    }

    public String getNombreUsuarioActual() {
        if (usuarioActual == null) {
            return "";
        }
        return usuarioActual.getNombreUsuario();
    }

    public Estadisticas obtenerEstadisticasActuales() {
        if (usuarioActual == null) {
            return null;
        }
        return usuarioActual.getEstadisticas();
    }

    public List<ErrorFrecuente> obtenerErroresFrecuentesActuales() {
        if (usuarioActual == null) {
            return List.of();
        }
        return usuarioActual.getErroresFrecuentes();
    }

    public int contarErroresFrecuentes() {
        if (usuarioActual == null) {
            return 0;
        }
        return usuarioActual.getErroresFrecuentes().size();
    }

    // Métodos de gestión de Progreso

    public void registrarAciertoProgreso(Progreso progreso) {
        if (progreso != null) {
            progreso.registrarAcierto();
        }
    }

    public void registrarErrorProgreso(Progreso progreso) {
        if (progreso != null) {
            progreso.registrarError();
        }
    }

    public void reiniciarProgreso(Progreso progreso) {
        if (progreso != null) {
            progreso.reiniciar();
            guardarProgreso();
        }
    }

    public void actualizarTiempoProgreso(Progreso progreso, int segundos) {
        if (progreso != null) {
            progreso.setTiempoSegundos(segundos);
        }
    }

    public void asignarEstrategiaProgreso(Progreso progreso, String nombreEstrategia) {
        if (progreso != null) {
            progreso.setEstrategia(nombreEstrategia);
            guardarProgreso();
        }
    }

    // =====================================================
    // LÓGICA DE DECISIÓN DE NAVEGACIÓN
    // =====================================================

    /**
     * Resultado de la decisión de iniciar un curso.
     * Encapsula la lógica de negocio sobre qué acción tomar.
     */
    public enum AccionIniciarCurso {
        SIN_LECCIONES,           // El curso no tiene lecciones
        CONTINUAR_PROGRESO,      // Hay progreso existente, continuar
        CURSO_COMPLETADO,        // El curso ya fue completado
        SELECCIONAR_ESTRATEGIA   // Mostrar selección de estrategia
    }

    /**
     * Enum que representa los tipos de pregunta.
     * Encapsula el tipo de pregunta para que las vistas no usen instanceof.
     */
    public enum TipoPregunta {
        TEST,
        TRADUCCION,
        HUECOS
    }

    /**
     * Obtiene el tipo de una pregunta sin exponer la jerarquía de clases.
     * Encapsula el instanceof para respetar la ocultación de información.
     */
    public TipoPregunta obtenerTipoPregunta(Pregunta pregunta) {
        if (pregunta instanceof PreguntaTest) {
            return TipoPregunta.TEST;
        } else if (pregunta instanceof PreguntaTraduccion) {
            return TipoPregunta.TRADUCCION;
        } else if (pregunta instanceof PreguntaHuecos) {
            return TipoPregunta.HUECOS;
        }
        return null;
    }

    /**
     * Obtiene las opciones de una pregunta de tipo test.
     * Retorna una lista con las opciones mezcladas.
     */
    public List<String> obtenerOpcionesTest(Pregunta pregunta) {
        if (!(pregunta instanceof PreguntaTest)) {
            return List.of();
        }
        PreguntaTest pt = (PreguntaTest) pregunta;
        List<String> opciones = new java.util.ArrayList<>();
        opciones.add(pt.getOpcionCorrecta());
        opciones.add(pt.getOpcionIncorrecta1());
        opciones.add(pt.getOpcionIncorrecta2());
        java.util.Collections.shuffle(opciones);
        return opciones;
    }

    /**
     * Determina qué acción tomar al intentar iniciar un curso.
     * Centraliza la lógica de decisión que antes estaba en la vista.
     */
    public AccionIniciarCurso determinarAccionIniciarCurso(Curso curso) {
        if (!cursoTieneLecciones(curso)) {
            return AccionIniciarCurso.SIN_LECCIONES;
        }

        Progreso progresoExistente = buscarProgresoCurso(curso);
        int totalPreguntas = calcularTotalPreguntas(curso);

        if (progresoExistente != null && progresoExistente.getPreguntaActual() > 0) {
            if (progresoExistente.getPreguntaActual() < totalPreguntas) {
                return AccionIniciarCurso.CONTINUAR_PROGRESO;
            } else {
                return AccionIniciarCurso.CURSO_COMPLETADO;
            }
        }

        return AccionIniciarCurso.SELECCIONAR_ESTRATEGIA;
    }

    /**
     * Prepara el progreso para iniciar un curso (crea uno nuevo si no existe).
     */
    public Progreso prepararProgresoParaCurso(Curso curso) {
        Progreso progreso = buscarProgresoCurso(curso);
        if (progreso == null) {
            progreso = crearNuevoProgreso(curso);
        }
        return progreso;
    }

    /**
     * Obtiene información del progreso para mostrar al usuario.
     */
    public String obtenerInfoProgreso(Curso curso) {
        Progreso progreso = buscarProgresoCurso(curso);
        if (progreso == null) {
            return "";
        }
        int totalPreguntas = calcularTotalPreguntas(curso);
        return "Pregunta " + progreso.getPreguntaActual() + " de " + totalPreguntas;
    }

    // Factory de estrategias

    public EstrategiaAprendizaje crearEstrategia(String nombreEstrategia) {
        if ("Aleatoria".equals(nombreEstrategia)) {
            return new EstrategiaAleatoria();
        } else if ("Repetición Espaciada".equals(nombreEstrategia)) {
            return new EstrategiaRepeticionEspaciada();
        } else {
            return new EstrategiaSecuencial();
        }
    }

    public Curso importarCurso(String ruta) throws Exception {
        Curso curso;
        if (ruta.endsWith(".json")) {
            CargadorCursosJSON cargador = new CargadorCursosJSON();
            curso = cargador.cargarCurso(ruta);
        } else if (ruta.endsWith(".yaml") || ruta.endsWith(".yml")) {
            CargadorCursosYAML cargador = new CargadorCursosYAML();
            curso = cargador.cargarCurso(ruta);
        } else {
            throw new IllegalArgumentException("Formato no soportado. Use JSON o YAML.");
        }

        if (usuarioActual != null) {
            usuarioActual.getBiblioteca().addCurso(curso);
            guardarProgreso();
        }
        return curso;
    }

    public void exportarCurso(Curso curso, String ruta) throws Exception {
        if (curso == null) {
            throw new IllegalArgumentException("Curso no especificado");
        }
        if (ruta.endsWith(".json")) {
            CargadorCursosJSON cargador = new CargadorCursosJSON();
            cargador.exportarCurso(curso, ruta);
        } else if (ruta.endsWith(".yaml") || ruta.endsWith(".yml")) {
            CargadorCursosYAML cargador = new CargadorCursosYAML();
            cargador.exportarCurso(curso, ruta);
        } else {
            CargadorCursosJSON cargador = new CargadorCursosJSON();
            cargador.exportarCurso(curso, ruta + ".json");
        }
    }

    // Métodos de lógica de negocio para ejercicios

    /**
     * Procesa una respuesta de ejercicio y actualiza el progreso y estadísticas.
     * Encapsula toda la lógica de negocio que antes estaba en VentanaEjercicio.
     */
    public ResultadoRespuesta procesarRespuestaEjercicio(Pregunta pregunta, String respuesta,
            Progreso progreso, EstrategiaAprendizaje estrategia) {

        boolean correcta = pregunta.validarRespuesta(respuesta);
        boolean yaContada = false;

        if (estrategia instanceof EstrategiaRepeticionEspaciada) {
            yaContada = ((EstrategiaRepeticionEspaciada) estrategia).esPreguntaYaContada(pregunta);
        }

        if (correcta) {
            if (!yaContada) {
                registrarAciertoProgreso(progreso);
                if (estrategia instanceof EstrategiaRepeticionEspaciada) {
                    ((EstrategiaRepeticionEspaciada) estrategia).marcarComoContada(pregunta);
                }
            }
            if (estrategia instanceof EstrategiaRepeticionEspaciada) {
                ((EstrategiaRepeticionEspaciada) estrategia).registrarAcierto(pregunta);
            }
        } else {
            if (!yaContada) {
                registrarErrorProgreso(progreso);
                registrarErrorEjercicio(pregunta);
                if (estrategia instanceof EstrategiaRepeticionEspaciada) {
                    ((EstrategiaRepeticionEspaciada) estrategia).marcarComoContada(pregunta);
                }
            }
            if (estrategia instanceof EstrategiaRepeticionEspaciada) {
                ((EstrategiaRepeticionEspaciada) estrategia).registrarFallo(pregunta);
            }
        }

        if (!yaContada) {
            incrementarEjerciciosCompletados();
        }

        String respuestaCorrecta = obtenerRespuestaCorrecta(pregunta);

        return new ResultadoRespuesta(correcta, yaContada, respuestaCorrecta,
                progreso.getAciertos(), progreso.getErrores());
    }

    /**
     * Procesa una respuesta en el modo repaso.
     */
    public ResultadoRespuesta procesarRespuestaRepaso(ErrorFrecuente error, String respuesta) {
        Pregunta pregunta = error.getPregunta();
        boolean correcta = pregunta.validarRespuesta(respuesta);

        if (correcta) {
            procesarAciertoRepaso(error);
        }

        String respuestaCorrecta = obtenerRespuestaCorrecta(pregunta);
        return new ResultadoRespuesta(correcta, false, respuestaCorrecta, 0, 0);
    }

    /**
     * Obtiene la respuesta correcta de una pregunta según su tipo.
     */
    public String obtenerRespuestaCorrecta(Pregunta pregunta) {
        if (pregunta instanceof PreguntaTest) {
            return ((PreguntaTest) pregunta).getOpcionCorrecta();
        } else if (pregunta instanceof PreguntaTraduccion) {
            return ((PreguntaTraduccion) pregunta).getRespuestaCorrecta();
        } else if (pregunta instanceof PreguntaHuecos) {
            return ((PreguntaHuecos) pregunta).getPalabraOculta();
        }
        return "";
    }

    /**
     * Calcula el porcentaje de aciertos del usuario actual.
     */
    public int calcularPorcentajeAciertos() {
        if (usuarioActual == null) {
            return 0;
        }
        Estadisticas stats = usuarioActual.getEstadisticas();
        int erroresPendientes = usuarioActual.getErroresFrecuentes().size();
        int aciertos = stats.getEjerciciosCompletados() - erroresPendientes;
        if (aciertos < 0) {
            aciertos = stats.getEjerciciosCompletados();
        }
        int total = aciertos + erroresPendientes;
        return total > 0 ? (aciertos * 100) / total : 0;
    }

    /**
     * Obtiene el número de aciertos calculado para estadísticas.
     */
    public int obtenerAciertosEstadisticas() {
        if (usuarioActual == null) {
            return 0;
        }
        Estadisticas stats = usuarioActual.getEstadisticas();
        int erroresPendientes = usuarioActual.getErroresFrecuentes().size();
        int aciertos = stats.getEjerciciosCompletados() - erroresPendientes;
        return aciertos < 0 ? stats.getEjerciciosCompletados() : aciertos;
    }

    /**
     * Calcula el porcentaje de progreso de un curso.
     */
    public int calcularPorcentajeProgreso(Curso curso, Progreso progreso) {
        if (curso == null || progreso == null) {
            return 0;
        }
        if (progreso.isCompletado()) {
            return 100;
        }
        int totalPreguntas = calcularTotalPreguntas(curso);
        if (totalPreguntas == 0) {
            return 0;
        }
        return (progreso.getPreguntaActual() * 100) / totalPreguntas;
    }

    /**
     * Verifica si un curso tiene progreso iniciado (sin exponer el objeto Progreso).
     * Encapsula el acceso al modelo para respetar MVC.
     */
    public boolean tieneProgresoIniciado(Curso curso) {
        Progreso progreso = buscarProgresoCurso(curso);
        if (progreso == null) {
            return false;
        }
        return progreso.getPreguntaActual() > 0 || progreso.isCompletado();
    }

    /**
     * Obtiene el porcentaje de progreso de un curso (sin exponer el objeto Progreso).
     * Encapsula el acceso al modelo para respetar MVC.
     */
    public int obtenerPorcentajeProgresoCurso(Curso curso) {
        Progreso progreso = buscarProgresoCurso(curso);
        return calcularPorcentajeProgreso(curso, progreso);
    }

    /**
     * Obtiene la estrategia guardada de un progreso de curso.
     * Encapsula el acceso al modelo para respetar MVC.
     */
    public String obtenerEstrategiaProgresoCurso(Curso curso) {
        Progreso progreso = buscarProgresoCurso(curso);
        if (progreso == null) {
            return null;
        }
        return progreso.getEstrategia();
    }

    /**
     * Finaliza un ejercicio guardando el tiempo y las estadísticas.
     */
    public void finalizarEjercicio(Progreso progreso, int segundosTotales) {
        actualizarTiempoProgreso(progreso, segundosTotales);
        incrementarTiempoEstadisticas(segundosTotales);
        // Guardar el progreso explícitamente para asegurar que se persista
        GestorPersistencia.getInstancia().actualizar(progreso);
        guardarProgreso();
    }

    // Getters

    public Usuario getUsuarioActual() {
        return usuarioActual;
    }

    // =====================================================
    // GESTIÓN DE SESIÓN DE EJERCICIO
    // =====================================================

    /**
     * Inicia una nueva sesión de ejercicio con el curso y estrategia dados.
     */
    public void iniciarSesionEjercicio(Curso curso, Progreso progreso, EstrategiaAprendizaje estrategia) {
        this.progresoActual = progreso;
        this.estrategiaActual = estrategia;
        this.preguntasSesion = new java.util.ArrayList<>(obtenerTodasLasPreguntas(curso));
        this.indicePreguntaActual = progreso.getPreguntaActual();
        this.aciertosSesion = progreso.getAciertos();
        this.erroresSesion = progreso.getErrores();
        this.segundosSesion = progreso.getTiempoSegundos();
    }

    /**
     * Obtiene la siguiente pregunta de la sesión actual.
     * @deprecated Usar cargarSiguientePreguntaSesion() y los métodos de acceso encapsulados
     */
    public Pregunta obtenerSiguientePreguntaSesion() {
        if (estrategiaActual == null || preguntasSesion == null) {
            return null;
        }
        return estrategiaActual.siguientePregunta(preguntasSesion, indicePreguntaActual);
    }

    /**
     * Carga la siguiente pregunta de la sesión y la almacena internamente.
     * Retorna true si hay una pregunta disponible, false si la sesión terminó.
     * Las vistas deben usar este método y luego los métodos de acceso encapsulados.
     */
    public boolean cargarSiguientePreguntaSesion() {
        if (estrategiaActual == null || preguntasSesion == null) {
            preguntaActualSesion = null;
            tipoPreguntaActualSesion = null;
            return false;
        }
        preguntaActualSesion = estrategiaActual.siguientePregunta(preguntasSesion, indicePreguntaActual);
        if (preguntaActualSesion != null) {
            tipoPreguntaActualSesion = obtenerTipoPregunta(preguntaActualSesion);
        } else {
            tipoPreguntaActualSesion = null;
        }
        return preguntaActualSesion != null;
    }

    /**
     * Obtiene el enunciado de la pregunta actual de la sesión.
     * Encapsula el acceso al modelo para respetar MVC.
     */
    public String obtenerEnunciadoPreguntaSesionActual() {
        return preguntaActualSesion != null ? preguntaActualSesion.getEnunciado() : "";
    }

    /**
     * Obtiene el tipo de la pregunta actual de la sesión.
     * Encapsula el acceso al modelo para respetar MVC.
     */
    public TipoPregunta obtenerTipoPreguntaSesionActual() {
        return tipoPreguntaActualSesion;
    }

    /**
     * Obtiene las opciones de test de la pregunta actual de la sesión.
     * Encapsula el acceso al modelo para respetar MVC.
     */
    public List<String> obtenerOpcionesTestSesionActual() {
        return obtenerOpcionesTest(preguntaActualSesion);
    }

    /**
     * Obtiene la respuesta correcta de la pregunta actual de la sesión.
     * Encapsula el acceso al modelo para respetar MVC.
     */
    public String obtenerRespuestaCorrectaSesionActual() {
        return obtenerRespuestaCorrecta(preguntaActualSesion);
    }

    /**
     * Procesa la respuesta del ejercicio actual usando la pregunta almacenada internamente.
     * Encapsula el acceso al modelo para respetar MVC.
     */
    public ResultadoRespuesta procesarRespuestaSesionActual(String respuesta) {
        if (preguntaActualSesion == null) {
            return null;
        }
        ResultadoRespuesta resultado = procesarRespuestaEjercicio(
                preguntaActualSesion, respuesta, progresoActual, estrategiaActual);

        if (!resultado.isYaContada()) {
            if (resultado.isCorrecta()) {
                aciertosSesion = resultado.getAciertosActuales();
            } else {
                erroresSesion = resultado.getErroresActuales();
            }
        }

        return resultado;
    }

    /**
     * Procesa la respuesta del ejercicio actual y avanza el estado de la sesión.
     * @deprecated Usar procesarRespuestaSesionActual(String respuesta) para mejor encapsulación
     */
    public ResultadoRespuesta procesarRespuestaSesion(Pregunta pregunta, String respuesta) {
        ResultadoRespuesta resultado = procesarRespuestaEjercicio(
                pregunta, respuesta, progresoActual, estrategiaActual);

        if (!resultado.isYaContada()) {
            if (resultado.isCorrecta()) {
                aciertosSesion = resultado.getAciertosActuales();
            } else {
                erroresSesion = resultado.getErroresActuales();
            }
        }

        return resultado;
    }

    /**
     * Avanza a la siguiente pregunta en la sesión.
     */
    public void avanzarPreguntaSesion() {
        indicePreguntaActual++;
    }

    /**
     * Verifica si la sesión de ejercicio ha terminado.
     */
    public boolean sesionEjercicioTerminada() {
        return obtenerSiguientePreguntaSesion() == null;
    }

    /**
     * Finaliza la sesión de ejercicio actual.
     * El Progreso ya tiene los valores correctos (actualizados en registrarAcierto/Error),
     * solo necesitamos guardar el tiempo y persistir.
     */
    public void finalizarSesionEjercicio() {
        if (progresoActual != null) {
            finalizarEjercicio(progresoActual, segundosSesion);
        }
        limpiarSesionEjercicio();
    }

    /**
     * Limpia el estado de la sesión de ejercicio.
     */
    private void limpiarSesionEjercicio() {
        preguntasSesion = null;
        progresoActual = null;
        estrategiaActual = null;
        indicePreguntaActual = 0;
        aciertosSesion = 0;
        erroresSesion = 0;
        segundosSesion = 0;
    }

    /**
     * Incrementa el tiempo de la sesión actual.
     */
    public void incrementarTiempoSesion() {
        segundosSesion++;
    }

    // Getters de estado de sesión de ejercicio

    public int getIndicePreguntaActual() {
        return indicePreguntaActual;
    }

    public int getAciertosSesion() {
        return aciertosSesion;
    }

    public int getErroresSesion() {
        return erroresSesion;
    }

    public int getSegundosSesion() {
        return segundosSesion;
    }

    public int getTotalPreguntasSesion() {
        return preguntasSesion != null ? preguntasSesion.size() : 0;
    }

    /**
     * Calcula el porcentaje de aciertos de la sesión actual.
     * Centraliza el cálculo que antes estaba duplicado en la vista.
     */
    public int calcularPorcentajeSesion() {
        int total = aciertosSesion + erroresSesion;
        if (total == 0) {
            return 0;
        }
        return (aciertosSesion * 100) / total;
    }

    public Progreso getProgresoActual() {
        return progresoActual;
    }

    public EstrategiaAprendizaje getEstrategiaActual() {
        return estrategiaActual;
    }

    // =====================================================
    // GESTIÓN DE SESIÓN DE REPASO
    // =====================================================

    /**
     * Inicia una nueva sesión de repaso de errores.
     */
    public void iniciarSesionRepaso() {
        this.erroresRepasoSesion = new java.util.ArrayList<>(obtenerErroresFrecuentesActuales());
        this.indiceRepasoActual = 0;
        this.aciertosRepaso = 0;
        this.erroresRepaso = 0;
    }

    /**
     * Obtiene el error frecuente actual de la sesión de repaso.
     */
    public ErrorFrecuente obtenerErrorActualRepaso() {
        if (erroresRepasoSesion == null || indiceRepasoActual >= erroresRepasoSesion.size()) {
            return null;
        }
        return erroresRepasoSesion.get(indiceRepasoActual);
    }

    /**
     * Procesa la respuesta de la sesión de repaso actual.
     */
    public ResultadoRespuesta procesarRespuestaSesionRepaso(String respuesta) {
        ErrorFrecuente errorActual = obtenerErrorActualRepaso();
        if (errorActual == null) {
            return null;
        }

        ResultadoRespuesta resultado = procesarRespuestaRepaso(errorActual, respuesta);

        if (resultado.isCorrecta()) {
            aciertosRepaso++;
        } else {
            erroresRepaso++;
        }

        return resultado;
    }

    /**
     * Avanza al siguiente error en la sesión de repaso.
     */
    public void avanzarRepasoSesion() {
        indiceRepasoActual++;
    }

    /**
     * Verifica si la sesión de repaso ha terminado.
     */
    public boolean sesionRepasoTerminada() {
        return erroresRepasoSesion == null || indiceRepasoActual >= erroresRepasoSesion.size();
    }

    /**
     * Finaliza la sesión de repaso.
     */
    public void finalizarSesionRepaso() {
        guardarProgreso();
        limpiarSesionRepaso();
    }

    /**
     * Limpia el estado de la sesión de repaso.
     */
    private void limpiarSesionRepaso() {
        erroresRepasoSesion = null;
        indiceRepasoActual = 0;
        aciertosRepaso = 0;
        erroresRepaso = 0;
    }

    // Getters de estado de sesión de repaso

    public int getIndiceRepasoActual() {
        return indiceRepasoActual;
    }

    public int getTotalErroresRepaso() {
        return erroresRepasoSesion != null ? erroresRepasoSesion.size() : 0;
    }

    public int getAciertosRepaso() {
        return aciertosRepaso;
    }

    public int getErroresRepaso() {
        return erroresRepaso;
    }

    // =====================================================
    // UTILIDADES
    // =====================================================

    /**
     * Formatea segundos en formato HH:MM:SS o MM:SS.
     */
    public String formatearTiempo(int totalSegundos) {
        int horas = totalSegundos / 3600;
        int minutos = (totalSegundos % 3600) / 60;
        int segundos = totalSegundos % 60;
        if (horas > 0) {
            return String.format("%d:%02d:%02d", horas, minutos, segundos);
        }
        return String.format("%02d:%02d", minutos, segundos);
    }

    // =====================================================
    // MÉTODOS ENCAPSULADOS PARA ESTADÍSTICAS (MVC)
    // Las vistas no deben acceder directamente al modelo Estadisticas
    // =====================================================

    /**
     * Obtiene el tiempo total de uso en minutos.
     */
    public int obtenerTiempoTotalUso() {
        if (usuarioActual == null || usuarioActual.getEstadisticas() == null) {
            return 0;
        }
        return usuarioActual.getEstadisticas().getTiempoTotalUso();
    }

    /**
     * Obtiene la racha actual en días.
     */
    public int obtenerRachaActual() {
        if (usuarioActual == null || usuarioActual.getEstadisticas() == null) {
            return 0;
        }
        return usuarioActual.getEstadisticas().getRachaActual();
    }

    /**
     * Obtiene la mejor racha en días.
     */
    public int obtenerMejorRacha() {
        if (usuarioActual == null || usuarioActual.getEstadisticas() == null) {
            return 0;
        }
        return usuarioActual.getEstadisticas().getMejorRacha();
    }

    /**
     * Obtiene el número de ejercicios completados.
     */
    public int obtenerEjerciciosCompletadosTotal() {
        if (usuarioActual == null || usuarioActual.getEstadisticas() == null) {
            return 0;
        }
        return usuarioActual.getEstadisticas().getEjerciciosCompletados();
    }

    // =====================================================
    // MÉTODOS ENCAPSULADOS PARA CURSOS (MVC)
    // Las vistas no deben acceder directamente al modelo Curso
    // =====================================================

    /**
     * Obtiene el título de un curso por su índice en la biblioteca.
     */
    public String obtenerTituloCurso(int index) {
        Curso curso = getCursoBiblioteca(index);
        return curso != null ? curso.getTitulo() : "";
    }

    /**
     * Obtiene el idioma de un curso por su índice en la biblioteca.
     */
    public String obtenerIdiomaCurso(int index) {
        Curso curso = getCursoBiblioteca(index);
        return curso != null ? curso.getIdioma() : "";
    }

    /**
     * Obtiene el título de un curso dado.
     */
    public String obtenerTituloCurso(Curso curso) {
        return curso != null ? curso.getTitulo() : "";
    }

    /**
     * Obtiene el idioma de un curso dado.
     */
    public String obtenerIdiomaCurso(Curso curso) {
        return curso != null ? curso.getIdioma() : "";
    }

    // =====================================================
    // GESTIÓN DE CURSO ACTIVO PARA EJERCICIOS (MVC)
    // Las vistas no deben pasar objetos del modelo entre ellas
    // =====================================================

    private Curso cursoActivo;
    private int indiceCursoActivo = -1;

    /**
     * Establece el curso activo por su índice en la biblioteca.
     * Esto permite que las vistas trabajen con índices en lugar de objetos del modelo.
     */
    public void establecerCursoActivo(int index) {
        this.indiceCursoActivo = index;
        this.cursoActivo = getCursoBiblioteca(index);
    }

    /**
     * Obtiene el curso activo actual.
     */
    public Curso getCursoActivo() {
        return cursoActivo;
    }

    /**
     * Obtiene el título del curso activo.
     */
    public String obtenerTituloCursoActivo() {
        return cursoActivo != null ? cursoActivo.getTitulo() : "";
    }

    /**
     * Obtiene el idioma del curso activo.
     */
    public String obtenerIdiomaCursoActivo() {
        return cursoActivo != null ? cursoActivo.getIdioma() : "";
    }

    /**
     * Obtiene el número de lecciones del curso activo.
     */
    public int obtenerNumeroLeccionesCursoActivo() {
        return obtenerNumeroLecciones(cursoActivo);
    }

    /**
     * Obtiene el total de preguntas del curso activo.
     */
    public int obtenerTotalPreguntasCursoActivo() {
        return calcularTotalPreguntas(cursoActivo);
    }

    /**
     * Determina la acción a tomar al iniciar el curso activo.
     */
    public AccionIniciarCurso determinarAccionIniciarCursoActivo() {
        return determinarAccionIniciarCurso(cursoActivo);
    }

    /**
     * Prepara el progreso para el curso activo.
     */
    public void prepararProgresoParaCursoActivo() {
        if (cursoActivo != null) {
            Progreso progreso = buscarProgresoCurso(cursoActivo);
            if (progreso == null) {
                progreso = crearNuevoProgreso(cursoActivo);
            }
            this.progresoActual = progreso;
        }
    }

    /**
     * Reinicia el progreso del curso activo.
     */
    public void reiniciarProgresoCursoActivo() {
        if (cursoActivo != null) {
            Progreso progreso = buscarProgresoCurso(cursoActivo);
            if (progreso != null) {
                reiniciarProgreso(progreso);
                this.progresoActual = progreso;
            }
        }
    }

    /**
     * Obtiene información del progreso del curso activo.
     */
    public String obtenerInfoProgresoCursoActivo() {
        return obtenerInfoProgreso(cursoActivo);
    }

    /**
     * Obtiene la estrategia guardada del progreso del curso activo.
     */
    public String obtenerEstrategiaProgresoCursoActivo() {
        return obtenerEstrategiaProgresoCurso(cursoActivo);
    }

    /**
     * Verifica si el curso activo tiene progreso iniciado.
     */
    public boolean cursoActivoTieneProgresoIniciado() {
        return tieneProgresoIniciado(cursoActivo);
    }

    /**
     * Obtiene el porcentaje de progreso del curso activo.
     */
    public int obtenerPorcentajeProgresoCursoActivo() {
        return obtenerPorcentajeProgresoCurso(cursoActivo);
    }

    /**
     * Asigna una estrategia al progreso del curso activo.
     */
    public void asignarEstrategiaCursoActivo(String nombreEstrategia) {
        if (progresoActual != null) {
            asignarEstrategiaProgreso(progresoActual, nombreEstrategia);
        }
    }

    /**
     * Inicia la sesión de ejercicio con el curso activo y una estrategia dada.
     */
    public void iniciarSesionEjercicioCursoActivo(String nombreEstrategia) {
        if (cursoActivo != null && progresoActual != null) {
            EstrategiaAprendizaje estrategia = crearEstrategia(nombreEstrategia);
            iniciarSesionEjercicio(cursoActivo, progresoActual, estrategia);
        }
    }

    /**
     * Continúa la sesión de ejercicio con el curso activo usando la estrategia guardada.
     */
    public void continuarSesionEjercicioCursoActivo() {
        if (cursoActivo != null) {
            Progreso progreso = buscarProgresoCurso(cursoActivo);
            if (progreso != null) {
                this.progresoActual = progreso;
                String nombreEstrategia = progreso.getEstrategia();
                EstrategiaAprendizaje estrategia = crearEstrategia(nombreEstrategia);
                iniciarSesionEjercicio(cursoActivo, progreso, estrategia);
            }
        }
    }

    // =====================================================
    // MÉTODOS ENCAPSULADOS PARA PREGUNTAS (MVC)
    // Las vistas no deben acceder directamente al modelo Pregunta
    // =====================================================

    /**
     * Obtiene el enunciado de la pregunta actual de la sesión.
     */
    public String obtenerEnunciadoPreguntaActual() {
        Pregunta pregunta = obtenerSiguientePreguntaSesion();
        return pregunta != null ? pregunta.getEnunciado() : "";
    }

    /**
     * Obtiene el enunciado del error actual en la sesión de repaso.
     */
    public String obtenerEnunciadoErrorActualRepaso() {
        ErrorFrecuente error = obtenerErrorActualRepaso();
        if (error != null && error.getPregunta() != null) {
            return error.getPregunta().getEnunciado();
        }
        return "";
    }

    /**
     * Obtiene el tipo de pregunta del error actual en la sesión de repaso.
     */
    public TipoPregunta obtenerTipoPreguntaErrorActualRepaso() {
        ErrorFrecuente error = obtenerErrorActualRepaso();
        if (error != null && error.getPregunta() != null) {
            return obtenerTipoPregunta(error.getPregunta());
        }
        return null;
    }

    /**
     * Obtiene las opciones de test del error actual en la sesión de repaso.
     */
    public List<String> obtenerOpcionesTestErrorActualRepaso() {
        ErrorFrecuente error = obtenerErrorActualRepaso();
        if (error != null && error.getPregunta() != null) {
            return obtenerOpcionesTest(error.getPregunta());
        }
        return List.of();
    }

    // =====================================================
    // CALLBACK PARA NAVEGACIÓN (MVC)
    // Evita referencias circulares entre vistas
    // =====================================================

    private Runnable callbackActualizarVentanaPrincipal;

    /**
     * Registra un callback para actualizar la ventana principal.
     * Esto evita que las vistas se referencien directamente entre sí.
     */
    public void setCallbackActualizarVentanaPrincipal(Runnable callback) {
        this.callbackActualizarVentanaPrincipal = callback;
    }

    /**
     * Ejecuta el callback para actualizar la ventana principal.
     */
    public void notificarActualizacionVentanaPrincipal() {
        if (callbackActualizarVentanaPrincipal != null) {
            callbackActualizarVentanaPrincipal.run();
        }
    }
}