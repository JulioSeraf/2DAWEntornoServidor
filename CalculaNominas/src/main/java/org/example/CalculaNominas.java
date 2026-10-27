package org.example;

import laboral1.DatosNoCorrectosException;
import laboral1.Empleado;
import laboral1.EmpleadoDAO;
import laboral1.Nomina;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;

/**
 * Clase principal que gestiona el control de nóminas y empleados.
 * Utiliza como almacenamiento principal la Base de Datos MySQL y como
 * respaldo secundario ficheros de texto plano.
 *
 * @author Sistema Laboral
 * @version 2.1
 */
public class CalculaNominas {

    private static final EmpleadoDAO empleadoDAO = new EmpleadoDAO();
    private static final Nomina nomina = new Nomina();
    private static final Scanner scanner = new Scanner(System.in);

    /**
     * Punto de entrada de la aplicación.
     * Gestiona el bucle interactivo del menú principal.
     *
     * @param args Argumentos de la línea de comandos.
     */
    public static void main(String[] args) {
        int opcion = -1;
        do {
            mostrarMenuPrincipal();
            try {
                System.out.print("Seleccione una opción: ");
                opcion = Integer.parseInt(scanner.nextLine().trim());
                switch (opcion) {
                    case 1 -> listarEmpleadosBD();
                    case 2 -> consultarSueldoPorDni();
                    case 3 -> menuModificarEmpleado();
                    case 4 -> recalcularSueldoEmpleado();
                    case 5 -> recalcularTodosLosSueldos();
                    case 6 -> realizarCopiaSeguridad();
                    case 7 -> altaEmpleadoIndividualConsola();
                    case 8 -> altaEmpleadoLotesConsola();
                    case 0 -> System.out.println("Saliendo de la aplicación...");
                    default -> System.out.println("Opción no válida. Intente de nuevo.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Debe introducir un número entero válido.");
            } catch (Exception e) {
                System.out.println("Ocurrió un error inesperado: " + e.getMessage());
            }
            System.out.println();
        } while (opcion != 0);
    }

    /**
     * Despliega en consola las opciones del menú principal.
     */
    private static void mostrarMenuPrincipal() {
        System.out.println("==================================================");
        System.out.println("       SISTEMA DE GESTIÓN DE NÓMINAS (BD)         ");
        System.out.println("==================================================");
        System.out.println("1. Mostrar todos los empleados (BD)");
        System.out.println("2. Consultar salario de un empleado por DNI");
        System.out.println("3. Modificar datos de un empleado");
        System.out.println("4. Recalcular y actualizar sueldo de un empleado");
        System.out.println("5. Recalcular y actualizar sueldos de todos");
        System.out.println("6. Realizar copia de seguridad en fichero de texto");
        System.out.println("7. Alta de empleado individual");
        System.out.println("8. Alta de empleados por lotes (empleadosNuevos.txt)");
        System.out.println("0. Salir");
        System.out.println("==================================================");
    }

    /**
     * Consulta y muestra los datos de todos los empleados presentes en la BD.
     */
    private static void listarEmpleadosBD() {
        List<Empleado> empleados = empleadoDAO.obtenerTodos();
        if (empleados.isEmpty()) {
            System.out.println("No hay empleados registrados en la base de datos.");
            return;
        }
        System.out.println("\n--- LISTADO DE EMPLEADOS ---");
        for (Empleado emp : empleados) {
            System.out.printf("DNI: %-10s | Nombre: %-18s | Sexo: %c | Cat: %2d | Años: %2d%n",
                    emp.dni, emp.nombre, emp.sexo, emp.getCategoria(), emp.anyos);
        }
    }

    /**
     * Pide el DNI de un empleado y consulta su sueldo en la tabla nominas.
     */
    private static void consultarSueldoPorDni() {
        System.out.print("Ingrese el DNI del empleado: ");
        String dni = scanner.nextLine().trim();
        Integer sueldo = empleadoDAO.obtenerSueldo(dni);
        if (sueldo != null) {
            System.out.printf("El sueldo del empleado con DNI %s es: %d €%n", dni, sueldo);
        } else {
            System.out.println("No se encontró nómina registrada para el DNI indicado.");
        }
    }

    /**
     * Submenú para modificar los atributos de un empleado.
     * Si se altera la categoría o los años de servicio, se recalcula y
     * persiste el salario de forma automática en la BD.
     */
    private static void menuModificarEmpleado() {
        System.out.print("Ingrese el DNI del empleado a modificar: ");
        String dni = scanner.nextLine().trim();
        Empleado emp = empleadoDAO.obtenerPorDni(dni);

        if (emp == null) {
            System.out.println("Empleado no encontrado.");
            return;
        }

        System.out.println("\nModificando a: " + emp.nombre + " (" + emp.dni + ")");
        System.out.println("1. Modificar Nombre");
        System.out.println("2. Modificar Sexo");
        System.out.println("3. Modificar Categoría (recalcula sueldo)");
        System.out.println("4. Modificar Años trabajados (recalcula sueldo)");
        System.out.print("Opción: ");

        try {
            int subOpcion = Integer.parseInt(scanner.nextLine().trim());
            boolean recalcular = false;

            switch (subOpcion) {
                case 1 -> {
                    System.out.print("Nuevo nombre: ");
                    String nuevoNombre = scanner.nextLine().trim();
                    if (nuevoNombre.isEmpty()) throw new DatosNoCorrectosException("El nombre no puede estar vacío.");
                    emp.nombre = nuevoNombre;
                }
                case 2 -> {
                    System.out.print("Nuevo sexo (M/F): ");
                    char nuevoSexo = scanner.nextLine().trim().toUpperCase().charAt(0);
                    if (nuevoSexo != 'M' && nuevoSexo != 'F') throw new DatosNoCorrectosException("Sexo inválido.");
                    emp.sexo = nuevoSexo;
                }
                case 3 -> {
                    System.out.print("Nueva categoría (1-10): ");
                    int nuevaCat = Integer.parseInt(scanner.nextLine().trim());
                    emp.setCategoria(nuevaCat);
                    recalcular = true;
                }
                case 4 -> {
                    System.out.print("Nuevos años trabajados: ");
                    int nuevosAnyos = Integer.parseInt(scanner.nextLine().trim());
                    if (nuevosAnyos < 0) throw new DatosNoCorrectosException("Los años no pueden ser negativos.");
                    emp.anyos = nuevosAnyos;
                    recalcular = true;
                }
                default -> {
                    System.out.println("Opción no válida.");
                    return;
                }
            }

            if (empleadoDAO.actualizarEmpleado(emp)) {
                System.out.println("Datos del empleado actualizados en la BD.");
                if (recalcular) {
                    int nuevoSueldo = nomina.sueldo(emp);
                    empleadoDAO.guardarOActualizarNomina(emp.dni, nuevoSueldo);
                    System.out.printf("Sueldo recalculado y actualizado automáticamente a: %d €%n", nuevoSueldo);
                }
            }
        } catch (DatosNoCorrectosException e) {
            System.out.println("Error de validación: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Error: Valor numérico incorrecto.");
        }
    }

    /**
     * Recalcula y actualiza en la base de datos el sueldo del empleado indicado.
     */
    private static void recalcularSueldoEmpleado() {
        System.out.print("Ingrese el DNI del empleado: ");
        String dni = scanner.nextLine().trim();
        Empleado emp = empleadoDAO.obtenerPorDni(dni);
        if (emp != null) {
            int nuevoSueldo = nomina.sueldo(emp);
            empleadoDAO.guardarOActualizarNomina(dni, nuevoSueldo);
            System.out.printf("Sueldo actualizado exitosamente para %s: %d €%n", emp.nombre, nuevoSueldo);
        } else {
            System.out.println("Empleado no encontrado.");
        }
    }

    /**
     * Recalcula y actualiza el sueldo de todos los empleados en la base de datos.
     */
    private static void recalcularTodosLosSueldos() {
        List<Empleado> empleados = empleadoDAO.obtenerTodos();
        if (empleados.isEmpty()) {
            System.out.println("No hay empleados para procesar.");
            return;
        }
        for (Empleado emp : empleados) {
            int nuevoSueldo = nomina.sueldo(emp);
            empleadoDAO.guardarOActualizarNomina(emp.dni, nuevoSueldo);
        }
        System.out.println("Se han recalculado y guardado los sueldos de " + empleados.size() + " empleados.");
    }

    /**
     * Da de alta a un empleado en la BD y guarda automáticamente su salario calculado.
     *
     * @param emp Objeto Empleado a insertar.
     * @return true si se realizó el registro correctamente; false en caso contrario.
     */
    public static boolean altaEmpleado(Empleado emp) {
        if (emp == null) return false;
        boolean exito = empleadoDAO.insertarEmpleado(emp);
        if (exito) {
            int sueldo = nomina.sueldo(emp);
            empleadoDAO.guardarOActualizarNomina(emp.dni, sueldo);
        }
        return exito;
    }

    /**
     * Sobrecarga de altaEmpleado para dar de alta registros masivos desde un fichero de texto.
     *
     * @param rutaFichero Ruta del archivo que contiene los registros a importar.
     * @return Número de empleados añadidos satisfactoriamente.
     */
    public static int altaEmpleado(String rutaFichero) {
        int altas = 0;
        File f = new File(rutaFichero);
        if (!f.exists()) {
            System.out.println("El fichero " + rutaFichero + " no fue encontrado.");
            return 0;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.isBlank()) continue;
                String[] datos = linea.split(",");
                if (datos.length >= 5) {
                    String nombre = datos[0].trim();
                    String dni = datos[1].trim();
                    char sexo = datos[2].trim().toUpperCase().charAt(0);
                    int categoria = Integer.parseInt(datos[3].trim());
                    int anyos = Integer.parseInt(datos[4].trim());

                    Empleado emp = new Empleado(nombre, dni, sexo, categoria, anyos);
                    if (altaEmpleado(emp)) altas++;
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error procesando fichero por lotes: " + e.getMessage());
        }
        return altas;
    }

    /**
     * Captura datos por consola para el alta de un único empleado.
     */
    private static void altaEmpleadoIndividualConsola() {
        try {
            System.out.print("Nombre: ");
            String nombre = scanner.nextLine().trim();
            System.out.print("DNI: ");
            String dni = scanner.nextLine().trim();
            System.out.print("Sexo (M/F): ");
            char sexo = scanner.nextLine().trim().toUpperCase().charAt(0);
            System.out.print("Categoría (1-10): ");
            int cat = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Años trabajados: ");
            int anyos = Integer.parseInt(scanner.nextLine().trim());

            Empleado emp = new Empleado(nombre, dni, sexo, cat, anyos);
            if (altaEmpleado(emp)) {
                System.out.println("Empleado dado de alta y nómina generada correctamente.");
            } else {
                System.out.println("No fue posible dar de alta al empleado.");
            }
        } catch (DatosNoCorrectosException e) {
            System.out.println("Error de validación: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Error: Formato numérico incorrecto.");
        }
    }

    /**
     * Ejecuta el alta por lotes a partir de empleadosNuevos.txt.
     */
    private static void altaEmpleadoLotesConsola() {
        int total = altaEmpleado("empleadosNuevos.txt");
        System.out.println("Altas procesadas desde fichero: " + total);
    }

    /**
     * Exporta los datos de los empleados desde la base de datos
     * al fichero de texto plano "empleados.txt" a modo de copia de seguridad.
     */
    public static void realizarCopiaSeguridad() {
        List<Empleado> lista = empleadoDAO.obtenerTodos();
        if (lista.isEmpty()) {
            System.out.println("No hay empleados en la BD para respaldar.");
            return;
        }

        try (PrintWriter pw = new PrintWriter(new FileWriter("empleados.txt"))) {
            for (Empleado e : lista) {
                pw.printf("%s,%s,%c,%d,%d%n", e.nombre, e.dni, e.sexo, e.getCategoria(), e.anyos);
            }
            System.out.println("Copia de seguridad realizada con éxito en 'empleados.txt'.");
        } catch (IOException e) {
            System.err.println("Error al escribir el archivo empleados.txt: " + e.getMessage());
        }
    }
}