package org.example;

import laboral1.ConexionBD;
import laboral1.Empleado;
import laboral1.Nomina;

import java.awt.*;
import java.io.*;
import java.sql.*;
import java.util.Arrays;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.

public class CalculaNominas {
    private void escribe(Empleado empl, Nomina sueldo) {
        empl.imprime();
        System.out.printf("=============================\nSueldo: %d \n\n", sueldo.sueldo(empl));
    }
    public void writeText(String ruta, String line){

        String[] empleadosReg  = readText(ruta).split(">");

        try(BufferedWriter br = new BufferedWriter(new FileWriter(ruta,true))){

            for(String emple : empleadosReg) {

                if(emple.length() == 0) continue;

                String empleaLimpo = emple.trim();

                int positionIni = empleaLimpo.indexOf("dni:")+4;
                int positionIniNew = line.indexOf("dni:") +4;

                String dniEmpleRegis = empleaLimpo.substring(positionIni, positionIni + 9);
                String dniNewEmpl = line.substring(positionIniNew, positionIniNew +9);

                if(dniNewEmpl.equals(dniEmpleRegis)) throw new IllegalArgumentException("Empleado ya Existe !!! " + line);
            }
            br.write(line + "\n");
        }catch(IOException e){
            System.out.println("Error:"+ e.getMessage());
        }catch (IllegalArgumentException e){
            System.out.println("Error: " + e.getMessage());
        }
    }

    public String readText(String ruta
    ) {
        String text = "";
        String linea = "";
        try(BufferedReader bf = new BufferedReader( new FileReader(ruta))){
            if(ruta == null) throw new IOException("error al leer el archivo");

            while(( linea = bf.readLine()) != null) text += linea + "\n";

        }catch (IOException e){
            System.out.printf("Erro ao ler texto: %s\n", e.getMessage());
        }
        return text.trim();
    }

