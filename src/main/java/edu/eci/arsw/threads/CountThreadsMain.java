/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.threads;

/**
 *
 * @author hcadavid
 */
public class CountThreadsMain{
    
    public static void main(String a[]){
    	
    	Thread primero = new CountThread(0,20);
    	Thread primerIntervalo = new CountThread(0,99);
    	Thread segundoIntervalo = new CountThread(100,199);
    	Thread tercerIntervalo = new CountThread(200,299);
    	
    	//Parte 1.1
    	//primero.start();
    	
    	//Parte 1.2
    	//primerIntervalo.start();
    	//segundoIntervalo.start();
    	//tercerIntervalo.start();
    	
    	//Parte 1.2.4
    	primerIntervalo.run();
    	segundoIntervalo.run();
    	tercerIntervalo.run();
    }
    
}
