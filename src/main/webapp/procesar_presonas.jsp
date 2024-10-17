<%-- 
    Document   : procesar_presonas
    Created on : 16/10/2024, 7:44:10 p. m.
    Author     : RICARDO
--%>

<%@page import="com.sena.webapp.modelo.Personas"%>
<%@page import="com.sena.webapp.service.DataService"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        
         <% 
             String nombre = request.getParameter("nombre");
             Integer edad = Integer.parseInt(request.getParameter("edad")) ;
             Integer idDeporte = Integer.parseInt(request.getParameter("idDeporte"));
         
         
         
         %>
         
         <h1><%=nombre%></h1>
         <h1><%=edad%></h1>
         <h1><%=idDeporte%></h1>
         
         <%
           DataService dataservice = new DataService();
            Personas persona = new Personas();
            
            persona.setNombre(nombre);
            persona.setEdad(edad);
            persona.setIdDeporte(
             dataservice.deportesService().findById(idDeporte) 
            );
            
           
           dataservice.personasService().save(persona);
         
         
         %>
         
         <a href="tables_personas.jsp"> Ir a listado </a>
         
        
    </body>
</html>