    public static void main(String[] args) throws SQLException {
        Scanner sc = new Scanner(System.in);
        CalculaNominas c = new CalculaNominas();

       /* Empleado empl1 = new Empleado("James Cosling", "32000032G", 'm', 11, 7);
        Empleado empl2 = new Empleado("Ada Lovelace", "32000031R", 'F');
        Nomina sueldo = new Nomina();
        c.escribe(empl1, sueldo);
        c.escribe(empl2, sueldo);
        empl1.setCategoria(9);
        c.escribe(empl1, sueldo);
        c.escribe(empl2, sueldo);


        c.writeText("empleados.txt",">nombre:James casling, dni:32000032G,sexo:M,edad = 11 ");
        c.writeText("empleados.txt",">nombre:Ada Lovelace, dni:32000031R,sexo:F,edad = NULL");
        String t = c.readText("empleados.txt");
        System.out.println(t);*/



        // Conexion a la base de datos ===========================================
        ConexionBD cBD = new ConexionBD();


        System.out.print("""
                ===================== Menu =======================
                1 -> Empleados Registrados
                2 -> Añadir empleado
                3 -> Mostrar sueldo
                4 -> Editar empleado
                """);
        int opciones = sc.nextInt();
        switch (opciones){
            case 1:{
                System.out.println("Empleados Registrados: ==========================================");
                String sql = "select p.nombre,e.anyos,p.sexo, p.dni, e.categoria, e.id_empl from empleado e join persona p on e.id_empl = p.id_empleado";

                // Recorriendo Select ================================
                try(PreparedStatement stb = cBD.conexion().prepareStatement(sql);
                    ResultSet res = stb.executeQuery()){
                    while(res.next()){
                        int id = res.getInt("id_empl");
                        String nombre = res.getNString("nombre" );
                        String dni = res.getNString("dni");
                        int anyos = res.getInt("anyos");
                        String sexo = res.getString("sexo");
                        int categoria = res.getInt("categoria");
                        Empleado empl = new Empleado(nombre,dni,sexo.charAt(0),categoria,anyos);

                        System.out.printf("======================== Empleado %d ======================= \n",id);
                        empl.imprime();
                    }
                }catch (SQLException e){
                    System.out.println(e.getMessage());
                }
            }
            break;
            case 2:{
                System.out.println("============================== Registrar Nuevo Empleado: ==============================");
                sc.nextLine();
                System.out.println("Nombre:");
                String nombre = sc.nextLine();
                String dni = "";
                do{
                    System.out.println("Dni:");
                    dni = sc.nextLine();
                    if(dni.length() !=9)  System.out.println("Formato de Dni incorreto ( hay que ser 9 caracteres");

                }while (dni.length() !=9);
                System.out.println("Años:");
                int anyos = sc.nextInt();
                sc.nextLine();
                System.out.println("Sexo: M = Masculino / F = Femenino / X = No informa ");
                char sexo = sc.nextLine().charAt(0);

                int categoria = 0;
                do{
                    System.out.println("Categoria: 1 a 7 ");
                    categoria = sc.nextInt();
                    if(categoria <= 0 || categoria > 7) System.out.println("categoria invalida!!");

                }while(categoria < 1 || categoria > 7);

                String sqlPer = "insert INTO persona (nombre,dni,sexo) VALUES (?,?,?)";
                String sqlEmpl = "insert into empleado (categoria, anyos) values (?,?)";
                try {

                    cBD.conexion().setAutoCommit(false);

                    try (PreparedStatement stPer = cBD.conexion().prepareStatement(sqlPer)) {
                        stPer.setString(1, nombre);
                        stPer.setString(2, dni);
                        stPer.setString(3, String.valueOf(sexo));
                        stPer.executeUpdate();
                    }

                    try (PreparedStatement stEmpl = cBD.conexion().prepareStatement(sqlEmpl)) {
                        stEmpl.setInt(1, categoria);
                        stEmpl.setInt(2, anyos);
                        stEmpl.setString(3, dni);
                        stEmpl.executeUpdate();
                    }

                    cBD.conexion().commit();
                    System.out.println("¡Empleado registrado con éxito!");
                }catch (SQLException e) {

                    System.out.println("Detalle del error original: " + e.getMessage());
                }
            }
            break;
            case 3:{
                Nomina sueldo = new Nomina();
                System.out.println("======================= Mostrar sueldo de Empleado ====================================");
                System.out.println("Informe dni:");
                sc.nextLine();
                String dniInfo = sc.nextLine();
                String sql = "select p.nombre,e.anyos,p.sexo, p.dni, e.categoria, e.id_empl from empleado e join persona p on e.id_empl = p.id_empleado";

                // Recorriendo Select ================================
                try(PreparedStatement stb = cBD.conexion().prepareStatement(sql);
                    ResultSet res = stb.executeQuery()){
                    while(res.next()){
                        if(res.getString("dni").contains(dniInfo)){

                            int id = res.getInt("id_empl");
                            String nombre = res.getNString("nombre" );
                            String dni = res.getNString("dni");
                            int anyos = res.getInt("anyos");
                            String sexo = res.getString("sexo");
                            int categoria = res.getInt("categoria");
                            Empleado empl = new Empleado(nombre,dni,sexo.charAt(0),categoria,anyos);
                            empl.imprime();
                            System.out.println("Sueldo:" + sueldo.sueldo(empl));
                        }

                    }
                }catch (SQLException e){
                    System.out.println(e.getMessage());
                }
            }
            case 4:{
                System.out.printf("=========================== Modificar Empleado ============================\n");
                System.out.println("Informe el DNI del empleado a modificar:");
                sc.nextLine();
                String dniBuscar = sc.nextLine();

                System.out.println("NUEVO Nombre:");
                String nuevoNombre = sc.nextLine();

                System.out.println("NUEVO Sexo (M/F/X):");
                char nuevoSexo = sc.nextLine().charAt(0);

                System.out.println("NUEVOS Años de experiencia:");
                int nuevosAnyos = sc.nextInt();

                int nuevaCategoria = 0;

                do {
                    System.out.println("NUEVA Categoria (1 a 7):");
                    nuevaCategoria = sc.nextInt();
                    if (nuevaCategoria <= 0 || nuevaCategoria > 7) {
                        System.out.println("Categoría inválida!");
                    }
                } while (nuevaCategoria < 1 || nuevaCategoria > 7);

                String sqlUpdatePersona = "UPDATE persona SET nombre = ?, sexo = ? WHERE dni = ?";
                String sqlUpdateEmpleado = "UPDATE empleado " +
                        "INNER JOIN persona ON empleado.id_empl = persona.id_empleado " +
                        "SET empleado.categoria = ?, empleado.anyos = ? " +
                        "WHERE persona.dni = ?";


                try {
                    // Desactivar el autoCommit para iniciar la transacción
                    cBD.conexion().setAutoCommit(false);

                    // Actualizar Tabla Persona
                    try (PreparedStatement stPer = cBD.conexion().prepareStatement(sqlUpdatePersona)) {
                        stPer.setString(1, nuevoNombre);
                        stPer.setString(2, String.valueOf(nuevoSexo));
                        stPer.setString(3, dniBuscar);
                        stPer.executeUpdate();
                    }

                    // Actualizar Tabla Empleado
                    try (PreparedStatement stEmpl = cBD.conexion().prepareStatement(sqlUpdateEmpleado)) {
                        stEmpl.setInt(1, nuevaCategoria);
                        stEmpl.setInt(2, nuevosAnyos);
                        stEmpl.setString(3, dniBuscar);

                        // Validar si el DNI realmente existía en la base de datos
                        int filasAfectadas = stEmpl.executeUpdate();
                        if (filasAfectadas == 0) {
                            throw new SQLException("No se encontró ningún empleado con el DNI especificado.");
                        }
                    }

                    // Confirmar los cambios si ambos bloques se ejecutan correctamente
                    cBD.conexion().commit();

                    System.out.println("¡Empleado modificado con éxito!");

                } catch (SQLException e) {
                    System.out.printf(e.getMessage());
                }

            }
        }

    }
}
