package Entity;

public class ComandaItem {
    private Produs produs;
    private int cantitate;

    public ComandaItem(Produs produs, int cantitate) {
        this.produs = produs;
        this.cantitate = cantitate;
    }

    public String getDenumire() {
        return produs.getDenumire();
    }

    public int getCantitate() {
        return cantitate;
    }

    public int getProdusId() {
        return produs.getId();
    }

    public Produs getProdus() {
        return produs;
    }
}
