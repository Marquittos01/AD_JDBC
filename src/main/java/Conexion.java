import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;

public class Conexion {

    public static Properties properties = new Properties();
    static {
        try {
            InputStream input = Conexion.class.getClassLoader().getResourceAsStream("bbdd.properties");
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() throws IOException {
        Properties properties = new Properties();
        String IP, PORT, BBDD, USER, PWD;

        IP = "localhost";

        InputStream input = getClass().getClassLoader().getResourceAsStream("bbdd.properties");
        if (input == null) {
            System.out.println("No se pudo encontrar el archivo de propiedades");
            return null;
        } else {
            properties.load(input);
            PORT = (String) properties.get("PORT");
            BBDD = (String) properties.get("BBDD");
            USER = (String) properties.get("USER");
            PWD = (String) properties.get("PWD");

            Connection conn;
            try {
                String cadconex = "jdbc:mysql://" + IP + ":" + PORT + "/" + BBDD + " USER:" + USER + "PWD:" + PWD;
                System.out.println(cadconex);
                conn = DriverManager.getConnection("jdbc:mysql://" + IP + ":" + PORT + "/" + BBDD, USER, PWD);
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

    /*CREATE TABLE IF NOT EXISTS public."Tarea"
        (
                id integer NOT NULL DEFAULT nextval('"Tarea_id_seq"'::regclass),
                nombre text COLLATE pg_catalog."default" NOT NULL,
        descripcion text COLLATE pg_catalog."default",
                fecha date NOT NULL,
                estado text COLLATE pg_catalog."default" NOT NULL,
        CONSTRAINT "Tarea_pkey" PRIMARY KEY (id)
)

        TABLESPACE pg_default;

        ALTER TABLE IF EXISTS public."Tarea"
        OWNER to postgres;*/
}
