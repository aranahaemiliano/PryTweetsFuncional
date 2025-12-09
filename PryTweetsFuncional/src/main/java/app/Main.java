/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import model.Tweet;
import report.ReportGenerator;
import services.TextTransformService;
import services.TweetAnalyticsService;
import services.TweetLoader;

/**
 *
 * @author emiliano arana
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("=== SISTEMA DE ANÁLISIS DE TWEETS (FUNCIONAL) ===\n");
        
        TweetLoader tweetLoader = new TweetLoader();
        TextTransformService transformService = new TextTransformService();
        TweetAnalyticsService analyticsService = new TweetAnalyticsService();
        ReportGenerator reportGenerator = new ReportGenerator();
        
        List<Tweet> tweets = tweetLoader.obtenerTodosTweets();
        
        System.out.println("=== TWEETS ORIGINALES ===");
        tweets.forEach(System.out::println);
        
        System.out.println("\n=== TRANSFORMACIONES ===");
        
        System.out.println("\n1. Tweets en MAYÚSCULAS:");
        List<Tweet> tweetsMayusculas = transformService.transformarTweets(tweets, transformService.aMayusculas);
        tweetsMayusculas.forEach(t -> System.out.println(t.getTexto()));
        
        System.out.println("\n2. Tweets sin menciones:");
        List<Tweet> tweetsSinMenciones = transformService.transformarTweets(tweets, transformService.sinMenciones);
        tweetsSinMenciones.forEach(t -> System.out.println(t.getTexto()));
        
        System.out.println("\n3. Tweets sin hashtags:");
        List<Tweet> tweetsSinHashtags = transformService.transformarTweets(tweets, transformService.sinHashtags);
        tweetsSinHashtags.forEach(t -> System.out.println(t.getTexto()));
        

        System.out.println("\n=== ANÁLISIS ===");
        

        String estadisticas = analyticsService.generarEstadisticas(tweets);
        System.out.println(estadisticas);
       
        System.out.println("Promedio longitud tweets POSITIVOS: " + 
                String.format("%.2f", analyticsService.calcularPromedioLongitud(tweets, "positivo")));
        System.out.println("Promedio longitud tweets NEGATIVOS: " + 
                String.format("%.2f", analyticsService.calcularPromedioLongitud(tweets, "negativo")));
        Tweet masLargo = analyticsService.encontrarTweetMasLargo(tweets);
        Tweet masCorto = analyticsService.encontrarTweetMasCorto(tweets);
        
        if (masLargo != null) {
            System.out.println("\nTweet más largo (" + masLargo.getTexto().length() + " chars):");
            System.out.println(masLargo.getTexto());
        }
        
        if (masCorto != null) {
            System.out.println("\nTweet más corto (" + masCorto.getTexto().length() + " chars):");
            System.out.println(masCorto.getTexto());
        }
        
 
        System.out.println("\n=== GENERANDO REPORTES ===");
     
        String rutaReportes = "reportes";
        try {
            java.nio.file.Files.createDirectories(java.nio.file.Path.of(rutaReportes));
        } catch (Exception e) {
            System.out.println("Error creando directorio: " + e.getMessage());
        }
        
 
        reportGenerator.guardarTweetsLimpios(tweetsSinMenciones, rutaReportes + "/tweets_limpios.csv");
        
  
        reportGenerator.guardarResumenEstadisticas(estadisticas, rutaReportes + "/estadisticas.txt");
        
       
        reportGenerator.generarReporteCompleto(tweets, rutaReportes);
        
    
        System.out.println("\n=== PIPELINE DE TRANSFORMACIONES ===");
        System.out.println("Aplicando múltiples transformaciones:");
        
        List<Function<Tweet, Tweet>> pipeline = new ArrayList<>();
        pipeline.add(transformService.sinMenciones);
        pipeline.add(transformService.sinHashtags);
        pipeline.add(transformService.sinEspaciosExtra);
        pipeline.add(transformService.aMayusculas);
        
        transformService.procesarConPipeline(tweets, pipeline, 
            t -> System.out.println("Resultado: " + t.getTexto()));
        
        System.out.println("\n=== PROCESO COMPLETADO ===");
    }
}