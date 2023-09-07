package br.com.ufrpe.model;

import java.util.ArrayList;

public class Look {

    private int headStart;
    private ArrayList<Integer> requests;
    private int maxBlock;
    private int direction;
    
    public Look(int headStart, ArrayList<Integer> requests, int maxBlock) {
        this.headStart = headStart;
        this.requests = requests;
        this.maxBlock = maxBlock;
    }
    
    public void simulate(DiskMemory diskMemory) {
        int totalSeekTime = 0;
        int currentBlock = headStart;

        System.out.println("\nOrdem dos blocos visitados (LOOK):");
        System.out.print(headStart + " ");

        for (int block : requests) {
            setDirection(currentBlock, block);

            boolean cond = true;
            int seekTime = 0;
            while(cond){

                if (diskMemory.getMemory().get(currentBlock) != null && diskMemory.getMemory().get(currentBlock).equals(block)){
                    totalSeekTime++;
                    System.out.print("-> " + block + "(" + seekTime + "u.t.) ");
                    cond = false;
                } else {
                    seekTime++;
                    totalSeekTime++;
                    currentBlock = currentBlock + direction;
                }
                if (currentBlock == 0) {
                    this.direction = 1;
                    cond = false;
                } else if(currentBlock == maxBlock -1){
                    this.direction = -1;
                    cond = false;
                }
            }
        }

        totalSeekTime += Math.abs(currentBlock - maxBlock);

        System.out.println("\nTempo total de busca: " + totalSeekTime + "u.t.\n");
    }

    private void setDirection(int headStart, int block){
        if (block < headStart){
            this.direction = -1;
        } else if(block > headStart){
            this.direction = 1;
        } else {
            this.direction = 1;
        }
    }
}