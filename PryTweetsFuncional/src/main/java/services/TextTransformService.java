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

    public Function<Tweet, Tweet> aMayusculas = t
            -> new Tweet(t.getId(), t.getEntidad(), t.getSentimiento(),
                    t.getTexto().toUpperCase());

    public Function<Tweet, Tweet> sinMenciones = t -> {
        String limpio = t.getTexto()
            .replaceAll("^\"|\"$", "") 
            .replace("\"\"", "\"")  
            .replaceAll("@\\s*[^\\s,]+", "")
            .replaceAll("\\s+", " ")
            .trim();
        return new Tweet(t.getId(), t.getEntidad(), t.getSentimiento(), limpio);
    };

    public Function<Tweet, Tweet> sinHashtags = t -> {
        String nuevo = t.getTexto().replaceAll("#\\w+", "");
        return new Tweet(t.getId(), t.getEntidad(), t.getSentimiento(), nuevo.trim());
    };
    
    public Function<Tweet, Tweet> sinEspaciosExtra = t -> {
        String nuevo = t.getTexto().replaceAll("\\s+", " ").trim();
        return new Tweet(t.getId(), t.getEntidad(), t.getSentimiento(), nuevo);
    };
    
    public Function<Tweet, Tweet> aMinusculas = t -> 
            new Tweet(t.getId(), t.getEntidad(), t.getSentimiento(),
                    t.getTexto().toLowerCase());
    
    public Function<Tweet, Tweet> capitalizar = t -> {
        String texto = t.getTexto();
        if (texto == null || texto.isEmpty()) return t;
        
        String capitalizado = texto.substring(0, 1).toUpperCase() + 
                            texto.substring(1).toLowerCase();
        return new Tweet(t.getId(), t.getEntidad(), t.getSentimiento(), capitalizado);
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

    public void procesarConPipeline(List<Tweet> tweets, 
                                   List<Function<Tweet, Tweet>> transformaciones,
                                   Consumer<Tweet> accionFinal) {
        tweets.stream()
                .map(t -> {
                    Tweet resultado = t;
                    for (Function<Tweet, Tweet> transformacion : transformaciones) {
                        resultado = transformacion.apply(resultado);
                    }
                    return resultado;
                })
                .forEach(accionFinal);
    }
}