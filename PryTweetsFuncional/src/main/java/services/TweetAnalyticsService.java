/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import model.Tweet;

/**
 *
 * @author emiliano arana
 */
public class TweetAnalyticsService {
    
    public double calcularPromedioLongitud(List<Tweet> tweets, String sentimiento) {
        return tweets.stream()
                .filter(t -> t.getSentimiento().equalsIgnoreCase(sentimiento))
                .mapToInt(t -> t.getTexto().length())
                .average()
                .orElse(0.0);
    }
    
    public Map<String, Long> contarTweetsPorSentimiento(List<Tweet> tweets) {
        return tweets.stream()
                .collect(Collectors.groupingBy(
                        t -> t.getSentimiento(),  
                        Collectors.counting()
                ));
    }
    
    // Contar tweets por entidad
    public Map<String, Long> contarTweetsPorEntidad(List<Tweet> tweets) {
        return tweets.stream()
                .collect(Collectors.groupingBy(
                        Tweet::getEntidad,
                        Collectors.counting()
                ));
    }
    
    public Tweet encontrarTweetMasLargo(List<Tweet> tweets) {
        return tweets.stream()
                .max((t1, t2) -> Integer.compare(t1.getTexto().length(), t2.getTexto().length()))
                .orElse(null);
    }
    
    public Tweet encontrarTweetMasCorto(List<Tweet> tweets) {
        return tweets.stream()
                .min((t1, t2) -> Integer.compare(t1.getTexto().length(), t2.getTexto().length()))
                .orElse(null);
    }
    
    public List<Tweet> filtrarPorLongitudMinima(List<Tweet> tweets, int longitudMinima) {
        return tweets.stream()
                .filter(t -> t.getTexto().length() >= longitudMinima)
                .toList();
    }
    
    public String generarEstadisticas(List<Tweet> tweets) {
        long total = tweets.size();
        Map<String, Long> porSentimiento = contarTweetsPorSentimiento(tweets);
        Map<String, Long> porEntidad = contarTweetsPorEntidad(tweets);
        
        StringBuilder sb = new StringBuilder();
        sb.append("=== ESTADÍSTICAS DE TWEETS ===\n");
        sb.append("Total de tweets: ").append(total).append("\n\n");
        
        sb.append("Distribución por sentimiento:\n");
        porSentimiento.forEach((sentimiento, cantidad) -> 
            sb.append("  ").append(sentimiento).append(": ").append(cantidad)
              .append(" (").append(String.format("%.1f", (cantidad * 100.0 / total)))
              .append("%)\n"));
        
        sb.append("\nDistribución por entidad:\n");
        porEntidad.forEach((entidad, cantidad) -> 
            sb.append("  ").append(entidad).append(": ").append(cantidad).append("\n"));
        
        return sb.toString();
    }
}
