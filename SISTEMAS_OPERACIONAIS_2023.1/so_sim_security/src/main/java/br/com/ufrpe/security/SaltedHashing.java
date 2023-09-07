package br.com.ufrpe.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class SaltedHashing {
    
    public static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return bytesToHex(salt); // Converte o salt em uma representação hexadecimal
    }

    public static String hashPasswordWithSalt(String password, String salt) throws NoSuchAlgorithmException {
        try {
            // Inicializa uma instância do algoritmo de hash SHA-256
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            // Converte o salt hexadecimal em bytes e adiciona aos bytes da senha
            md.update(hexStringToByteArray(salt));

            // Calcula o hash da senha com o salt
            byte[] hashedBytes = md.digest(password.getBytes());

            // Converte os bytes do hash em uma representação hexadecimal
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString(); // Retorna a senha criptografada com salt
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Função auxiliar para converter bytes em uma representação hexadecimal
    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    // Função auxiliar para converter uma representação hexadecimal em bytes
    private static byte[] hexStringToByteArray(String hex) {
        int len = hex.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4) + Character.digit(hex.charAt(i + 1), 16));
        }
        return data;
    }
}
