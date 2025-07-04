package Entity;

public class Utilizator extends Entity {
    private int id;
    private String username;
    private String parola;

    public Utilizator(int id, String username, String parola) {
        super(id);
        this.username = username;
        this.parola = parola;
    }

    public String getUsername() { return username; }
    public String getParola() { return parola; }
}
