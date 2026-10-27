package laboral1;

import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
@WebServlet("/empleados")
public class EmpleadoServlet extends HttpServlet {
    private EmpleadoDAO empleadoDAO = new EmpleadoDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");
        if (accion == null) accion = "listar";

        switch (accion) {
            case "buscarSueldo":
                String dni = request.getParameter("dni");
                Empleado emp = empleadoDAO.obtenerPorDni(dni);
                if (emp != null) {
                    int sueldo = empleadoDAO.calcularSueldo(emp);
                    request.setAttribute("empleado", emp);
                    request.setAttribute("sueldo", sueldo);
                } else {
                    request.setAttribute("error", "No se encontró ningún empleado con el DNI proporcionado.");
                }
                request.getRequestDispatcher("sueldoEmpleado.jsp").forward(request, response);
                break;

            case "listar":
            default:
                List<Empleado> lista = empleadoDAO.obtenerTodos();
                request.setAttribute("listaEmpleados", lista);
                request.getRequestDispatcher("listaEmpleados.jsp").forward(request, response);
                break;
        }
    }
}