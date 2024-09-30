<%-- 
    Document   : index
    Created on : 29/09/2024, 7:56:43 p. m.
    Author     : RICARDO
--%>

<%@page import="com.sena.webapp.modelo.Deportes"%>
<%@page import="java.util.List"%>
<%@page import="com.sena.webapp.service.DataService"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Hello World!</h1>
        <%
         DataService dataService = new DataService();
        
        List<Deportes> lista =dataService.deportesService().findAll();       
           
         
         for (Deportes deporte : lista) {
          %>
             Esto es Java Web <%=deporte.getNombreDeporte() %> <br>
          <%
                 }
           %>
        
        
        
        
    </body>
</html>
