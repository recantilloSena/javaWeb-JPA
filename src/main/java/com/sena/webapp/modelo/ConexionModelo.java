/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sena.webapp.modelo;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author RICARDO
 */
public class ConexionModelo {
    
    private static ConexionModelo conexion;
    private final EntityManagerFactory fabricaConexion;
    
    private ConexionModelo() {
        fabricaConexion = Persistence.createEntityManagerFactory("senaPU");
    }
    
    public static ConexionModelo getConexion () {
        
        if (conexion == null){
            conexion = new ConexionModelo ();
        }
        
       return conexion; 
    }
    
    public EntityManagerFactory getEntityManagerFactory(){
        return fabricaConexion;
    }
    
    public static String Ver (String value){
        return value;
    }
    
}