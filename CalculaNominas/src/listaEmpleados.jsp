<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List, laboral1.Empleado" %>
<!DOCTYPE html>
<html>
<head>
    <title>Listado de Empleados</title>
</head>
<body>
    <h2>Lista de Empleados</h2>
    <table border="1" cellpadding="6">
        <thead>
            <tr>
                <th>DNI</th>
                <th>Nombre</th>
                <th>Sexo</th>
                <th>Categoría</th>
                <th>Años Trabajados</th>
                <th>Acción</th>
            </tr>
        </thead>
        <tbody>
            <%
                List<Empleado> empleados = (List<Empleado>) request.getAttribute("listaEmpleados");
                if (empleados != null && !empleados.isEmpty()) {
                    for (Empleado e : empleados) {
            %>
            <tr>
                <td><%= e.dni %></td>
                <td><%= e.nombre %></td>
                <td><%= e.sexo %></td>
                <td><%= e.getCategoria() %></td>
                <td><%= e.anyos %></td>
                <td>
                    <a href="empleados?accion=buscarSueldo&dni=<%= e.dni %>">Ver Sueldo</a>
                </td>
            </tr>
            <%
                    }
                } else {
            %>
            <tr><td colspan="6">No hay empleados registrados.</td></tr>
            <% } %>
        </tbody>
    </table>
    <br>
    <a href="index.jsp">Volver al inicio</a>
</body>
</html>