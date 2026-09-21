package laboral1;
public class Empleado extends Persona {
    private int categoria;
    public int anyos;
    public Empleado(String nombre, String dni, char sexo) {
        super(nombre, dni, sexo);
        categoria = 1;
        anyos = 0;
    }
    public Empleado(String nombre, String dni, char sexo, int categoria, int anyos) {
        super(nombre, dni, sexo);
       try {
           if(anyos < 0) throw new DatosNoCorrectosException("Datos no correctos, Años invalido!");
           if(categoria < 0 || categoria > 10) throw new DatosNoCorrectosException("Categoria incorrecto!");
           this.categoria = categoria;
           this.anyos = anyos;
       }catch(DatosNoCorrectosException e){
           System.out.printf("error: %s\n",e.getMessage());
       }

    }

    /**
     *
     * @return Categoria
     */
    public int getCategoria() {
        return categoria;
    }

    /**
     * Cambia valor de categoria o salta error si no es valida
     * @param categoria
     * @throws DatosNoCorrectosException
     */
    public void setCategoria(int categoria) throws  DatosNoCorrectosException {
        if (categoria < 0 || categoria > 10) throw new DatosNoCorrectosException("El categoria no es valido");
        this.categoria = categoria;
    }

    /**
     *  incrementa anios
     * @param anios
     */
    public void incAnyos(int anios) {
        this.anyos += anios;
    }

    /**
     * imprime datos de empleados
     */
    public void imprime() {
        super.Imprime();
        System.out.printf("""
    Edad: %d
    Dni: %d
    Sexo: %S
   """,anyos,categoria,sexo);
    }
}
