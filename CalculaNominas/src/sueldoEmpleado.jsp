<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="laboral1.Empleado" %>
<!DOCTYPE html>
<html>
<head>
    <title>Detalle de Sueldo</title>
</head>
<body>
    <h2>Cálculo de Nómina</h2>

    <%
        String error = (String) request.getAttribute("error");
        if (error != null) {
    %>
        <p style="color: red;"><%= error %></p>
    <%
        } else {
            Empleado emp = (Empleado) request.getAttribute("empleado");
            Integer sueldo = (Integer) request.getAttribute("sueldo");
    %>
        <p><strong>Nombre:</strong> <%= emp.nombre %></p>
        <p><strong>DNI:</strong> <%= emp.dni %></p>
        <p><strong>Categoría:</strong> <%= emp.getCategoria() %></p>
        <p><strong>Años de servicio:</strong> <%= emp.anyos %></p>
        <h3><strong>Sueldo Total:</strong> <%= sueldo %> €</h3>
    <%
        }
    %>
    <br>
    <a href="index.jsp">Volver al inicio</a>
</body>
</html>