package laboral1;
import java.sql.*;
public class ConexionBD {
    String ruta = "jdbc:mysql://localhost:3306/CalculaNominas";
    String user = "root";
    String password = "";

    public Connection conexion(){
        Connection reConection = null;
        try{
            reConection = DriverManager.getConnection(this.ruta,this.user,this.password);
            if(reConection != null){
                System.out.println("conexion exitosa!");
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return reConection;
    }

}
