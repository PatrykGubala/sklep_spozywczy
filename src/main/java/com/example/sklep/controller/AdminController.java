package com.example.sklep.controller;

import com.example.sklep.HelloApplication;
import com.example.sklep.model.DBUtils;
import com.example.sklep.model.Product;
import com.example.sklep.model.SessionManager;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import static com.example.sklep.view.CurrentUserWindow.ADDSELLER;
import static com.example.sklep.view.CurrentWindow.LOGIN;

public class AdminController implements Initializable {

    @FXML
    private Button button_logout;

    @FXML
    private Button button_addSeller;

    @FXML
    private Button button_addProduct;

    @FXML
    private TableView<Product> productsTableView;

    @FXML
    private TableColumn<Product, String> productNameColumn;

    @FXML
    private TableColumn<Product, LocalDate> expirationDateColumn;

    @FXML
    private TableColumn<Product, String> categoryColumn;

    @FXML
    private TableColumn<Product, Integer> quantityColumn;

    @FXML
    private TableColumn<Product, Void> deleteColumn;

    private ObservableList<Product> products;
    @FXML
    private BorderPane mainAdminWindow;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        products = DBUtils.getProductListFromDatabase(false);

        productsTableView.setItems(products);

        productNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProductName()));
        expirationDateColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().expirationDateProperty()));
        categoryColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCategory()));
        quantityColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getQuantity()));

        setDeleteColumnFactory();
    }

    private void setDeleteColumnFactory() {
        deleteColumn.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");

            {
                deleteButton.setOnAction(event -> {
                    Product product = getTableView().getItems().get(getIndex());
                    System.out.println("Delete clicked for: " + product.getProductName());

                    DBUtils.deleteProduct(product);

                    getTableView().getItems().remove(product);

                    getTableView().refresh();
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
    }

    @FXML
    private void handleLogout() {
        System.out.println("Admin logout clicked");
        SessionManager.getInstance().setLoggedInUser(null);
        SessionManager.getInstance().getViewFactory().getCurrentWindowProperty().set(LOGIN);

    }

    @FXML
    private void handleAddSeller() {
        System.out.println("Add Seller clicked");
        SessionManager.getInstance().getViewFactory().getCurrentUserWindowProperty().set(ADDSELLER);

    }

    @FXML
    private void handleAddProduct() {
        System.out.println("Add Product clicked");

        try {
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("order.fxml"));
            Scene scene = new Scene(loader.load());

            Stage addProductStage = new Stage();
            addProductStage.setTitle("Add Product");
            addProductStage.initModality(Modality.APPLICATION_MODAL);
            addProductStage.setScene(scene);


            addProductStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }




    }
}