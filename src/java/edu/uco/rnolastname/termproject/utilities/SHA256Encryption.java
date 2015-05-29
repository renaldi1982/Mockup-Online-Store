package edu.uco.rnolastname.termproject.utilities;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA256Encryption {
    
    public static String encrypt(String input){
        try{
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(input.getBytes("UTF-8"));
            byte[] digest = md.digest();
            BigInteger bigInt = new BigInteger(1,digest);            
            return bigInt.toString(16);
        }catch(NoSuchAlgorithmException | UnsupportedEncodingException e){
            System.out.println("Error encrypted with message: " + e.getMessage());
            return null;
        }
    }
}
