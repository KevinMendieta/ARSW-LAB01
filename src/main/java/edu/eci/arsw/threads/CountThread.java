/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.threads;

import edu.eci.arsw.math.Main;
import edu.eci.arsw.math.PiDigits;

/**
 *
 * @author hcadavid
 */
public class CountThread extends Thread{
    
    private int start, count;
    
    public CountThread(int start, int count){
        this.start = start;
        this.count = count;
    }
    
    @Override
    public void run(){
       for (int i = start; i <= count ; i++){
           System.out.println(i);
       }
    }
}
