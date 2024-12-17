/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tarea1pgvjcg;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author 3DAM Jose Carrillo González - Tarea 1 - PGV
 */
public class ProcesarTextoConcurrente {

     public static void main(String[] args) throws IOException, InterruptedException {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Introduce la longitud mínima de las palabras a contar (0 para no aplicar filtro): ");
        int LongMinima = scanner.nextInt();
        System.out.println("Longitud mínima seleccionada: " + LongMinima);

        File dirEntrada = new File("ficheros");
        File dirSalida = new File("resultados");

        if (!dirSalida.exists()) {
            dirSalida.mkdirs();
        }

        File[] ficheros = dirEntrada.listFiles((dir, nombre) -> nombre.endsWith(".txt"));

        if (ficheros == null || ficheros.length == 0) {
            System.out.println("No se encontraron archivos en la carpeta: " + dirEntrada.getAbsolutePath());
            return;
        }

        List<Process> procesos = new ArrayList<>();

        // Iniciar el temporizador
        Timer temporizador = new Timer();

        for (File archivo : ficheros) {
            ProcessBuilder pb = new ProcessBuilder(
                "java", 
                "-cp", 
                ".", 
                "BuscadorPalabraLarga",
                archivo.getAbsolutePath(),
                String.valueOf(LongMinima),
                dirSalida.getAbsolutePath()
            );
            procesos.add(pb.start());
        }

        for (Process proceso : procesos) {
            proceso.waitFor();
        }

        // Finalizar el temporizador y mostrar el tiempo transcurrido
        temporizador.finalizar();
        System.out.printf("Tiempo total del procesamiento concurrente: %.2f segundos.%n", temporizador.tiempoTotal());

        // Leer los resultados y mostrarlos en el formato deseado
        File[] resultadoFicheros = dirSalida.listFiles((dir, nombre) -> nombre.endsWith(".txt"));
        if (resultadoFicheros != null) {
            for (File ficherosResultado : resultadoFicheros) {
                try (BufferedReader br = new BufferedReader(new FileReader(ficherosResultado))) {
                    String line;
                    boolean isResumen = true; // Variable para controlar si estamos en el resumen

                    while ((line = br.readLine()) != null) {
                        if (isResumen) {
                            // Mostrar solo la línea del resumen por consola
                            System.out.println(line);
                            isResumen = false; // Solo la primera línea es el resumen
                        }
                    }
                }
            }
        }
    }
}

