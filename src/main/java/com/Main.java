package com;


import java.util.*; 
import java.lang.*;
import java.io.*;
import java.lang.Math;

public class Main {
    public static void cipher(String S) {
        //this is default OUTPUT. You can change it
        String result="";
        
        //WRITE YOUR LOGIC HERE:
        int k=0;
        char array[]=new char[S.length()];
        for(int i=0;i<S.length();i++){
            array[i]=S.charAt(i);
        }
        for(char c:array){
            if(c>='A' && c<='Z'){
                k=c-64;
                System.out.print(String.format("%02d",k));
            }
            else{
                k=c-96;
                 System.out.print(String.format("%02d",k));
            }
        }
                
                
        //For optimizing code, You are free to make changes to "return type", "variables" and "Libraries".        
        
    }
    public static void main(String[] args) {
        // INPUT [uncomment & modify if required]
        Scanner sc = new Scanner(System.in);
        String S = sc.next();
       
        // OUTPUT [uncomment & modify if required]
        cipher(S);
        sc.close();
    }
}

