package br.com.ufrpe.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SimpleHashing {
    public static String hashPassword(String password) throws NoSuchAlgorithmException {
        try {
            // Inicializa uma instância do algoritmo de hash SHA-256
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            
            // Converte a senha em bytes e calcula o hash
            byte[] hashedBytes = md.digest(password.getBytes());
            
            // Converte os bytes do hash em uma representação hexadecimal
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString(); // Retorna a senha criptografada
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}