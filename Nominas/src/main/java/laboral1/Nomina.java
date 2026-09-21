package laboral1;


public class Nomina {
    public Nomina() {}
    private static final int SUELDO_BASE[] = {50000, 70000, 90000, 110000, 130000,
                    150000, 170000, 190000, 210000, 230000};

    /**
     * Suma sueldo base
     * @param empleado
     * @return SUELDO BASE sumando 5000 multiplicado por years.
     */
    public int sueldo(Empleado empleado) {
            return SUELDO_BASE[empleado.getCategoria()] + 5000 * empleado.anyos;
    }
}
