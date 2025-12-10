/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import model.Tweet;

/**
 *
 * @author emiliano arana
 */
public class TweetLoader {
    
    public Supplier<List<Tweet>> crearLectorTweets(String rutaArchivo) {
        return () -> {
            List<Tweet> tweets = new ArrayList<>();
            int contador = 1; 
            
            try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
                String linea;
                
                System.out.println("Leyendo archivo: " + rutaArchivo);
                
                while ((linea = br.readLine()) != null && tweets.size() < 1000) {
                    try {
                        String[] partes = parsearLineaCSV(linea);
                        
                        if (partes.length >= 2) {
                            int id = contador; 
                            String entidad = partes.length > 0 ? partes[0].trim() : "Desconocida";
                            String sentimiento = partes.length > 1 ? partes[1].trim() : "Neutral";
                            String texto = partes.length > 2 ? partes[2].trim() : "";
                            
                            if (texto.startsWith("\"") && texto.endsWith("\"")) {
                                texto = texto.substring(1, texto.length() - 1);
                            }
                            
                            sentimiento = sentimiento.toLowerCase();
                            
                            tweets.add(new Tweet(id, entidad, sentimiento, texto));
                            contador++;
                        }
                        
                    } catch (Exception e) {
                        System.err.println("Error parseando línea: " + linea);
                    }
                }
                
                System.out.println("✓ Total tweets cargados: " + tweets.size());
              
                if (tweets.size() < 100) {
                    System.err.println("⚠ ADVERTENCIA: Solo se cargaron " + tweets.size() + " tweets");
                    System.err.println("Asegúrate de que el archivo CSV tenga al menos 100 registros");
                }
                
            } catch (IOException e) {
                System.err.println("❌ Error leyendo archivo: " + e.getMessage());
                System.err.println("Ruta intentada: " + new java.io.File(rutaArchivo).getAbsolutePath());
            }
            
            return tweets;
        };
    }
    
    private String[] parsearLineaCSV(String linea) {
        List<String> partes = new ArrayList<>();
        StringBuilder campoActual = new StringBuilder();
        boolean entreComillas = false;
        
        for (int i = 0; i < linea.length(); i++) {
            char c = linea.charAt(i);
            
            if (c == '"') {
                entreComillas = !entreComillas;
            } else if (c == ',' && !entreComillas) {
                partes.add(campoActual.toString());
                campoActual = new StringBuilder();
            } else {
                campoActual.append(c);
            }
        }
        
        partes.add(campoActual.toString());
        
        return partes.toArray(new String[0]);
    }
    
    public List<Tweet> cargarTweetsDesdeCSV(String rutaArchivo) {
        return crearLectorTweets(rutaArchivo).get();
    }
}