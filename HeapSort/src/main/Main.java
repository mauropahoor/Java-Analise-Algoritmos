package main;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

public class Main {

    // -------- HeapSort ----------
    static void heapify(int arr[], int n, int i) {
        int largest = i; 
        int l = 2 * i + 1; 
        int r = 2 * i + 2; 

        if (l < n && arr[l] > arr[largest]) {
            largest = l;
        }
        if (r < n && arr[r] > arr[largest]) {
            largest = r;
        }
        if (largest != i) {
            int swap = arr[i];
            arr[i] = arr[largest];
            arr[largest] = swap;
            heapify(arr, n, largest);
        }
    }

    static void heapSort(int arr[]) {
        int n = arr.length;
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(arr, n, i);
        }
        for (int i = n - 1; i > 0; i--) {
            int temp = arr[0];
            arr[0] = arr[i];
            arr[i] = temp;
            heapify(arr, i, 0);
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
        int[] tamanhos = {1, 4, 16, 64, 256, 1024, 4096, 16384, 65536, 262144, 1048576};
        int tentativas = 20;

        Random rand = new Random();
        FileWriter fw = new FileWriter("resultados_heap.txt");

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
                heapSort(copia);
                long fim = System.nanoTime();
                tempos[t] = fim - inicio;
            }
            double media = media(tempos) / 1_000_000_000.0;
            double dp = desvioPadrao(tempos, media(tempos)) / 1_000_000_000.0;
            fw.write("HeapSort | " + n + " | Melhor | " + String.format("%.6f", media) + " | " + String.format("%.6f", dp) + "\n");

            // Pior caso
            tempos = new long[tentativas];
            for (int t = 0; t < tentativas; t++) {
                int[] copia = Arrays.copyOf(decrescente, n);
                long inicio = System.nanoTime();
                heapSort(copia);
                long fim = System.nanoTime();
                tempos[t] = fim - inicio;
            }
            media = media(tempos) / 1_000_000_000.0;
            dp = desvioPadrao(tempos, media(tempos)) / 1_000_000_000.0;
            fw.write("HeapSort | " + n + " | Pior | " + String.format("%.6f", media) + " | " + String.format("%.6f", dp) + "\n");

            // Caso médio (aleatório)
            tempos = new long[tentativas];
            for (int t = 0; t < tentativas; t++) {
                int[] aleatorio = new int[n];
                for (int i = 0; i < n; i++) {
                    aleatorio[i] = rand.nextInt();
                }
                long inicio = System.nanoTime();
                heapSort(aleatorio);
                long fim = System.nanoTime();
                tempos[t] = fim - inicio;
            }
            media = media(tempos) / 1_000_000_000.0;
            dp = desvioPadrao(tempos, media(tempos)) / 1_000_000_000.0;
            fw.write("HeapSort | " + n + " | Médio | " + String.format("%.6f", media) + " | " + String.format("%.6f", dp) + "\n");
        }

        fw.close();
        System.out.println("Resultados salvos em resultados_heap.txt");
    }
}
