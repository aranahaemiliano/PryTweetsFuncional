/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import model.Tweet;

/**
 *
 * @author emiliano arana
 */
public class TextTransformService {
   
    public Function<Tweet, Tweet> aMayusculas = t ->
        new Tweet(t.getId(), t.getEntidad(), t.getSentimiento(), 
                 t.getTexto().toUpperCase());
    
    public Function<Tweet, Tweet> aMinusculas = t ->
        new Tweet(t.getId(), t.getEntidad(), t.getSentimiento(), 
                 t.getTexto().toLowerCase());
    
    public Function<Tweet, Tweet> sinMenciones = t -> {
        String limpio = t.getTexto().replaceAll("@\\w+", "");
        return new Tweet(t.getId(), t.getEntidad(), t.getSentimiento(), 
                        limpio.trim());
    };
    
    public Function<Tweet, Tweet> sinHashtags = t -> {
        String limpio = t.getTexto().replaceAll("#\\w+", "");
        return new Tweet(t.getId(), t.getEntidad(), t.getSentimiento(), 
                        limpio.trim());
    };
    
    public Function<Tweet, Tweet> sinEspaciosExtra = t -> {
        String limpio = t.getTexto().replaceAll("\\s+", " ").trim();
        return new Tweet(t.getId(), t.getEntidad(), t.getSentimiento(), limpio);
    };
    
    public List<Tweet> transformarTweets(List<Tweet> tweets, Function<Tweet, Tweet> transformacion) {
        return tweets.stream()
                .map(transformacion)
                .toList();
    }
    
    public void procesarTweets(List<Tweet> tweets, 
                              Function<Tweet, Tweet> transformacion, 
                              Consumer<Tweet> accionFinal) {
        tweets.stream()
              .map(transformacion)
              .forEach(accionFinal);
    }
}