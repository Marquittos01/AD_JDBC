import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.*;
import java.util.Date;

public class Servicio {
    private static ArrayList<Tarea> tasks = new ArrayList<>();
    private static int idActual = 1;

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

        /*if (getConnection() == null) {
            System.out.println("No se ha conectado a la base de datos.");
            return;
        }*/
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

        // Insertar en la base de datos
        String query = "INSERT INTO tarea (nombre, descripcion, fecha, estado) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pst = Conexion.getConnection().prepareStatement(query)) {
            pst.setString(1, name);
            pst.setString(2, description);
            pst.setDate(3, new java.sql.Date(date.getYear(), date.getMonth(), date.getDay()));
            pst.setString(4, estado.toString());
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void modificarTarea(Scanner scanner) throws IOException {


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
            System.out.println("La tarea no existe.");
            return;
        }

        tasks.remove(task);

        String query = "DELETE FROM tarea WHERE id = " + taskId;
        try (PreparedStatement pst = Conexion.getConnection().prepareStatement(query)) {
            pst.executeUpdate();
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }

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

    public static void crearTabla() {
        try {
            String createTableQuery = """
                CREATE TABLE IF NOT EXISTS public."tarea" (
                    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                    nombre TEXT NOT NULL,
                    descripcion TEXT,
                    fecha DATE NOT NULL,
                    estado TEXT NOT NULL
                );
                """;

            try (Statement stmt = Conexion.getConnection().createStatement()) {
                stmt.execute(createTableQuery);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void cargarTareas() {
        String query = "SELECT * FROM tarea";
        try (PreparedStatement pst = Conexion.getConnection().prepareStatement(query)) {
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                String descripcion = rs.getString("descripcion");
                Date fecha = rs.getDate("fecha");
                Estado estado = Estado.valueOf(rs.getString("estado"));
                tasks.add(new Tarea(id, nombre, descripcion, estado, fecha));
            }
            idActual = tasks.get(tasks.size() - 1).getId();
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void insertarDatos(String nombre, String descripcion,Date fecha, String estado){
        String query = "INSERT INTO tarea (nombre, descripcion, fecha, estado) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pst = Conexion.getConnection().prepareStatement(query)) {
            pst.setString(1, "fecha");
            pst.setString(2, "golLocal");
            pst.setDate(3, new java.sql.Date(2003,12,12));
            pst.setString(4, "EN_PROCESO");
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}