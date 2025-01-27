import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;

public class Servicio {
    private ArrayList<Tarea> tasks = new ArrayList<>();
    private int idActual = 1;

    public void verTareas() {
        if (tasks.isEmpty()) {
            System.out.println("No hay tareas en la agenda.");
        } else {
            System.out.println("\nLista de Tareas:");
            for (Tarea task : tasks) {
                System.out.println(task);
            }
        }
    }

    public void anadirTarea(Scanner scanner) throws IOException {
        if (getConnection() == null) {
            System.out.println("No se ha conectado a la base de datos.");
            return;
        }
        System.out.print("Nombre de la tarea: ");
        String name = scanner.nextLine();
        System.out.print("Descripción de la tarea: ");
        String description = scanner.nextLine();
        System.out.print("Estado de la tarea (Pendiente/En proceso/Finalizada): ");
        String status = scanner.nextLine().toLowerCase();

        Estado estado;

        switch (status) {
            case "pendiente" -> estado = Estado.PENDIENTE;
            case "en proceso" -> estado = Estado.EN_PROCESO;
            case "finalizada" -> estado = Estado.FINALIZADA;
            default -> {
                System.out.println("Estado de tarea inválido. Se utiliza 'Pendiente' por defecto.");
                estado = Estado.PENDIENTE;
            }
        }

        Date date = new Date();
        tasks.add(new Tarea(idActual++, name, description, estado, date));
        System.out.println("Tarea añadida correctamente.");
    }


    public void modificarTarea(Scanner scanner) throws IOException {
        getConnection();

        verTareas();
        if (tasks.isEmpty()) return;

        System.out.print("Selecciona el ID de la tarea a modificar: ");
        int taskId = scanner.nextInt();
        scanner.nextLine();

        Tarea task = findTaskById(taskId);
        if (task == null) {
            System.out.println("ID de tarea no válido.");
            return;
        }

        System.out.println("Tarea seleccionada: " + task);
        System.out.print("Nuevo nombre (deja vacío para no cambiar): ");
        String newName = scanner.nextLine();
        if (!newName.isEmpty()) {
            task.setName(newName);
        }

        System.out.print("Nueva descripción (deja vacío para no cambiar): ");
        String newDescription = scanner.nextLine();
        if (!newDescription.isEmpty()) {
            task.setDescription(newDescription);
        }

        System.out.print("Nuevo estado (deja vacío para no cambiar) \n{Pendiente - En proceso - Finalizada}: ");
        String newStatus = scanner.nextLine().toLowerCase();

        Estado newEstado;
        if (!newStatus.isEmpty()) {
            switch (newStatus) {
                case "pendiente" -> newEstado = Estado.PENDIENTE;
                case "en proceso" -> newEstado = Estado.EN_PROCESO;
                case "finalizada" -> newEstado = Estado.FINALIZADA;
                default -> {
                    System.out.println("Estado de tarea inválido. Se utiliza 'Pendiente' por defecto.");
                    newEstado = Estado.PENDIENTE;
                }
            }

            task.setStatus(newEstado);
        }

        System.out.println("Tarea modificada correctamente.");
    }

    public void eliminarTarea(Scanner scanner) {
        verTareas();
        if (tasks.isEmpty()) return;

        System.out.print("Selecciona el ID de la tarea a eliminar: ");
        int taskId = scanner.nextInt();
        scanner.nextLine();

        Tarea task = findTaskById(taskId);
        if (task == null) {
            System.out.println("ID de tarea no válido.");
            return;
        }

        tasks.remove(task);
        System.out.println("Tarea eliminada correctamente.");
    }

    private Tarea findTaskById(int id) {
        for (Tarea task : tasks) {
            if (task.getId() == id) {
                return task;
            }
        }
        return null;
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
