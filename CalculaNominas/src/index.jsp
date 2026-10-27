<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Gestión Laboral - Tomcat</title>
</head>
<body>
    <h2>Menú de Gestión de Nóminas</h2>

    <ul>
        <li><a href="empleados?accion=listar">Ver lista completa de empleados</a></li>
    </ul>

    <h3>Consultar Salario por DNI</h3>
    <form action="empleados" method="get">
        <input type="hidden" name="accion" value="buscarSueldo">
        <label for="dni">DNI:</label>
        <input type="text" id="dni" name="dni" required>
        <button type="submit">Calcular Sueldo</button>
    </form>
</body>
</html>