package br.com.ufrpe;

import br.com.ufrpe.malicious.BruteForce;
import br.com.ufrpe.security.SaltedHashing;
import br.com.ufrpe.security.SimpleHashing;

public class App {
    public static void main(String[] args) {

        String passwordSimple = "a68rqf";

        try {
            // Exemplo de Hashing Simples
            String hashedPasswordSimple = SimpleHashing.hashPassword(passwordSimple);
            System.out.println("Senha criptografada (Hashing Simples): " + hashedPasswordSimple);

            // Exemplo de Hashing com Salt
            String salt = SaltedHashing.generateSalt();
            String hashedPasswordSalted = SaltedHashing.hashPasswordWithSalt(passwordSimple, salt);
            System.out.println("Salt gerado: " + salt);
            System.out.println("Senha criptografada (Hashing com Salt): " + hashedPasswordSalted);
        } catch (Exception e) {
            // TODO: handle exception
        }

        BruteForce.decrypt(passwordSimple);
        //System.out.println("\n\n - - - - - - - - - - - - - - - - - - - - \n\n");
        //DictionaryAttack.decrypt(passwordSimple);
    }
}
