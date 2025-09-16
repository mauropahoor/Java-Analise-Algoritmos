package main;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

class Main {

    // -------- Merge Sort ----------
    static void merge(int arr[], int l, int m, int r) {
        int n1 = m - l + 1;
        int n2 = r - m;

        int L[] = new int[n1];
        int R[] = new int[n2];

        for (int i = 0; i < n1; ++i)
            L[i] = arr[l + i];
        for (int j = 0; j < n2; ++j)
            R[j] = arr[m + 1 + j];

        int i = 0, j = 0;
        int k = l;

        while (i < n1 && j < n2) {
            if (L[i] <= R[j]) {
                arr[k] = L[i];
                i++;
            } else {
                arr[k] = R[j];
                j++;
            }
            k++;
        }

        while (i < n1) {
            arr[k] = L[i];
            i++;
            k++;
        }

        while (j < n2) {
            arr[k] = R[j];
            j++;
            k++;
        }
    }

    static void mergeSort(int arr[], int l, int r) {
        if (l < r) {
            int m = l + (r - l) / 2;
            mergeSort(arr, l, m);
            mergeSort(arr, m + 1, r);
            merge(arr, l, m, r);
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
        FileWriter fw = new FileWriter("resultados_merge.txt");

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
                mergeSort(copia, 0, copia.length - 1);
                long fim = System.nanoTime();
                tempos[t] = fim - inicio;
            }
            double media = media(tempos) / 1_000_000_000.0;
            double dp = desvioPadrao(tempos, media(tempos)) / 1_000_000_000.0;
            fw.write("MergeSort | " + n + " | Melhor | " + String.format("%.6f", media) + " | " + String.format("%.6f", dp) + "\n");

            // Pior caso
            tempos = new long[tentativas];
            for (int t = 0; t < tentativas; t++) {
                int[] copia = Arrays.copyOf(decrescente, n);
                long inicio = System.nanoTime();
                mergeSort(copia, 0, copia.length - 1);
                long fim = System.nanoTime();
                tempos[t] = fim - inicio;
            }
            media = media(tempos) / 1_000_000_000.0;
            dp = desvioPadrao(tempos, media(tempos)) / 1_000_000_000.0;
            fw.write("MergeSort | " + n + " | Pior | " + String.format("%.6f", media) + " | " + String.format("%.6f", dp) + "\n");

            // Caso médio (aleatório)
            tempos = new long[tentativas];
            for (int t = 0; t < tentativas; t++) {
                int[] aleatorio = new int[n];
                for (int i = 0; i < n; i++) {
                    aleatorio[i] = rand.nextInt();
                }
                long inicio = System.nanoTime();
                mergeSort(aleatorio, 0, aleatorio.length - 1);
                long fim = System.nanoTime();
                tempos[t] = fim - inicio;
            }
            media = media(tempos) / 1_000_000_000.0;
            dp = desvioPadrao(tempos, media(tempos)) / 1_000_000_000.0;
            fw.write("MergeSort | " + n + " | Médio | " + String.format("%.6f", media) + " | " + String.format("%.6f", dp) + "\n");
        }

        fw.close();
        System.out.println("Resultados salvos em resultados_merge.txt");
    }
}
