package org.example;

import laboral1.Empleado;
import laboral1.Nomina;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class CalculaNominas {
    private void escribe(Empleado empl, Nomina sueldo) {
        empl.imprime();
        System.out.printf("=============================\nSueldo: %d \n\n", sueldo.sueldo(empl));
    }
    public String readText(String ruta
    ) {
        String text = "";
        try(BufferedReader bf = new BufferedReader( new FileReader(ruta))){
            text =  bf.readLine();
            if(text == null) throw new IOException("error al leer el archivo");

        }catch (IOException e){
            System.out.printf("Erro ao ler texto: %s\n", e.getMessage());
        }
        return text;
    }

    public static void main(String[] args) {
        CalculaNominas c = new CalculaNominas();
        String t = c.readText("empleados.txt");
        System.out.print(t);
        Empleado empl1 = new Empleado("James Cosling", "32000032G", 'm', 11, 7);
        Empleado empl2 = new Empleado("Ada Lovelace", "32000031R", 'F');
        Nomina sueldo = new Nomina();
        c.escribe(empl1, sueldo);
        c.escribe(empl2, sueldo);
        empl1.setCategoria(9);
        c.escribe(empl1, sueldo);
        c.escribe(empl2, sueldo);
    }
}
