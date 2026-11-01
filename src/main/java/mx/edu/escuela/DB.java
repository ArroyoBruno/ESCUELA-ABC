package mx.edu.escuela;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DB {
    private DB() {}
    // Cambia usuario y contraseña según tu MySQL. En Azure se configurarán aparte.
    private static final String URL = System.getenv().getOrDefault("ESCUELA_DB_URL", "jdbc:mysql://localhost:3306/ESCUELA?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true");
    private static final String USER = System.getenv().getOrDefault("ESCUELA_DB_USER", "root");
    private static final String PASSWORD = System.getenv().getOrDefault("ESCUELA_DB_PASSWORD", "n0m3l0");
    static { try { Class.forName("com.mysql.cj.jdbc.Driver"); } catch (ClassNotFoundException e) { throw new ExceptionInInitializerError(e); } }
    public static Connection getConnection() throws SQLException { return DriverManager.getConnection(URL, USER, PASSWORD); }
}
