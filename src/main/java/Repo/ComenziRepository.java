package Repo;

import Entity.Produs;
import Entity.ComandaItem;
import Service.ServiceProduse;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ComenziRepository {
    private final Connection conn;
    private final ServiceProduse serviceProduse;

    public ComenziRepository(Connection conn, ServiceProduse serviceProduse) {
        this.conn = conn;
        this.serviceProduse = serviceProduse;
    }
    public void salveazaComanda(String nume, String adresa, String telefon, List<ComandaItem> produse) throws SQLException {
        String sql = "INSERT INTO comenzi (nume, adresa, telefon, data_plasare, detalii) VALUES (?, ?, ?, ?, ?)";

        // Construieste string-ul cu detalii: "produs1 x2, produs2 x1"
        String detalii = produse.stream()
                .map(p -> p.getDenumire() + " x" + p.getCantitate())
                .collect(Collectors.joining(", "));

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nume);
            stmt.setString(2, adresa);
            stmt.setString(3, telefon);
            stmt.setString(4, LocalDateTime.now().toString());
            stmt.setString(5, detalii);
            stmt.executeUpdate();
        }

        for (ComandaItem item : produse) {
            Produs p = item.getProdus();
            int stocNou = p.getStoc() - item.getCantitate();
            Produs actualizat = new Produs(p.getId(), p.getDenumire(), p.getPret(), stocNou);
            try {
                serviceProduse.modificaEntitatea(actualizat);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

    }
}
