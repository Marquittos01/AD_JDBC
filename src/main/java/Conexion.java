import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Conexion {


    public static Connection getConnection() throws IOException {
        Properties properties = new Properties();
        //String URL, BBDD, USER, PWD;
        String URL, BBDD, USER, PWD, SUPABASE;

        InputStream input = Conexion.class.getClassLoader().getResourceAsStream("bbdd.properties");
        if (input == null) {
            System.out.println("No se pudo encontrar el archivo de propiedades");
            return null;
        } else {
            /*properties.load(input);
            URL = (String) properties.get("URL");
            BBDD = (String) properties.get("BBDD");
            USER = (String) properties.get("USER");
            PWD = (String) properties.get("PWD");

            Connection conn;
            try {
                String cadconex = URL + "/" + BBDD + " USER:" + USER + "PWD:" + PWD;
                System.out.println(cadconex);
                conn = DriverManager.getConnection(URL + "/" + BBDD, USER, PWD);
                return conn;
            } catch (SQLException e) {
                System.out.println("Error SQL: " + e.getMessage());
                return null;
            }*/
            properties.load(input);
            URL = (String) properties.get("URL");
            BBDD = (String) properties.get("BBDD");
            USER = (String) properties.get("USER");
            PWD = (String) properties.get("PWD");
            SUPABASE = (String) properties.get("SUPABASE");

            Connection conn;
            try {
                String cadconex = URL + "/" + BBDD + " USER:" + USER + "PWD:" + PWD;
                System.out.println(cadconex);
                conn = DriverManager.getConnection(SUPABASE);
                return conn;
            } catch (SQLException e) {
                System.out.println("Error SQL: " + e.getMessage());
                return null;
            }
        }
    }

    public static void closeConnection(Connection connection) throws SQLException {
        connection.close();
    }
}
