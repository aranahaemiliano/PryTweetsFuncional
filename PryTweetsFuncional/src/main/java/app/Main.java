/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
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
        System.out.println("=== PROYECTO 2° PARCIAL: ANÁLISIS FUNCIONAL DE TWEETS ===\n");
        
        Runnable pipelinePrincipal = () -> {
            try {
                System.out.println("Iniciando pipeline de procesamiento...\n");
                
                String rutaCSV = "data/twitters.csv";
                File archivoCSV = new File(rutaCSV);
                
                if (!archivoCSV.exists()) {
                    System.err.println("❌ ERROR: No se encuentra el archivo CSV en: " + archivoCSV.getAbsolutePath());
                    System.err.println("Coloca el archivo 'twitters.csv' en la carpeta 'data/'");
                    
                    File carpetaData = new File("data");
                    if (carpetaData.exists()) {
                        System.err.println("Archivos en carpeta 'data/':");
                        String[] archivos = carpetaData.list();
                        if (archivos != null) {
                            for (String archivo : archivos) {
                                System.err.println("  - " + archivo);
                            }
                        }
                    }
                    return;
                }
                
                System.out.println("✓ Archivo encontrado: " + archivoCSV.getAbsolutePath());
                System.out.println("Tamaño del archivo: " + archivoCSV.length() + " bytes\n");
                
                TweetLoader loader = new TweetLoader();
                Supplier<List<Tweet>> lector = loader.crearLectorTweets(rutaCSV);
                List<Tweet> tweets = lector.get();
                
                if (tweets.isEmpty()) {
                    System.err.println("❌ ERROR: No se pudieron cargar tweets del archivo.");
                    System.err.println("Verifica el formato del CSV. Debe tener al menos: Entidad,Sentimiento,Texto");
                    return;
                }
                
                System.out.println("✓ Tweets cargados exitosamente: " + tweets.size() + " registros");
                
                System.out.println("\n=== MUESTRA DE TWEETS CARGADOS (primeros 3) ===");
                tweets.stream().limit(3).forEach(t -> 
                    System.out.println("ID: " + t.getId() + 
                                     " | Entidad: " + t.getEntidad() + 
                                     " | Sentimiento: " + t.getSentimiento() + 
                                     " | Texto: " + (t.getTexto().length() > 50 ? 
                                        t.getTexto().substring(0, 50) + "..." : t.getTexto()))
                );
                
                System.out.println("\n=== APLICANDO TRANSFORMACIONES ===");
                TextTransformService transformService = new TextTransformService();
                
                List<Tweet> tweetsLimpios = transformService.transformarTweets(tweets, t -> {
                    String textoLimpio = t.getTexto();
                    
                    textoLimpio = textoLimpio.replaceAll("@\\w+", ""); 
                    textoLimpio = textoLimpio.replaceAll("#\\w+", ""); 
                    textoLimpio = textoLimpio.replaceAll("\\s+", " ").trim(); 
                    
                    return new Tweet(t.getId(), t.getEntidad(), t.getSentimiento(), textoLimpio);
                });
                
                System.out.println("✓ Transformaciones aplicadas a " + tweetsLimpios.size() + " tweets");
                
                
                System.out.println("\n=== REALIZANDO ANÁLISIS ESTADÍSTICO ===");
                TweetAnalyticsService analyticsService = new TweetAnalyticsService();
                
                
                double promedioPositivos = analyticsService.calcularPromedioLongitud(tweets, "positive");
                double promedioNegativos = analyticsService.calcularPromedioLongitud(tweets, "negative");
                double promedioNeutros = analyticsService.calcularPromedioLongitud(tweets, "neutral");
                
                System.out.println("✓ Promedios calculados");
                
                
                Map<String, Long> conteoSentimientos = analyticsService.contarTweetsPorSentimiento(tweets);
                Map<String, Long> conteoEntidades = analyticsService.contarTweetsPorEntidad(tweets);
                
                System.out.println("✓ Conteos realizados");
                
                File carpetaOutput = new File("output");
                if (!carpetaOutput.exists()) {
                    carpetaOutput.mkdirs();
                    System.out.println("✓ Carpeta 'output/' creada");
                }
                
                
                System.out.println("\n=== GENERANDO REPORTES ===");
                ReportGenerator reportGenerator = new ReportGenerator();
                
                
                String rutaTweetsLimpios = "output/tweets_limpios.csv";
                reportGenerator.guardarTweetsLimpios(tweetsLimpios, rutaTweetsLimpios);
                
                
                StringBuilder resumen = new StringBuilder();
                resumen.append("=== RESUMEN DE ANÁLISIS DE TWEETS ===\n\n");
                resumen.append("Archivo fuente: ").append(rutaCSV).append("\n");
                resumen.append("Fecha de análisis: ").append(java.time.LocalDateTime.now()).append("\n");
                resumen.append("Total de tweets procesados: ").append(tweets.size()).append("\n\n");
                
                resumen.append("--- DISTRIBUCIÓN POR SENTIMIENTO ---\n");
                conteoSentimientos.forEach((sentimiento, cantidad) -> {
                    double porcentaje = (cantidad * 100.0) / tweets.size();
                    resumen.append(String.format("  %s: %d tweets (%.1f%%)\n", 
                        sentimiento, cantidad, porcentaje));
                });
                
                resumen.append("\n--- PROMEDIO DE LONGITUD POR SENTIMIENTO ---\n");
                resumen.append(String.format("  Positivos: %.2f caracteres\n", promedioPositivos));
                resumen.append(String.format("  Negativos: %.2f caracteres\n", promedioNegativos));
                resumen.append(String.format("  Neutros: %.2f caracteres\n", promedioNeutros));
                
                resumen.append("\n--- ENTIDADES MÁS MENCIONADAS ---\n");
                
                conteoEntidades.entrySet().stream()
                    .sorted((e1, e2) -> Long.compare(e2.getValue(), e1.getValue()))
                    .limit(10)
                    .forEach(entry -> {
                        double porcentaje = (entry.getValue() * 100.0) / tweets.size();
                        resumen.append(String.format("  %s: %d menciones (%.1f%%)\n", 
                            entry.getKey(), entry.getValue(), porcentaje));
                    });
                
                resumen.append("\n--- ESTADÍSTICAS ADICIONALES ---\n");

                Tweet tweetMasLargo = tweets.stream()
                    .max((t1, t2) -> Integer.compare(t1.getTexto().length(), t2.getTexto().length()))
                    .orElse(null);
                
                if (tweetMasLargo != null) {
                    resumen.append("Tweet más largo (").append(tweetMasLargo.getTexto().length())
                           .append(" caracteres):\n  ").append(tweetMasLargo.getTexto()).append("\n");
                }
                
                String rutaResumen = "output/resumen_estadisticas.txt";
                reportGenerator.guardarResumenEstadisticas(resumen.toString(), rutaResumen);
                
                System.out.println("\n" + resumen.toString());
                
                System.out.println("\n=== DEMOSTRACIÓN PIPELINE FUNCTION + CONSUMER ===");
                System.out.println("Transformando primeros 2 tweets a mayúsculas:");
                
                Consumer<Tweet> mostrarTweet = tweet -> 
                    System.out.println("  [" + tweet.getSentimiento().toUpperCase() + "] " + 
                                     tweet.getTexto());
                
                transformService.procesarTweets(
                    tweets.stream().limit(2).toList(),
                    transformService.aMayusculas,
                    mostrarTweet
                );
                
                System.out.println("\n✅ PIPELINE COMPLETADO EXITOSAMENTE!");
                System.out.println("✅ Reportes generados en carpeta 'output/'");
                
            } catch (Exception e) {
                System.err.println("❌ ERROR CRÍTICO en el pipeline: " + e.getMessage());
                e.printStackTrace();
            }
        };
        
        System.out.println("Ejecutando Runnable principal...\n");
        pipelinePrincipal.run();
        
        System.out.println("\n=== FIN DEL PROGRAMA ===");
    }
}