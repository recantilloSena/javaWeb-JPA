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
        <title>Lista de Deportes</title>
         <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    </head>
    <body>
        <div class="container">
            
        <h1>Listado de Deportes</h1>
        <div class="row">
        <div class="col-6">    
        
      <ul class="list-group">
        <%
         DataService dataService = new DataService();
        
        List<Deportes> lista =dataService.deportesService().findAll();       
           
         
         for (Deportes deporte : lista) {
          %>
           <li class="list-group-item"><%=deporte.getNombreDeporte().toUpperCase() %></li>
          <%
                 }
           %>
        
         </ul>  
        </div>
           <div class="col-6">
               
               <div class="dropdown">
                <button class="btn btn-warning dropdown-toggle" type="button" data-bs-toggle="dropdown" aria-expanded="false">
                  Deportes Disponibles
                </button>
                <ul class="dropdown-menu">
           <%         
                  
          for (Deportes deporte : lista) {
          %>
           <li><a class="dropdown-item" href="index.html"><%=deporte.getNombreDeporte().toUpperCase() %></a></li>
          <%
           }
           %>  
                    
                  
                 
                </ul>
              </div>
              
               
           </div>   
           
           
        </row>
        
        
        <a href="index.html" >Regresar</a>
           
        </div>  
       <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script> 
    </body>
</html>
