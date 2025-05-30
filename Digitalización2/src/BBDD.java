import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class BBDD {

    private static final String URL = "jdbc:mysql://192.168.4.148/";
    private static final String BD = "alquiler_coches";
    private static final String USUARIO = "root";
    private static final String CONTRASEÑA = "bd2024";

    // Método reutilizable para obtener conexión
    public static Connection getConexion() throws SQLException {
        return DriverManager.getConnection(URL + BD, USUARIO, CONTRASEÑA);
    }
}
