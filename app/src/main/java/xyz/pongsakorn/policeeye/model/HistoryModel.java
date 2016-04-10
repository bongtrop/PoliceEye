package xyz.pongsakorn.policeeye.model;

/**
 * Created by Porpeeranut on 28/2/2559.
 */
public class HistoryModel {
    public String fileName;
    public String name;
    public String gender;
    public String note;
    public String people;
    public String algo;

    public HistoryModel(String fileName, String name, String gender, String note, String people, String algo) {
        this.fileName = fileName;
        this.name = name;
        this.gender = gender;
        this.note = note;
        this.people = people;
        this.algo = algo;
    }
}