package com.sena.webapp.service;

import com.sena.webapp.controlador.DeportesDao;
import com.sena.webapp.controlador.PersonasDao;
import com.sena.webapp.modelo.ConexionModelo;


public class DataService {
    
    ConexionModelo conexion = ConexionModelo.getConexion(); 
	
	
    //Declaración de Servicios de Acceso a Datos//
    private DeportesDao deportesService;
    private PersonasDao personasService;
     
    //Mapeo de Controllers//
    public DeportesDao deportesService(){
         deportesService = new  DeportesDao(conexion.getEntityManagerFactory());
	 return deportesService ;
	}
    
     public PersonasDao personasService(){
         personasService = new  PersonasDao(conexion.getEntityManagerFactory());
	 return personasService ;
	}
	
	
	
   }
