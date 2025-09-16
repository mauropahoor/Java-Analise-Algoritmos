package main;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

public class Main {

    // -------- Insertion Sort ----------
    static void sort(int arr[]) {
        int n = arr.length;
        for (int i = 1; i < n; ++i) {
            int key = arr[i];
            int j = i - 1;
            while (j >= 0 && arr[j] > key) {
                arr[j + 1] = arr[j];
                j = j - 1;
            }
            arr[j + 1] = key;
        }
    }

    // -------- Estatística ----------
    static double media(long[] valores) {
        long soma = 0;
        for (long v : valores) soma += v;
        return (double) soma / valores.length;
    }

    static double desvioPadrao(long[] valores, double media) {
        double soma = 0;
        for (long v : valores) soma += Math.pow(v - media, 2);
        return Math.sqrt(soma / valores.length);
    }

    // -------- Testes ----------
    public static void main(String[] args) throws IOException {
        int[] tamanhos = {1, 10, 50, 100, 500, 1000, 5000, 10000, 20000, 50000};
        int tentativas = 20;

        Random rand = new Random();
        FileWriter fw = new FileWriter("resultados_insertion.txt");

        fw.write("Algoritmo | Tamanho | Caso | Tempo Médio (s) | Desvio Padrão (s)\n");

        for (int n : tamanhos) {
            int[] crescente = new int[n];
            int[] decrescente = new int[n];
            for (int i = 0; i < n; i++) {
                crescente[i] = i;
                decrescente[i] = n - i;
            }

            // Melhor caso
            long[] tempos = new long[tentativas];
            for (int t = 0; t < tentativas; t++) {
                int[] copia = Arrays.copyOf(crescente, n);
                long inicio = System.nanoTime();
                sort(copia);
                long fim = System.nanoTime();
                tempos[t] = fim - inicio;
            }
            double media = media(tempos) / 1_000_000_000.0;
            double dp = desvioPadrao(tempos, media(tempos)) / 1_000_000_000.0;
            fw.write("InsertionSort | " + n + " | Melhor | " + String.format("%.6f", media) + " | " + String.format("%.6f", dp) + "\n");

            // Pior caso
            tempos = new long[tentativas];
            for (int t = 0; t < tentativas; t++) {
                int[] copia = Arrays.copyOf(decrescente, n);
                long inicio = System.nanoTime();
                sort(copia);
                long fim = System.nanoTime();
                tempos[t] = fim - inicio;
            }
            media = media(tempos) / 1_000_000_000.0;
            dp = desvioPadrao(tempos, media(tempos)) / 1_000_000_000.0;
            fw.write("InsertionSort | " + n + " | Pior | " + String.format("%.6f", media) + " | " + String.format("%.6f", dp) + "\n");

            // Caso médio (aleatório)
            tempos = new long[tentativas];
            for (int t = 0; t < tentativas; t++) {
                int[] aleatorio = new int[n];
                for (int i = 0; i < n; i++) {
                    aleatorio[i] = rand.nextInt();
                }
                long inicio = System.nanoTime();
                sort(aleatorio);
                long fim = System.nanoTime();
                tempos[t] = fim - inicio;
            }
            media = media(tempos) / 1_000_000_000.0;
            dp = desvioPadrao(tempos, media(tempos)) / 1_000_000_000.0;
            fw.write("InsertionSort | " + n + " | Médio | " + String.format("%.6f", media) + " | " + String.format("%.6f", dp) + "\n");
        }

        fw.close();
        System.out.println("Resultados salvos em resultados_insertion.txt");
    }
}
