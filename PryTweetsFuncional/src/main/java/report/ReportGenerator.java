/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package report;

import java.io.BufferedWriter;
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
    
    public void guardarTweetsLimpios(List<Tweet> tweets, String rutaSalida) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(rutaSalida))) {
            writer.println("ID,Entidad,Sentimiento,Texto");
            tweets.forEach(tweet -> writer.println(tweet));
            System.out.println("✓ Tweets guardados en: " + rutaSalida);
        } catch (IOException e) {
            System.err.println("Error guardando tweets: " + e.getMessage());
        }
    }
    
    
    public void guardarResumenEstadisticas(String resumen, String rutaSalida) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(rutaSalida))) {
            writer.write(resumen);
            System.out.println("✓ Resumen guardado en: " + rutaSalida);
        } catch (IOException e) {
            System.err.println("Error guardando resumen: " + e.getMessage());
        }
    }
}