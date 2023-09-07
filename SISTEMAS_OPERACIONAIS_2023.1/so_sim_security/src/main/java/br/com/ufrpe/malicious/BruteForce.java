package br.com.ufrpe.malicious;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class BruteForce {

    public static void decrypt(String senhaCorreta) {
        HashMap<String, String> list = new HashMap<>();
        String senha;
        //long tentativas = 0;
        
        long startTime = System.currentTimeMillis(); // Inicia a contagem do tempo
        
        do {
            senha = gerarSenhaAleatoria(senhaCorreta); // Gera uma senha aleatória
            //tentativas ++;
            while (list.containsKey(senha)){
                senha = gerarSenhaAleatoria(senhaCorreta);
            }
            list.put(senha, null);
            if (list.size() % 1000 == 0){System.out.println(list.size() +" senhas testadas!");}
            // System.out.println("Tentando senha: " + senha);
        } while (!senha.equals(senhaCorreta));
        
        long endTime = System.currentTimeMillis(); // Finaliza a contagem do tempo
        long duracao = endTime - startTime; // Calcula a duração em milissegundos
        
        System.out.println("Senha correta encontrada: " + senha);
        System.out.println("Número de tentativas: " + list.size());
        //System.out.println("Número de tentativas: " + tentativas);
        System.out.println("Tempo total em milissegundos: " + duracao);
    }
    
    private static String gerarSenhaAleatoria(String senhaCorreta) {
        String caracteres = "abcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder senha = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < senhaCorreta.length(); i++) {
            int index = random.nextInt(caracteres.length());
            senha.append(caracteres.charAt(index));
        }
        
        return senha.toString();
    }
}
