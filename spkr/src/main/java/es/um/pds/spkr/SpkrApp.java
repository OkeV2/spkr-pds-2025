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
     * Finaliza un ejercicio guardando el tiempo y las estadísticas.
     */
    public void finalizarEjercicio(Progreso progreso, int segundosTotales) {
        actualizarTiempoProgreso(progreso, segundosTotales);
        incrementarTiempoEstadisticas(segundosTotales);
        guardarProgreso();
    }

    // Getters

    public Usuario getUsuarioActual() {
        return usuarioActual;
    }
}