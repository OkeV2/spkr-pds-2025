package es.um.pds.spkr.persistencia;

import es.um.pds.spkr.cargador.CargadorCursosJSON;
import es.um.pds.spkr.catalogo.CatalogoUsuarios;
import es.um.pds.spkr.modelo.Biblioteca;
import es.um.pds.spkr.modelo.Curso;
import es.um.pds.spkr.modelo.Estadisticas;
import es.um.pds.spkr.modelo.Usuario;

public class InicializadorDatos {

    private static final String USUARIO_DEMO = "demo";
    private static final String EMAIL_DEMO = "demo@spkr.com";
    private static final String PASSWORD_DEMO = "demo123";

    private static final String[] CURSOS_INICIALES = {
        "cursos/curso_ingles_extenso.json",
        "cursos/curso_ingles_intermedio.json"
    };

    public static void inicializar(CatalogoUsuarios catalogoUsuarios) {
        if (!catalogoUsuarios.existeUsuario(USUARIO_DEMO)) {
            crearUsuarioDemo(catalogoUsuarios);
            System.out.println("Usuario de demostración creado: " + USUARIO_DEMO + " / " + PASSWORD_DEMO);
        }
    }

    private static void crearUsuarioDemo(CatalogoUsuarios catalogoUsuarios) {
        Usuario usuarioDemo = new Usuario(USUARIO_DEMO, EMAIL_DEMO, PASSWORD_DEMO);
        usuarioDemo.setEstadisticas(new Estadisticas());
        usuarioDemo.setBiblioteca(new Biblioteca());

        cargarCursosIniciales(usuarioDemo.getBiblioteca());

        catalogoUsuarios.addUsuario(usuarioDemo);
    }

    private static void cargarCursosIniciales(Biblioteca biblioteca) {
        CargadorCursosJSON cargador = new CargadorCursosJSON();

        for (String recurso : CURSOS_INICIALES) {
            try {
                Curso curso = cargador.cargarCursoDesdeRecurso(recurso);
                biblioteca.addCurso(curso);
                System.out.println("Curso cargado: " + curso.getTitulo());
            } catch (Exception e) {
                System.err.println("Error al cargar curso " + recurso + ": " + e.getMessage());
            }
        }
    }
}
