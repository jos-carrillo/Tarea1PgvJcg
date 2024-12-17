/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tarea1pgvjcg;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 *
 * @author 3DAM Jose Carrillo González - Tarea 1 - PGV
 */
public class ProcesTextoSecuencial {

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Introduce la longitud mínima de las palabras a contar (0 para no aplicar filtro): ");
        int minLongitud = scanner.nextInt();
        System.out.println("Longitud mínima seleccionada: " + minLongitud);

        File dirEntrada = new File("ficheros");
        File dirSalida = new File("resultados");

        if (!dirSalida.exists()) {
            dirSalida.mkdirs();
        }

        File[] fichero = dirEntrada.listFiles((dir, name) -> name.endsWith(".txt"));

        if (fichero == null || fichero.length == 0) {
            System.out.println("No se encontraron archivos en la carpeta: " + dirEntrada.getAbsolutePath());
            return;
        }

        // Iniciar el temporizador
        Timer temporizador = new Timer();

        for (File fila : fichero) {
            Map<String, Integer> contadorPalabra = contadorPalabrasFichero(fila, minLongitud);

            Map.Entry<String, Integer> masRepetidaEntrada = contadorPalabra.entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .orElse(null);

            String masRepetida = masRepetidaEntrada != null ? masRepetidaEntrada.getKey() : "N/A";
            int maxCount = masRepetidaEntrada != null ? masRepetidaEntrada.getValue() : 0;

            File archivoSalida = new File(dirSalida, fila.getName());
            escrituraResultadosFichero(archivoSalida, fila.getName(), contadorPalabra, masRepetida, maxCount);

            System.out.printf("En el fichero %s la palabra que más aparece es '%s' que aparece %d veces.%n",
                    fila.getName(), masRepetida, maxCount);
        }

        // Finalizar el temporizador y mostrar el tiempo transcurrido
        temporizador.finalizar();
        System.out.printf("Tiempo total del procesamiento secuencial: %.2f segundos.%n", temporizador.tiempoTotal());
    }

    private static Map<String, Integer> contadorPalabrasFichero(File archivo, int minLength) throws IOException {
        Map<String, Integer> contador = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] words = linea.toLowerCase().split("\\W+");
                for (String palabra : words) {
                    if (!palabra.isEmpty() && palabra.length() >= minLength) {
                        contador.put(palabra, contador.getOrDefault(palabra, 0) + 1);
                    }
                }
            }
        }
        return contador;
    }

    private static void escrituraResultadosFichero(File ficheroSalida, String nombreFichero, Map<String, Integer> contadorPalabra, String palabraMasRepetida, int conteoMaximo) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ficheroSalida))) {
            bw.write(String.format("En el fichero %s la palabra que más aparece es '%s' que aparece %d veces.%n%n",
                    nombreFichero, palabraMasRepetida, conteoMaximo));

            bw.write("Listado de palabras:\n");
            for (Map.Entry<String, Integer> entry : contadorPalabra.entrySet()) {
                bw.write(entry.getKey() + ": " + entry.getValue());
                bw.newLine();
            }
        }
    }
}
