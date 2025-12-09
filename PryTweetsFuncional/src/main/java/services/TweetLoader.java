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
    private List<Tweet> tweets;
    
    public TweetLoader() {
        this.tweets = new ArrayList<>();
        cargarDatosEjemplo(); 
    }
    
    private void cargarDatosEjemplo() {
        tweets.add(new Tweet(1, "Apple", "Positivo", "Excelente producto, muy innovador"));
        tweets.add(new Tweet(2, "Google", "Neutro", "Nueva actualización disponible"));
        tweets.add(new Tweet(3, "Amazon", "Negativo", "Problemas con mi pedido"));
        tweets.add(new Tweet(4, "Apple", "Positivo", "iPhone es increíble"));
        tweets.add(new Tweet(5, "Microsoft", "Positivo", "Windows 11 funciona muy bien"));
    }
    
    public void agregarTweet(Tweet tweet) {
        tweets.add(tweet);
        System.out.println("✓ Tweet agregado exitosamente!");
    }
    
    public List<Tweet> obtenerTodosTweets() {
        return new ArrayList<>(tweets);
    }
    
    public Tweet buscarPorId(int id) {
        for (Tweet tweet : tweets) {
            if (tweet.getId() == id) {
                return tweet;
            }
        }
        return null;
    }
    
    public List<Tweet> buscarPorEntidad(String entidad) {
        List<Tweet> resultados = new ArrayList<>();
        for (Tweet tweet : tweets) {
            if (tweet.getEntidad().equalsIgnoreCase(entidad)) {
                resultados.add(tweet);
            }
        }
        return resultados;
    }
    
    public List<Tweet> buscarPorSentimiento(String sentimiento) {
        List<Tweet> resultados = new ArrayList<>();
        for (Tweet tweet : tweets) {
            if (tweet.getSentimiento().equalsIgnoreCase(sentimiento)) {
                resultados.add(tweet);
            }
        }
        return resultados;
    }
    
    public int contarPorEntidad(String entidad) {
        int contador = 0;
        for (Tweet tweet : tweets) {
            if (tweet.getEntidad().equalsIgnoreCase(entidad)) {
                contador++;
            }
        }
        return contador;
    }
    
    public int contarPorSentimiento(String sentimiento) {
        int contador = 0;
        for (Tweet tweet : tweets) {
            if (tweet.getSentimiento().equalsIgnoreCase(sentimiento)) {
                contador++;
            }
        }
        return contador;
    }
    
    public void mostrarResumenAnalisis() {
        System.out.println("\n=== RESUMEN DE ANÁLISIS DE SENTIMIENTOS ===");
        
        System.out.println("\nTotal por sentimiento:");
        System.out.println("Positivos: " + contarPorSentimiento("Positivo"));
        System.out.println("Negativos: " + contarPorSentimiento("Negativo"));
        System.out.println("Neutros: " + contarPorSentimiento("Neutro"));
        
        System.out.println("\nEntidades mencionadas:");
        List<String> entidadesUnicas = obtenerEntidadesUnicas();
        for (String entidad : entidadesUnicas) {
            int conteo = contarPorEntidad(entidad);
            System.out.println("- " + entidad + ": " + conteo + " menciones");
        }
    }
    
    private List<String> obtenerEntidadesUnicas() {
        List<String> entidades = new ArrayList<>();
        for (Tweet tweet : tweets) {
            String entidad = tweet.getEntidad();
            if (!entidades.contains(entidad)) {
                entidades.add(entidad);
            }
        }
        return entidades;
    }
    
    public void mostrarTodosTweets() {
        if (tweets.isEmpty()) {
            System.out.println("No hay tweets disponibles");
            return;
        }
        
        System.out.println("\n=== LISTA COMPLETA DE TWEETS ===");
        for (Tweet tweet : tweets) {
            System.out.println(tweet);
        }
    }
    
    public int getTotalTweets() {
        return tweets.size();
    }
}