package Entity;

public class Produs extends Entity {
    private String denumire;
    private float pret;
    private int stoc;

    public Produs(int id, String denumire, float pret, int stoc) {
        super(id);
        this.denumire = denumire;
        this.pret = pret;
        this.stoc = stoc;
    }

    public String getDenumire() {
        return denumire;
    }
    public float getPret() { return pret; }
    public int getStoc() { return stoc; }

    @Override
    public String toString() {
        return "Produs{" + "id=" + id + ",denumire=" + denumire + ",stoc=" + stoc + '}';
    }
}