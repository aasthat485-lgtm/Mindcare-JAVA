package mindcare;
public class Mood {
    private int id;
    private String name;
    private String mood;
    private String note;

    public Mood(int id, String name, String mood, String note) {
        this.id = id;
        this.name = name;
        this.mood = mood;
        this.note = note;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getMood() { return mood; }
    public String getNote() { return note; }

    public void setName(String name) { this.name = name; }
    public void setMood(String mood) { this.mood = mood; }
    public void setNote(String note) { this.note = note; }
}
