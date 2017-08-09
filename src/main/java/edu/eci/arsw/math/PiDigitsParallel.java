/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.math;

/**
 *
 * @author 2118677
 */
public class PiDigitsParallel extends Thread {

    private int start, count;
    private byte[] digits;
    
    public PiDigitsParallel(int start, int count){
        this.start = start;
        this.count = count;
    }
    
    @Override
    public void run(){
        digits = PiDigits.getDigits(start, count);
    }
    
    public byte[] getDigits(){
        return digits;
    }
}
