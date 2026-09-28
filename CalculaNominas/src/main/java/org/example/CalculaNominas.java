package org.example;

import laboral1.ConexionBD;
import laboral1.Empleado;
import laboral1.Nomina;

import java.awt.*;
import java.io.*;
import java.sql.Array;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class CalculaNominas {
    private void escribe(Empleado empl, Nomina sueldo) {
        empl.imprime();
        System.out.printf("=============================\nSueldo: %d \n\n", sueldo.sueldo(empl));
    }
    public void writeText(String ruta, String line){
        StringBuilder text = new StringBuilder();
        try(BufferedWriter br = new BufferedWriter(new FileWriter(ruta,true))){
            br.write(line);
        }catch(IOException e){
            System.out.println("Error:"+ e.getMessage());
        }
    }
    public StringBuilder readText(String ruta
    ) {
        StringBuilder text = new StringBuilder();
        String linea = "";
        try(BufferedReader bf = new BufferedReader( new FileReader(ruta))){
            if(text == null) throw new IOException("error al leer el archivo");

            while(( linea = bf.readLine()) != null) text.append(linea).append("\n");


        }catch (IOException e){
            System.out.printf("Erro ao ler texto: %s\n", e.getMessage());
        }
        return text;
    }

    public static void main(String[] args) {
        CalculaNominas c = new CalculaNominas();
        ConexionBD cBD = new ConexionBD();
        c.writeText("empleados.txt",">nombre:James casling, dni:32000032G,sexo:M,edad = 11 ");
        c.writeText("empleados.txt",">nombre:Ada Lovelace, dni:32000031R,sexo:F,edad = NULL");

        StringBuilder t = c.readText("empleados.txt");
        String[] emplText = t.toString().split(">");
        String[] empleados = new String[emplText.length];
        for(String empl : emplText){
            System.out.print(empl);
        }

        Empleado empl1 = new Empleado("James Cosling", "32000032G", 'm', 11, 7);
        Empleado empl2 = new Empleado("Ada Lovelace", "32000031R", 'F');
        Nomina sueldo = new Nomina();
        c.escribe(empl1, sueldo);
        c.escribe(empl2, sueldo);
        empl1.setCategoria(9);
        c.escribe(empl1, sueldo);
        c.escribe(empl2, sueldo);
        String sql = "select p.nombre,e.anyos,p.sexo, p.dni, e.categoria from empleado e join persona p on e.id_empl = p.id_empleado;";

        try(PreparedStatement stb = cBD.conexion().prepareStatement(sql);
            ResultSet res = stb.executeQuery()){
            while(res.next()){
                String nombre = res.getNString("nombre");
                String dni = res.getNString("dni");
                int anyos = res.getInt("anyos");
                String sexo = res.getString("sexo");
                int categoria = res.getInt("categoria");

                System.out.printf("Nombre: %s - años: %d - Dni: %s - Sexo: %s - Categoria: %s ", nombre,anyos,dni,sexo,categoria);
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }


    }
}
