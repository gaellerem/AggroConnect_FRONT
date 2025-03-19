package com.aggroconnect.appli.controller;

import com.aggroconnect.appli.MainApp;
import com.aggroconnect.appli.model.Department;
import com.aggroconnect.appli.service.DepartmentService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Optional;

public class DepartmentListController {
    @FXML
    private TableView<Department> departmentTableView;
    @FXML
    private TableColumn<Department, String> nameColumn;
    @FXML
    private TableColumn<Department, Void> deleteColumn;

    private final ObservableList<Department> departments = FXCollections.observableArrayList();
    private final DepartmentService departmentService = new DepartmentService();

    @FXML
    public void initialize() {
        // liaison de la liste de données à la TableView
        departmentTableView.setItems(departments);

        // lier les colonnes aux propriétés de l'objet Department
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        // gérer la modification d'un item
        nameColumn.setOnEditCommit(event -> {
            Department department = event.getRowValue();
            department.nameProperty().set(event.getNewValue());

            try {
                departmentService.updateDepartment(department, this::loadData);
            } catch (RuntimeException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "La mise à jour a échoué", e.getMessage()).showAndWait();
                department.nameProperty().set(event.getOldValue());
            }
        });

        // gérer la suppression d'un item
        deleteColumn.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button();
            private final ImageView deleteIcon = new ImageView(new Image(MainApp.class.getResourceAsStream("/com/aggroconnect/appli/images/trash.png")));

            {
                deleteIcon.setFitHeight(20);
                deleteIcon.setFitWidth(20);
                deleteButton.setGraphic(deleteIcon);

                // ajouter un événement de clic sur le bouton
                deleteButton.setOnAction(event -> {
                    Department department = getTableView().getItems().get(getIndex());
                    deleteDepartment(department);
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

        departmentTableView.setEditable(true);
        // chargement des données
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
        departments.setAll(departmentService.getDepartments());
    }

    @FXML
    public void addDepartment() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Ajouter un service");
        dialog.setHeaderText(null);
        dialog.setContentText("Nom du service :");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(name -> {
            Department newDepartment = new Department(0, name);
            departmentService.addDepartment(newDepartment, this::loadData);
        });
    }

    @FXML
    public void deleteDepartment(Department selectedDepartment) {
        if (selectedDepartment != null) {
            // Demander confirmation avant de supprimer
            Alert alert = showAlert(Alert.AlertType.CONFIRMATION,
                    "Confirmation de suppression",
                    "Êtes-vous sûr de vouloir supprimer ce service ?",
                    selectedDepartment.nameProperty().get()
            );

            // Si l'utilisateur confirme la suppression
            if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
                try {
                    departmentService.deleteDepartment(selectedDepartment);
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
