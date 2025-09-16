package main;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

class Main {

    static void selectionSort(int[] arr){
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            int min_idx = i;
            for (int j = i + 1; j < n; j++) {
                if (arr[j] < arr[min_idx]) {
                    min_idx = j;
                }
            }
            int temp = arr[i];
            arr[i] = arr[min_idx];
            arr[min_idx] = temp;
        }
    }

    // calcular média
    static double media(double[] valores) {
        double soma = 0.0;
        for (double v : valores) soma += v;
        return soma / valores.length;
    }

    // calcular desvio padrão
    static double desvioPadrao(double[] valores, double media) {
        double soma = 0.0;
        for (double v : valores) soma += Math.pow(v - media, 2);
        return Math.sqrt(soma / valores.length);
    }

    public static void main(String[] args) {
        int[] tamanhos = {1, 10, 50, 100, 500, 1000, 5000, 10000, 20000, 50000};
        int tentativas = 20;
        Random random = new Random();

        try (FileWriter writer = new FileWriter("SelectionSort_Resultados.txt")) {
            writer.write("Algoritmo | Tamanho | Caso | Tempo Médio(s) | Desvio Padrão(s)\n");

            for (int n : tamanhos) {
                for (String caso : new String[]{"Melhor", "Pior", "Médio"}) {
                    double[] tempos = new double[tentativas];

                    for (int t = 0; t < tentativas; t++) {
                        int[] arr = new int[n];

                        if (caso.equals("Melhor")) {
                            for (int i = 0; i < n; i++) arr[i] = i; // crescente
                        } else if (caso.equals("Pior")) {
                            for (int i = 0; i < n; i++) arr[i] = n - i; // decrescente
                        } else { // Médio
                            for (int i = 0; i < n; i++) arr[i] = random.nextInt();
                        }

                        long start = System.nanoTime();
                        selectionSort(arr);
                        long end = System.nanoTime();

                        tempos[t] = (end - start) / 1e9; // segundos
                    }

                    double m = media(tempos);
                    double dp = desvioPadrao(tempos, m);

                    writer.write(String.format("SelectionSort | %d | %s | %.6f | %.6f%n", n, caso, m, dp));
                }
            }

            System.out.println("Resultados salvos em SelectionSort_Resultados.txt");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
