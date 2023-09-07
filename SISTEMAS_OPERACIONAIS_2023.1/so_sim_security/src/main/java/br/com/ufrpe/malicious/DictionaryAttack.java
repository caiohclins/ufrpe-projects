package br.com.ufrpe.malicious;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class DictionaryAttack {

    public static void decrypt(String senhaCorreta) {
        ArrayList<String> dicionario = criarDicionario();
        int tentativas = 0;
        
        long startTime = System.currentTimeMillis(); // Inicia a contagem do tempo

        for (String palavra : dicionario) {
            tentativas++;
            
            if (palavra.equals(senhaCorreta)) {
                System.out.println("Senha correta encontrada: " + palavra);
                break;
            }
        }

        long endTime = System.currentTimeMillis(); // Finaliza a contagem do tempo
        long duracao = endTime - startTime; // Calcula a duração em milissegundos
        
        System.out.println("Número de tentativas: " + tentativas);
        System.out.println("Tempo total em milissegundos: " + duracao);
    }
    
    private static ArrayList<String> criarDicionario() {
        ArrayList<String> dicionario = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader("resources\\10-million-password-list-top-10000.txt"));
                String linha;
        while ((linha = reader.readLine()) != null) {
            dicionario.add(linha);
        }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        
        // Adicione aqui as palavras que deseja testar
        dicionario.add("senha");
        dicionario.add("123");
        dicionario.add("senha123");
        
        return dicionario;
    }
}