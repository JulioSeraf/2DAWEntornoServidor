package laboral1;
public class Empleado extends Persona {
    private int categoria;
    public int anyos;

    public Empleado(String nombre, String dni, char sexo) {
        super(nombre, dni, sexo);
        categoria = 1;
        anyos = 0;
    }
    public Empleado(String nombre, String dni, char sexo, int categoria, int anyos) throws DatosNoCorrectosException {
        super(nombre, dni, sexo);
       try {
           if(anyos < 0) throw new DatosNoCorrectosException("Datos no correctos, Años invalido!");
           this.categoria = categoria;
           this.anyos = anyos;
       }catch(DatosNoCorrectosException e){
           System.out.printf("error: %s\n",e.getMessage());
       }

    }
    public int getCategoria() {
        return categoria;
    }

    public void setCategoria(int categoria) throws  DatosNoCorrectosException {
        if (categoria < 0 || categoria > 10) throw new DatosNoCorrectosException("El categoria no es valido");
        this.categoria = categoria;
    }
    public void incAnyos(int anios) {
        this.anyos += anios;
    }
    public void imprime() {
        super.Imprime();
        System.out.printf("""
    Edad: %d
    Dni: %d
    Sexo: %S
   """,anyos,categoria,sexo);
    }
}
