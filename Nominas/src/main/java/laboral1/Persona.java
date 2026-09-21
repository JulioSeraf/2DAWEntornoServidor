package laboral1;

public class Persona {
    public String nombre;
    public String dni;
    public char sexo;
    public Persona(String nombre, String dni, char sexo) {
        if(nombre.isEmpty()) throw new DatosNoCorrectosException("Datos no correctos, Nombre invalido");
        this.nombre = nombre;
        this.dni = dni;
        this.sexo = sexo;
    }

    public Persona(String nombre, char sexo) {
        this.nombre = nombre;
        this.sexo = sexo;

    }

    /**
     * Cambia valor de edad
     * @param edad
     */
    public void setEdad(int edad) {
        this.dni = dni;
    }

    /**
     * Imprime info de Persona(nombre y Dni)
     */
    public void Imprime() {
        System.out.printf("""
                Nombre: %s
                DNI: %s
                """,nombre,dni);
    }

}
