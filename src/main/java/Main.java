import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Objects;
import java.util.Properties;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        Servicio taskService = new Servicio();
        Connection conn = Conexion.getConnection();

        Servicio.crearTabla();

        Servicio.insertarDatos("irnvmov","tbnrievo",new Date(2007,12,11),"irevjesjvn");

        /*String query = "INSERT INTO tarea (id, nombre, descripcion, fecha, estado) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setInt(1, 5);
            pst.setString(2, "fecha");
            pst.setString(3, "golLocal");
            pst.setDate(4, new Date(2003,12,12));
            pst.setString(5, "EN_PROCESO");
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }*/


        int option;

        do {
            System.out.println("\nGestor de Tareas");
            System.out.println("1. Ver tareas");
            System.out.println("2. Añadir tarea");
            System.out.println("3. Modificar tarea");
            System.out.println("4. Eliminar tarea");
            System.out.println("5. Salir");
            System.out.print("Selecciona una opción: ");

            option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1:
                    taskService.verTareas();
                    break;
                case 2:
                    taskService.anadirTarea(scanner);
                    break;
                case 3:
                    taskService.modificarTarea(scanner);
                    break;
                case 4:
                    taskService.eliminarTarea(scanner);
                    break;
                case 5:
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Opción no válida. Intenta de nuevo.");
            }

        } while (option != 5);

        scanner.close();
    }
}
