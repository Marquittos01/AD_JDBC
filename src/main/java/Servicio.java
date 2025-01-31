import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

public class Servicio {
    public Servicio(Connection conn){
        this.conn = conn;
    }
    private Connection conn;
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
        String query = "INSERT INTO tarea (id, nombre, descripcion, fecha, estado) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setInt(1, -1);
            pst.setString(2, name);
            pst.setString(3, description);
            pst.setDate(4, new java.sql.Date(date.getYear(), date.getMonth(), date.getDay()));
            pst.setString(5, estado.toString());
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

}
