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
public class BuscadorPalabraComun {

    public static void main(String[] args) throws IOException {
        
        String ubicacionEntrada;

        // Verificamos si la ruta del archivo fue pasada como argumento o no
        if (args.length < 1) {
            // Solicitar la ruta al usuario si no se pasó como argumento
            Scanner scanner = new Scanner(System.in, "UTF-8"); 
            System.out.print("Introduce la ruta completa (Absoluta) del fichero a procesar: ");
            ubicacionEntrada = scanner.nextLine().replace("\\", "/");
        } else {
            ubicacionEntrada = args[0].replace("\\", "/");
        }

        // Validamos archivo de entrada
        File ficheroEntrada = new File(ubicacionEntrada);
        if (!ficheroEntrada.exists() || !ficheroEntrada.isFile()) {
            System.out.println("El archivo especificado no existe o no es un archivo válido: " + ubicacionEntrada);
            return;
        }

        // Creamos el directorio de salida si no existe (resultados)
        File dirSalida = new File("resultados");
        if (!dirSalida.exists()) {
            dirSalida.mkdirs();
        }

        // Contamos las palabras del fichero
        Map<String, Integer> contadorPalabras = contadorPalabraFichero(ficheroEntrada);

        // Aquí averiguamos que palabra se repite más
        Map.Entry<String, Integer> entradaMasRepetida = contadorPalabras.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .orElse(null);

        String palabraMasRepetida = entradaMasRepetida != null ? entradaMasRepetida.getKey() : "N/A";
        int contMaximo = entradaMasRepetida != null ? entradaMasRepetida.getValue() : 0;

        // Creamos el fichero de salida con el mismo nombre que el fichero procesado
        File ficheroSalida = new File(dirSalida, ficheroEntrada.getName());
        escrituraResultadoFichero(ficheroSalida, palabraMasRepetida, contMaximo);

        /** Imprimimos el resultado por consola (No lo pide la actividad, pero me resultaba útil 
            para comprobar el resultado sin necesidad de abrir los ficheros.
        */
        System.out.printf("En el fichero %s, la palabra que más aparece es '%s' con %d apariciones.%n",
                ficheroEntrada.getName(), palabraMasRepetida, contMaximo);
    }

    // Aquí leemos el fichero y contamos las ocurrencias.
     
    private static Map<String, Integer> contadorPalabraFichero(File archivo) throws IOException {
        Map<String, Integer> contadorPalabra = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                // Dividir la línea en palabras utilizando una expresión regular
                String[] palabras = linea.toLowerCase().split("\\W+");
                for (String palabra : palabras) {
                    if (!palabra.isEmpty()) {
                        contadorPalabra.put(palabra, contadorPalabra.getOrDefault(palabra, 0) + 1);
                    }
                }
            }
        }
        return contadorPalabra;
    }

    
     // Escribe el resultado en un archivo, con la palabra más repetida y su conteo.
     
    private static void escrituraResultadoFichero(File ficheroSalida, String palabraMasRepetida, int contadorMaximo) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ficheroSalida))) {
            bw.write(String.format("La palabra que más aparece es '%s' con %d apariciones.%n",
                    palabraMasRepetida, contadorMaximo));
        }
    }
}


