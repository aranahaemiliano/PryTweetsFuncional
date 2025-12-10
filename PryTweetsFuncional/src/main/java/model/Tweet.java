/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author emiliano arana
 */
public class Tweet {
    private int id;
    private String entidad;
    private String sentimiento;
    private String texto;

    public Tweet(int id, String entidad, String sentimiento, String texto) {
        this.id = id;
        this.entidad = entidad;
        this.sentimiento = sentimiento;
        this.texto = texto;
    }
    
    // Getters
    public int getId() { return id; }
    public String getEntidad() { return entidad; }
    public String getSentimiento() { return sentimiento; }
    public String getTexto() { return texto; }
    

    @Override
    public String toString() {
        String textoEscapado = texto;
        if (textoEscapado.contains(",") || textoEscapado.contains("\"")) {
            textoEscapado = "\"" + textoEscapado.replace("\"", "\"\"") + "\"";
        }
        return id + "," + entidad + "," + sentimiento + "," + textoEscapado;
    }
}