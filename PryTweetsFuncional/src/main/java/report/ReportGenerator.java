/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package report;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import model.Tweet;

/**
 *
 * @author emiliano arana
 */
public class ReportGenerator {
    
    private String escaparCSV(String texto) {
        if (texto == null) return "";
        if (texto.contains(",") || texto.contains("\"") || texto.contains("\n")) {
            texto = texto.replace("\"", "\"\"");
            return "\"" + texto + "\"";
        }
        return texto;
    }
    
    public void guardarTweetsLimpios(List<Tweet> tweets, String rutaSalida) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(rutaSalida))) {
            writer.println("ID,Entidad,Sentimiento,Texto");
         
            tweets.forEach(t -> {
                String linea = t.getId() + "," +
                               escaparCSV(t.getEntidad()) + "," +
                               escaparCSV(t.getSentimiento()) + "," +
                               escaparCSV(t.getTexto());
                writer.println(linea);
            });
            System.out.println("✓ Tweets guardados en: " + rutaSalida);
            
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
   
    public void guardarResumenEstadisticas(String resumen, String rutaSalida) {
        try {
            Files.writeString(Path.of(rutaSalida), resumen);
            System.out.println("✓ Resumen guardado en: " + rutaSalida);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
    
    public void generarReporteCompleto(List<Tweet> tweets, String rutaBase) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        
        
        guardarTweetsLimpios(tweets, rutaBase + "/tweets_originales_" + timestamp + ".csv");
        
      
        try (PrintWriter writer = new PrintWriter(new FileWriter(rutaBase + "/estadisticas_" + timestamp + ".txt"))) {
            writer.println("=== REPORTE DE ANÁLISIS DE TWEETS ===\n");
            writer.println("Fecha: " + java.time.LocalDateTime.now());
            writer.println("Total tweets: " + tweets.size());
            
           
            long positivos = tweets.stream().filter(t -> t.getSentimiento().equalsIgnoreCase("positivo")).count();
            long negativos = tweets.stream().filter(t -> t.getSentimiento().equalsIgnoreCase("negativo")).count();
            long neutros = tweets.stream().filter(t -> t.getSentimiento().equalsIgnoreCase("neutro")).count();
            
            writer.println("\nDistribución por sentimiento:");
            writer.println("  Positivos: " + positivos);
            writer.println("  Negativos: " + negativos);
            writer.println("  Neutros: " + neutros);
            
    
            List<String> entidades = tweets.stream()
                    .map(Tweet::getEntidad)
                    .distinct()
                    .toList();
            
            writer.println("\nEntidades analizadas: " + String.join(", ", entidades));
            
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}