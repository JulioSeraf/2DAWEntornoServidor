import laboral1.ConexionBD;
import laboral1.Empleado;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

// Insertar o actualizar nómina
public void guardarOActualizarNomina(String dni, int sueldo) {
    String sql = "INSERT INTO nominas (dni, sueldo) VALUES (?, ?) " +
            "ON DUPLICATE KEY UPDATE sueldo = VALUES(sueldo)";
    try (Connection con = new ConexionBD.conexion();
         PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setString(1, dni);
        ps.setInt(2, sueldo);
        ps.executeUpdate();
    } catch (Exception e) {
        System.err.println("Error al guardar nómina: " + e.getMessage());
    }
}

// Obtener sueldo desde la tabla nominas
public Integer obtenerSueldo(String dni) {
    String sql = "SELECT sueldo FROM nominas WHERE dni = ?";
    try (Connection con = ConexionBD.conexion();
         PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setString(1, dni);
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt("sueldo");
        }
    } catch (Exception e) {
        System.err.println("Error al obtener sueldo: " + e.getMessage());
    }
    return null;
}

// Insertar empleado
public boolean insertarEmpleado(Empleado emp) {
    String sql = "INSERT INTO empleados (dni, nombre, sexo, categoria, anyos) VALUES (?, ?, ?, ?, ?) " +
            "ON DUPLICATE KEY UPDATE nombre = VALUES(nombre), sexo = VALUES(sexo), " +
            "categoria = VALUES(categoria), anyos = VALUES(anyos)";
    try (Connection con = conexionBD.conexion();
         PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setString(1, emp.dni);
        ps.setString(2, emp.nombre);
        ps.setString(3, String.valueOf(emp.sexo));
        ps.setInt(4, emp.getCategoria());
        ps.setInt(5, emp.anyos);
        return ps.executeUpdate() > 0;
    } catch (Exception e) {
        System.err.println("Error al insertar empleado: " + e.getMessage());
        return false;
    }
}

// Actualizar datos del empleado
public boolean actualizarEmpleado(Empleado emp) {
    String sql = "UPDATE empleados SET nombre = ?, sexo = ?, categoria = ?, anyos = ? WHERE dni = ?";
    try (Connection con = conexionBD.conexion();
         PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setString(1, emp.nombre);
        ps.setString(2, String.valueOf(emp.sexo));
        ps.setInt(3, emp.getCategoria());
        ps.setInt(4, emp.anyos);
        ps.setString(5, emp.dni);
        return ps.executeUpdate() > 0;
    } catch (Exception e) {
        System.err.println("Error al actualizar empleado: " + e.getMessage());
        return false;
    }
}