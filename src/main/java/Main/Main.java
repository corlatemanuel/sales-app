package Main;

import Service.*;
import Repo.*;
import Entity.*;

import javafx.application.Application;
import GUI.*;

public class Main {
    public static void main(String[] args) {
        Repository<Produs> entRepo = RepositoryFactory.createRepository("sql");

        ServiceProduse serviceProduse = new ServiceProduse(entRepo);

        GUI gui = new GUI(serviceProduse); Application.launch(GUI.class, args);
        //old gui = new old(serviceProduse); Application.launch(old.class, args);

    }
}