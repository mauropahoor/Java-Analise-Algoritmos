package main;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class Main {

    static Random random = new Random();

    // partition function with random pivot
    static int partition(int[] arr, int low, int high) {
        // escolher pivô aleatório e trocar com o último
        int pivotIndex = low + random.nextInt(high - low + 1);
        swap(arr, pivotIndex, high);

        int pivot = arr[high];
        int i = low - 1;

        for (int j = low; j <= high - 1; j++) {
            if (arr[j] < pivot) {
                i++;
                swap(arr, i, j);
            }
        }

        swap(arr, i + 1, high);
        return i + 1;
    }

    // swap function
    static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    // iterative quicksort (para evitar stackoverflow)
    static void quickSort(int[] arr, int low, int high) {
        int[] stack = new int[(high - low + 1) * 2]; // <- corrigido
        int top = -1;

        stack[++top] = low;
        stack[++top] = high;

        while (top >= 0) {
            high = stack[top--];
            low = stack[top--];

            int p = partition(arr, low, high);

            if (p - 1 > low) {
                stack[++top] = low;
                stack[++top] = p - 1;
            }

            if (p + 1 < high) {
                stack[++top] = p + 1;
                stack[++top] = high;
            }
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
        int[] tamanhos = {1, 4, 16, 64, 256, 1024, 4096, 16384, 65536, 262144, 1048576};
        int tentativas = 20;
        Random random = new Random();

        try (FileWriter writer = new FileWriter("QuickSort_Resultados.txt")) {
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
                        quickSort(arr, 0, arr.length - 1);
                        long end = System.nanoTime();

                        tempos[t] = (end - start) / 1e9; // segundos
                    }

                    double m = media(tempos);
                    double dp = desvioPadrao(tempos, m);

                    writer.write(String.format("QuickSort | %d | %s | %.6f | %.6f%n", n, caso, m, dp));
                }
            }

            System.out.println("Resultados salvos em QuickSort_Resultados.txt");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
