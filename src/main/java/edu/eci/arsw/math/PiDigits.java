package edu.eci.arsw.math;

///  <summary>

import java.util.ArrayList;

///  An implementation of the Bailey-Borwein-Plouffe formula for calculating hexadecimal
///  digits of pi.
///  https://en.wikipedia.org/wiki/Bailey%E2%80%93Borwein%E2%80%93Plouffe_formula
///  *** Translated from C# code: https://github.com/mmoroney/DigitsOfPi ***
///  </summary>
public class PiDigits {

    private static int DigitsPerSum = 8;
    private static double Epsilon = 1e-17;

    /**
     * Returns a range of hexadecimal digits of pi using parallelism.
     * @param start The starting location of the range.
     * @param count The number of digits to return
     * @param n The number of threads that would be used to get the digits.
     * @return An array containing the hexadecimal digits.
     */
    public static byte[] getDigits(int start, int count, int n){
        ArrayList<PiDigitsParallel> threads = new ArrayList<PiDigitsParallel>();
        byte[] digits = null;
        int delta = (start + count) / n;
        int residue = (start + count) % n;
        PiDigitsParallel newThread = null;
        for(int i = 0; i < n; i++){
            if(i + 1 < n){
                newThread = new PiDigitsParallel(start + (i * delta), (delta * (i + 1)) - 1);
            }else{
                newThread = new PiDigitsParallel(start + (i * delta), delta * (i + 1) + residue);
            }
            newThread.start();
        }
        for(PiDigitsParallel thread: threads){
            try{
                thread.join();
            }catch(Exception e){
                e.printStackTrace();
            }            
        }
        for(int i = 0; i < threads.size(); i++){
            digits = concatenate(digits, threads.get(i).getDigits());
        }        
        return digits;
    }
    
    /**
     * Concatenate two arrays.
     * @param first The first array.
     * @param second The second array.
     * @return An array that contains the elements of first and second.
     */
    public static byte[] concatenate(byte[] first, byte[] second){
        int newSize = first.length + second.length;
        byte[] newArray = new byte[newSize];
        int index = 0;
        for(byte number: first){
            newArray[index] = number;
            index++;
        }
        for(byte number: second){
            newArray[index] = number;
            index++;
        }        
        return newArray;
    }
    
    /**
     * Returns a range of hexadecimal digits of pi.
     * @param start The starting location of the range.
     * @param count The number of digits to return
     * @return An array containing the hexadecimal digits.
     */
    public static byte[] getDigits(int start, int count) {
        if (start < 0) {
            throw new RuntimeException("Invalid Interval");
        }

        if (count < 0) {
            throw new RuntimeException("Invalid Interval");
        }

        byte[] digits = new byte[count];
        double sum = 0;

        for (int i = 0; i < count; i++) {
            if (i % DigitsPerSum == 0) {
                sum = 4 * sum(1, start)
                        - 2 * sum(4, start)
                        - sum(5, start)
                        - sum(6, start);

                start += DigitsPerSum;
            }

            sum = 16 * (sum - Math.floor(sum));
            digits[i] = (byte) sum;
        }

        return digits;
    }

    /// <summary>
    /// Returns the sum of 16^(n - k)/(8 * k + m) from 0 to k.
    /// </summary>
    /// <param name="m"></param>
    /// <param name="n"></param>
    /// <returns></returns>
    private static double sum(int m, int n) {
        double sum = 0;
        int d = m;
        int power = n;

        while (true) {
            double term;

            if (power > 0) {
                term = (double) hexExponentModulo(power, d) / d;
            } else {
                term = Math.pow(16, power) / d;
                if (term < Epsilon) {
                    break;
                }
            }

            sum += term;
            power--;
            d += 8;
        }

        return sum;
    }

    /// <summary>
    /// Return 16^p mod m.
    /// </summary>
    /// <param name="p"></param>
    /// <param name="m"></param>
    /// <returns></returns>
    private static int hexExponentModulo(int p, int m) {
        int power = 1;
        while (power * 2 <= p) {
            power *= 2;
        }

        int result = 1;

        while (power > 0) {
            if (p >= power) {
                result *= 16;
                result %= m;
                p -= power;
            }

            power /= 2;

            if (power > 0) {
                result *= result;
                result %= m;
            }
        }

        return result;
    }

}
