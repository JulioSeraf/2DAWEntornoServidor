package laboral1;

public class DatosNoCorrectosException extends RuntimeException {
    /**
     * Error de datos errones
     * @param message
     */
    public DatosNoCorrectosException(String message) {
        super(message);
    }

}
