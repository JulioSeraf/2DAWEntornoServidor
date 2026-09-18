package org.example;

import laboral1.DatosNoCorrectosException;
import laboral1.Empleado;
import laboral1.Nomina;

import java.util.NavigableMap;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class CalculaNominas{
    private void escribe(Empleado empl, Nomina sueldo){
        empl.imprime();
        System.out.printf("""
                            =============================
                            Sueldo: %d \n
                            """,sueldo.sueldo(empl));
    }
    public static void main(String[] args) {

            CalculaNominas c = new CalculaNominas();
            Empleado empl1 = new Empleado("James Cosling","32000032G",'m',11,7);
            Empleado empl2 = new Empleado("Ada Lovelace","32000031R",'F');
            Nomina sueldo = new Nomina();
            c.escribe(empl1,sueldo);
            c.escribe(empl2,sueldo);
            empl1.setCategoria(9);
            c.escribe(empl1,sueldo);
            c.escribe(empl2,sueldo);




    }
}
