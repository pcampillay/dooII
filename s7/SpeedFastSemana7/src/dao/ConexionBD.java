package dao;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class ConexionBD {
    private static final Path ARCHIVO_CONFIGURACION = Path.of("config.properties");

    private ConexionBD() {
    }

    public static Connection conectar() throws SQLException {
        Properties configuracion = cargarConfiguracion();
        String url = obtenerDato(configuracion, "db.url");
        String usuario = obtenerDato(configuracion, "db.user");
        String clave = obtenerDato(configuracion, "db.password");

        return DriverManager.getConnection(url, usuario, clave);
    }

    private static Properties cargarConfiguracion() throws SQLException {
        Properties configuracion = new Properties();

        try (InputStream archivo = Files.newInputStream(ARCHIVO_CONFIGURACION)) {
            configuracion.load(archivo);
            return configuracion;
        } catch (IOException exception) {
            throw new SQLException("No fue posible leer config.properties.", exception);
        }
    }

    private static String obtenerDato(Properties configuracion, String nombre) throws SQLException {
        String valor = configuracion.getProperty(nombre);
        if (valor == null || valor.isBlank()) {
            throw new SQLException("Falta configurar " + nombre + " en config.properties.");
        }

        return valor;
    }
}
