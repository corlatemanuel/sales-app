package GUI;

import Entity.ComandaItem;
import Entity.Produs;
import Entity.Utilizator;
import Repo.ComenziRepository;
import Repo.Repository;
import Repo.RepositoryFactory;
import Repo.UtilizatorRepository;
import Service.ServiceProduse;
import javafx.animation.*;
import javafx.util.Duration;
import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GUI extends Application {

    private ServiceProduse serviceProduse;

    public GUI() {
    }

    public GUI(ServiceProduse serviceProduse) {
        this.serviceProduse = serviceProduse;
    }

    private final ObservableList<Produs> produse = FXCollections.observableArrayList();

    private final ObservableList<ComandaItem> cosCumparaturi = FXCollections.observableArrayList();

    private final ObservableList<String> notificari = FXCollections.observableArrayList();
    private final Set<Integer> produseNotificate = new HashSet<>();


    @Override
    public void start(Stage primaryStage) {
        if (serviceProduse == null) {
            Repository<Produs> entRepo = RepositoryFactory.createRepository("sql");
            serviceProduse = new ServiceProduse(entRepo);
        }
        Connection conn = RepositoryFactory.getSqlConnection();
        UtilizatorRepository utilizatorRepo = new UtilizatorRepository(conn);
        ComenziRepository comenziRepo = new ComenziRepository(conn, serviceProduse);

        primaryStage.setTitle("Magazin de produse smechere");

        // LOGIN
        Label loginLabel = new Label("Conectare:");
        TextField usernameField = new TextField("User");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Parola");
        Button loginButton = new Button("Login");

        VBox loginLayout = new VBox(10, loginLabel, usernameField, passwordField, loginButton);
        loginLayout.setStyle("-fx-alignment: center; -fx-padding: 100;");
        Scene loginScene = new Scene(loginLayout, 600, 400);

        // === PANE PENTRU PRODUSE ===
        TableView<Produs> tabelProduse = new TableView<>(produse);
        updateProduseTableView(tabelProduse);

        // Coloana pentru denumire
        TableColumn<Produs, String> colDenumire = new TableColumn<>("Denumire");
        colDenumire.setCellValueFactory(new PropertyValueFactory<>("denumire"));

        // Coloana pentru pret
        TableColumn<Produs, Float> colPret = new TableColumn<>("Pret");
        colPret.setCellValueFactory(new PropertyValueFactory<>("pret"));
        colPret.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Float item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("%.2f RON", item));
                }
            }
        });


        // Coloana pentru stoc
        TableColumn<Produs, Integer> colStoc = new TableColumn<>("Stoc");
        colStoc.setCellValueFactory(new PropertyValueFactory<>("stoc"));
        colStoc.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item + " buc.");
                }
            }
        });


        tabelProduse.getColumns().addAll(colDenumire, colPret, colStoc);
        tabelProduse.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        TextField cantitateInput = new TextField("1");
        cantitateInput.setPromptText("Cantitate");
        Button adaugaInCosBtn = new Button("Adauga in cos");

        VBox produsePane = new VBox(10, tabelProduse, cantitateInput, adaugaInCosBtn);
        produsePane.setStyle("-fx-padding: 20;");

        // === PANE PENTRU COS ===
        TableView<ComandaItem> tabelCos = new TableView<>(cosCumparaturi);
        TableColumn<ComandaItem, String> colNume = new TableColumn<>("Produs");
        colNume.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDenumire()));

        TableColumn<ComandaItem, Integer> colCantitate = new TableColumn<>("Cantitate");
        colCantitate.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getCantitate()).asObject());

        tabelCos.getColumns().addAll(colNume, colCantitate);
        tabelCos.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        TextField cantitateNouaInput = new TextField();
        cantitateNouaInput.setPromptText("Cantitate noua");

        Button modificaBtn = new Button("Modifica");
        Button stergeBtn = new Button("Sterge");

        TextField numeClient = new TextField();
        numeClient.setPromptText("Nume complet");

        TextField adresaClient = new TextField();
        adresaClient.setPromptText("Adresă");

        TextField telefonClient = new TextField();
        telefonClient.setPromptText("Telefon");

        HBox detaliiClientBox = new HBox(10, numeClient, telefonClient, adresaClient);
        detaliiClientBox.setStyle("-fx-padding: 10;");

        Button plaseazaBtn = new Button("Plaseaza comanda");

        HBox butoaneCos = new HBox(10, modificaBtn, stergeBtn);
        VBox cosPane = new VBox(10, tabelCos, cantitateNouaInput, butoaneCos, detaliiClientBox, plaseazaBtn);
        cosPane.setStyle("-fx-padding: 20;");

        // === PANE PRINCIPAL ===
        BorderPane mainLayout = new BorderPane();
        Scene mainScene = new Scene(mainLayout, 800, 500);

        // Butoane de navigare
        Button btnProduse = new Button("Vizualizare produse");
        Button btnCos = new Button("Vizualizare cos");
        Button btnNotificari = new Button("Notificări");
        HBox topMenu = new HBox(20, btnProduse, btnCos, btnNotificari);
        topMenu.setStyle("-fx-alignment: center; -fx-padding: 20;");
        mainLayout.setTop(topMenu);

        // === PANE PENTRU NOTIFICARI ===
        ListView<String> listaNotificari = new ListView<>(notificari);
        Button refreshStocuriBtn = new Button("Refresh stocuri");

        VBox notificariPane = new VBox(10, listaNotificari, refreshStocuriBtn);
        notificariPane.setStyle("-fx-padding: 20;");

        // Butonul de refresh manual
        refreshStocuriBtn.setOnAction(e -> {
            List<Produs> produse = serviceProduse.toateEntitatile();
            verificaStocuri(produse, true); // true = refresh manual => fără popup
        });

        Timeline autoCheck = new Timeline(
                new KeyFrame(Duration.seconds(10), e -> {
                    List<Produs> produse = serviceProduse.toateEntitatile();
                    verificaStocuri(produse, false); // false = auto => cu popup
                })
        );
        autoCheck.setCycleCount(Timeline.INDEFINITE);
        autoCheck.play();

        // === ACTIUNI ===
        btnProduse.setOnAction(e -> mainLayout.setCenter(produsePane));
        btnCos.setOnAction(e -> mainLayout.setCenter(cosPane));
        btnNotificari.setOnAction(e -> mainLayout.setCenter(notificariPane));

        adaugaInCosBtn.setOnAction(e -> {
            Produs selected = tabelProduse.getSelectionModel().getSelectedItem();
            String cantStr = cantitateInput.getText();
            if (selected != null && !cantStr.isEmpty()) {
                int cant = Integer.parseInt(cantStr);
                if (cant <= selected.getStoc()) {
                    cosCumparaturi.add(new ComandaItem(selected, cant));
                    cantitateInput.setText("1");
                } else {
                    showAlert("Stoc insuficient", "Stocul disponibil este: " + selected.getStoc(), Alert.AlertType.ERROR);
                }
            }
        });


        modificaBtn.setOnAction(e -> {
            ComandaItem selectedItem = tabelCos.getSelectionModel().getSelectedItem();
            String cantNouaStr = cantitateNouaInput.getText();

            if (selectedItem != null && !cantNouaStr.isEmpty()) {
                try {
                    int cantNoua = Integer.parseInt(cantNouaStr);
                    if (cantNoua <= 0) {
                        showAlert("Eroare", "Cantitatea trebuie să fie pozitivă.", Alert.AlertType.ERROR);
                        return;
                    }

                    int stocDisponibil = selectedItem.getProdus().getStoc();
                    if (cantNoua > stocDisponibil) {
                        showAlert("Stoc insuficient", "Maxim disponibil: " + stocDisponibil, Alert.AlertType.ERROR);
                        return;
                    }

                    selectedItem = new ComandaItem(selectedItem.getProdus(), cantNoua);
                    int index = tabelCos.getSelectionModel().getSelectedIndex();
                    cosCumparaturi.set(index, selectedItem);  // actualizează itemul
                    cantitateNouaInput.clear();

                } catch (NumberFormatException ex) {
                    showAlert("Eroare", "Cantitatea trebuie să fie un număr valid.", Alert.AlertType.ERROR);
                }
            }
        });


        stergeBtn.setOnAction(e -> {
            int selectedIndex = tabelCos.getSelectionModel().getSelectedIndex();
            if (selectedIndex >= 0) {
                cosCumparaturi.remove(selectedIndex);
            }
        });

        plaseazaBtn.setOnAction(e -> {
            if (cosCumparaturi.isEmpty()) {
                showAlert("Eroare", "Coșul este gol.", Alert.AlertType.ERROR);
                return;
            }

            if (numeClient.getText().isBlank() || telefonClient.getText().isBlank() || adresaClient.getText().isBlank()) {
                showAlert("Eroare", "Completează toate detaliile de livrare.", Alert.AlertType.ERROR);
                return;
            }

            try {
                comenziRepo.salveazaComanda(
                        numeClient.getText(),
                        adresaClient.getText(),
                        telefonClient.getText(),
                        new ArrayList<>(cosCumparaturi)
                );
                cosCumparaturi.clear();
                showAlert("Succes", "Comanda a fost plasată cu succes!", Alert.AlertType.INFORMATION);
                updateProduseTableView(tabelProduse);
                verificaStocuri(produse, true);
            } catch (SQLException ex) {
                ex.printStackTrace();
                showAlert("Eroare", "Comanda nu a putut fi salvată.", Alert.AlertType.ERROR);
            }
        });


        loginButton.setOnAction(e -> {
            try {
                Utilizator user = utilizatorRepo.autentifica(usernameField.getText(), passwordField.getText());
                if (user != null) {
                    primaryStage.setScene(mainScene);
                    updateProduseTableView(tabelProduse);
                    verificaStocuri(produse, true);
                } else {
                    new Alert(Alert.AlertType.ERROR, "Credentiale incorecte!").showAndWait();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });


        primaryStage.setScene(loginScene);
        primaryStage.show();
    }

    private void verificaStocuri(List<Produs> toateProdusele, boolean manual) {
        for (Produs p : toateProdusele) {
            if (p.getStoc() == 0 && !produseNotificate.contains(p.getId())) {
                String mesaj = "Stoc epuizat: " + p.getDenumire();
                notificari.add(mesaj);
                produseNotificate.add(p.getId());

                if (!manual) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Notificare");
                    alert.setHeaderText(null);
                    alert.setContentText(mesaj);
                    alert.show();
                }
            }

            if (p.getStoc() > 0) {
                produseNotificate.remove(p.getId());
            }
        }
    }


    private void updateProduseTableView(TableView<Produs> tableView) {
        produse.clear();
        produse.addAll(serviceProduse.toateEntitatile());
        tableView.setItems(produse);
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
