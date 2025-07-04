package GUI;

import Entity.Produs;
import Repo.Repository;
import Repo.RepositoryFactory;
import Service.ServiceProduse;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.List;

public class old extends Application {
    private ServiceProduse serviceProduse;

    // Constructorul implicit pentru JavaFX
    public old() {
    }

    // Constructorul cu parametri pentru injectarea dependențelor
    public old(ServiceProduse serviceProduse) {
        this.serviceProduse = serviceProduse;
    }

    @Override
    public void start(Stage primaryStage) {
        if (serviceProduse == null) {
            Repository<Produs> entRepo = RepositoryFactory.createRepository("sql");
            serviceProduse = new ServiceProduse(entRepo);
        }

        primaryStage.setTitle("Gestionare Entitati");

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        VBox menu = new VBox(10);
        menu.setPadding(new Insets(10));

        Button btnObiecte = new Button("Gestionează Obiecte");
        Button btnCeleMaiDesInchiriate = new Button("Cele mai des închiriate Obiecte");
        Button btnInchirieriPeLuna = new Button("Număr închirieri pe lună");
        Button btnZileInchiriere = new Button("Obiectele cu cele mai multe zile de închiriere");

        menu.getChildren().addAll(btnObiecte, btnCeleMaiDesInchiriate, btnInchirieriPeLuna, btnZileInchiriere);

        root.setLeft(menu);

        StackPane centerPane = new StackPane();
        root.setCenter(centerPane);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();

        btnObiecte.setOnAction(e -> gestioneazaObiecte(centerPane));
        btnCeleMaiDesInchiriate.setOnAction(e -> afiseazaCeleMaiDesInchiriateObiecte(centerPane));
        btnInchirieriPeLuna.setOnAction(e -> afiseazaInchirieriPeLuna(centerPane));
        btnZileInchiriere.setOnAction(e -> afiseazaObiecteleInchiriateCelMaiMultTimp(centerPane));
    }

    private void afiseazaCeleMaiDesInchiriateObiecte(StackPane centerPane) {
        VBox pane = new VBox(10);
        pane.setPadding(new Insets(10));

        Label title = new Label("Cele mai des închiriate Obiecte");
        ListView<String> listView = new ListView<>();

//        serviciuInchiriere.celeMaiDesInchiriateObiecte()
//                .forEach((masina, numarInchirieri) ->
//                        listView.getItems().add("Mașina: " + masina + ", Număr închirieri: " + numarInchirieri)
//                );

        pane.getChildren().addAll(title, listView);
        centerPane.getChildren().setAll(pane);
    }

    private void afiseazaInchirieriPeLuna(StackPane centerPane) {
        VBox pane = new VBox(10);
        pane.setPadding(new Insets(10));

        Label title = new Label("Număr închirieri pe lună");
        ListView<String> listView = new ListView<>();

//        serviciuInchiriere.numarInchirieriPeLuna()
//                .forEach((luna, numarInchirieri) ->
//                        listView.getItems().add("Luna: " + luna + ", Număr închirieri: " + numarInchirieri)
//                );

        pane.getChildren().addAll(title, listView);
        centerPane.getChildren().setAll(pane);
    }

    private void afiseazaObiecteleInchiriateCelMaiMultTimp(StackPane centerPane) {
        VBox pane = new VBox(10);
        pane.setPadding(new Insets(10));

        Label title = new Label("Obiectele cu cele mai multe zile de închiriere");
        ListView<String> listView = new ListView<>();

        HBox form = new HBox(10);
        TextField testfield = new TextField();
        testfield.setPromptText("test");
        Button testButton = new Button("Test");
        form.getChildren().addAll(testfield, testButton);
//        serviciuInchiriere.obiecteInchiriateCelMaiMultTimp()
//                .forEach((masina, zile) ->
//                        listView.getItems().add("Mașina: " + masina + ", Zile închiriate: " + zile)
//                );

        pane.getChildren().addAll(title, listView, form);
        centerPane.getChildren().setAll(pane);
    }


    private void gestioneazaObiecte(StackPane centerPane) {
        VBox obiectePane = new VBox(10);
        obiectePane.setPadding(new Insets(10));

        Label title = new Label("Gestionează Obiecte");
        ListView<String> listView = new ListView<>();
        updateObiecteListView(listView);

        // Form pentru adăugare mașină
        HBox form = new HBox(10);
        TextField idField = new TextField();
        idField.setPromptText("ID");
        TextField marcaField = new TextField();
        marcaField.setPromptText("Marca");
        TextField modelField = new TextField();
        modelField.setPromptText("Model");
        Button btnAdd = new Button("Adaugă");
        Button btnEdit = new Button("Modifică");
        form.getChildren().addAll(idField, marcaField, modelField, btnAdd, btnEdit);

        // Buton pentru ștergere mașină
        Button btnDelete = new Button("Șterge Mașină");
        TextField deleteField = new TextField();
        deleteField.setPromptText("ID pentru ștergere");

        // Handlere pentru acțiuni
        btnAdd.setOnAction(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                String denumire = marcaField.getText();
                int stoc = Integer.parseInt(modelField.getText());
                serviceProduse.adaugaEntitate(new Produs(id, denumire, id, stoc));
                updateObiecteListView(listView);
                idField.clear();
                marcaField.clear();
                modelField.clear();
            } catch (Exception ex) {
                showAlert("Eroare", ex.getMessage());
            }
        });

        btnEdit.setOnAction(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                String denumire = marcaField.getText();
                int stoc = Integer.parseInt(modelField.getText());
                serviceProduse.modificaEntitatea(new Produs(id, denumire, id, stoc));
                updateObiecteListView(listView);
                idField.clear();
                marcaField.clear();
                modelField.clear();
            } catch (Exception ex) {
                showAlert("Eroare", ex.getMessage());
            }
        });

        btnDelete.setOnAction(e -> {
            try {
                int id = Integer.parseInt(deleteField.getText());
                serviceProduse.stergeEntitatea(id);
                updateObiecteListView(listView);
                deleteField.clear();
            } catch (Exception ex) {
                showAlert("Eroare", ex.getMessage());
            }
        });

        obiectePane.getChildren().addAll(title, listView, form, deleteField, btnDelete);
        centerPane.getChildren().setAll(obiectePane);
    }

    private void updateObiecteListView(ListView<String> listView) {
        listView.getItems().clear();
        List<Produs> obiecte = serviceProduse.toateEntitatile();
        obiecte.forEach(m -> listView.getItems().add(m.toString()));
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        // Se lansează aplicația JavaFX cu dependențele injectate
        launch(args);
    }
}
