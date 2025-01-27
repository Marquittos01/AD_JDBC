import java.util.Scanner;

public class AdministradorTareas {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Servicio taskService = new Servicio();
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
