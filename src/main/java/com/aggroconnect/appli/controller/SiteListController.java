package com.aggroconnect.appli.controller;

import com.aggroconnect.appli.MainApp;
import com.aggroconnect.appli.model.Site;
import com.aggroconnect.appli.service.SiteService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.List;
import java.util.Optional;

public class SiteListController {

    @FXML
    private TableView<Site> siteTableView;
    @FXML
    private TableColumn<Site, String> cityColumn;
    @FXML
    private TableColumn<Site, Void> deleteColumn;

    private final SiteService siteService = new SiteService();

    @FXML
    public void initialize() {
        // Lier les colonnes aux propriétés de l'objet Employee
        cityColumn.setCellValueFactory(cellData -> cellData.getValue().cityProperty());
        
        // gérer la modification d'un élément
        cityColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        cityColumn.setOnEditCommit(event -> {
            Site site = event.getRowValue();
            site.cityProperty().set(event.getNewValue());

            try {
                siteService.updateSite(site, this::loadData);
            } catch (RuntimeException e) {
                showAlert(
                        Alert.AlertType.ERROR,
                        "Erreur",
                        "La mise à jour a échoué",
                        e.getMessage()
                ).showAndWait();
                site.cityProperty().set(event.getOldValue());
            }

            loadData();
        });
        siteTableView.setEditable(true);
        
        // gérer la suppression d'un item
        deleteColumn.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button();
            private final ImageView deleteIcon = new ImageView(new Image(MainApp.class.getResourceAsStream("/com/aggroconnect/appli/images/trash.png")));

            {
                deleteIcon.setFitHeight(20);
                deleteIcon.setFitWidth(20);
                deleteButton.setGraphic(deleteIcon);

                // Ajouter un événement de clic sur le bouton
                deleteButton.setOnAction(event -> {
                    Site site = getTableView().getItems().get(getIndex());
                    deleteSite(site);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                }
            }
        });
        // Charger les données
        loadData();
    }

    private Alert showAlert(Alert.AlertType type, String title, String header, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        return alert;
    }

    private void loadData() {
        // Charger les sites
        List<Site> siteList = siteService.getSites();
        ObservableList<Site> sites = FXCollections.observableArrayList(siteList);
        siteTableView.setItems(sites);
    }

    @FXML
    public void addSite(){
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Ajouter un site");
        dialog.setHeaderText(null);
        dialog.setContentText("Ville du site :");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(city -> {
            Site newSite = new Site(0, city);
            siteService.addSite(newSite, this::loadData);
        });
    }

    @FXML
    public void deleteSite(Site selectedSite) {
        if (selectedSite != null) {
            // Demander confirmation avant de supprimer
            Alert alert = showAlert(Alert.AlertType.CONFIRMATION,
                    "Confirmation de suppression",
                    "Êtes-vous sûr de vouloir supprimer ce site ?",
                    selectedSite.cityProperty().get()
            );

            // Si l'utilisateur confirme la suppression
            if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
                try {
                    siteService.deleteSite(selectedSite);
                    loadData();
                } catch (RuntimeException e) {
                    showAlert(Alert.AlertType.ERROR,
                            "Erreur",
                            "La suppression a échoué",
                            e.getMessage()
                    ).showAndWait();
                }
            }
        }
    }
}
