package Controller;

import Model.Inventory;
import Model.Part;
import Model.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

import static Model.Inventory.getallParts;


public class ModifyProductController {

    Product product;

    @FXML
    private TextField productMin;

    @FXML
    private TextField productName;

    @FXML
    private TextField searchProduct;

    @FXML
    private TextField productID;

    @FXML
    private TextField productInv;

    @FXML
    private TextField productPrice;

    @FXML
    private TextField productMax;

    @FXML
    private TableView<Part> allPartsTable;

    @FXML
    private TableColumn<Part, Integer> displayPartID;

    @FXML
    private TableColumn<Part, String> displayPartName;

    @FXML
    private TableColumn<Part, Integer> displayPartInv;

    @FXML
    private TableColumn<Part, Double> displayPartPrice;

    @FXML
    private TableView<Part> associatedPartsTable;

    @FXML
    private TableColumn<Part, Integer> removePartID;

    @FXML
    private TableColumn<Part, String> removePartName;

    @FXML
    private TableColumn<Part, Integer> removePartInvLevel;

    @FXML
    private TableColumn<Part, Double> removePartPrice;

    @FXML
    private Button saveProduct;


    private final ObservableList<Part> partObservableList = FXCollections.observableArrayList();

    /**
     * this function is called when coming to the screen. It sets the product information to the appropriate text fields

     * @param product this is the product that was selected on the main screen
     */
    void setProductToModify(Product product) {
        this.product = product;

        if (product != null) {

            productID.setText(Integer.toString(product.getId()));
            productInv.setText(Integer.toString(product.getStock()));
            productMax.setText(Integer.toString(product.getMax()));
            productMin.setText(Integer.toString(product.getMin()));
            productName.setText(product.getName());
            productPrice.setText(Double.toString(product.getPrice()));
            associatedPartsTable.setItems(product.getAllAssociatedParts());
            allPartsTable.setItems(Inventory.getallParts());

            displayPartID.setCellValueFactory(new PropertyValueFactory<>("id"));
            displayPartName.setCellValueFactory(new PropertyValueFactory<>("name"));
            displayPartInv.setCellValueFactory(new PropertyValueFactory<>("stock"));
            displayPartPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

            removePartID.setCellValueFactory(new PropertyValueFactory<>("id"));
            removePartName.setCellValueFactory(new PropertyValueFactory<>("name"));
            removePartInvLevel.setCellValueFactory(new PropertyValueFactory<>("stock"));
            removePartPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        }
    }

    /**
     * checks if the product has already been added to this product.
     * @return true if the part was already added to this product
     */
    private boolean alreadyAdded() {

        for (int i = 0; i < associatedPartsTable.getItems().size(); i++) {

            if (associatedPartsTable.getItems().get(i) == (allPartsTable.getSelectionModel().getSelectedItem())) {

                return true;
            }
        } return false;
    }
    /**
     * This will associate the part with the product if it is not already associated with the part. It will call the error
     * if the part is already associated.
     * @param event the add button is clicked on the list of all parts in the add product pane
     */
    @FXML
    void addPartToProduct(MouseEvent event) {

        if (!alreadyAdded()) {

            Part selectedPart = allPartsTable.getSelectionModel().getSelectedItem();
            partObservableList.add(selectedPart);
            associatedPartsTable.setItems(partObservableList);
        }
        else{
            errorCode(4);
        }
    }
    /**
     * Sends a confirmation to the user to cancel the the product addition. If "ok" is clicked, the add product
     * screen is closed and the main screen is opened
     * @param event the cancel button is clicked on the add product screen
     * @throws IOException the main screen cannot be loaded
     */
    @FXML
    void cancelProductModify(MouseEvent event) throws IOException {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Are You Sure?");
        alert.setHeaderText("Are you sure you would like to cancel?");
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/MainScreen.fxml"));
            Controller.MainScreenController controller = new Controller.MainScreenController();
            loader.setController(controller);
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } else {
            alert.close();
        }

    }
    /**
     * asks if the user wants to disassociate the part from the product, and removes it if the user confirms
     * generates error codes if there are no parts to remove
     * @param event the remove button is clicked on the list of associated parts on the add product page.
     */
    @FXML
    void removePartFromProduct(MouseEvent event) {

        if (associatedPartsTable.getItems().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("There are no Parts to Remove");
            alert.showAndWait();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Are You Sure?");
        alert.setHeaderText("Are you sure you would like to remove this part?");
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) {

            Part part = associatedPartsTable.getSelectionModel().getSelectedItem();
            partObservableList.remove(part);
            product.deleteAssociatedPart(part);
        } else {
            alert.close();
        }


    }
    /**
     * runs the error check, and adds the product to the product inventory list, and associates the parts with the
     * product if it passes the check. The main screen then loads
     * @param event save is clicked on the add product screen
     * @throws IOException the main screen fails to load
     */

    @FXML
    void saveProduct(MouseEvent event) throws IOException {

        checkForErrors();

        if (checkForErrors()) {
        }
        else {

            Product product = new Product(
                    Integer.parseInt(productID.getText()),
                    productName.getText(),
                    Double.parseDouble(productPrice.getText()),
                    Integer.parseInt(productInv.getText()),
                    Integer.parseInt(productMin.getText()),
                    Integer.parseInt(productMax.getText()));

            for (Part part : partObservableList) {
                product.addAssociatedPart(part);
            }

            Inventory.updateProduct(Integer.parseInt(productID.getText()), product);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/MainScreen.fxml"));
            Controller.MainScreenController controller = new Controller.MainScreenController();
            loader.setController(controller);
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        }
    }

    /**
     * This is the search function for the parts list. If the search text matches the part ID or is contained
     * int the part name, it will filter the list to those parts and display them.
     * @return True if the part is found
     */
    private boolean foundPart() {
        ObservableList<Part> partList = FXCollections.observableArrayList();
        String searchPart = searchProduct.getText();

        for (int i = 0; i < getallParts().size(); i++) {

            if ((getallParts().get(i).getName().contains(searchPart))
                    || ((String.valueOf(getallParts().get(i).getId())).equals(searchPart))) {

                Part found = Inventory.getallParts().get(i);
               allPartsTable.getSelectionModel().select(found);

                partList.add(found);
                allPartsTable.setItems(partList);

                return true;
            }
        }
        return false;
    }
    /**
     * this runs a few checks before calling the foundpart method. If the field is empty, it will populate all of the parts
     * if the parts inventory is empty, it will call an error message.
     *
     * the foundproduct method will populate the tableview with the found parts if they are found, and this method
     * will generate an error that the part was not found.
     *
     * @param event the enter button is pressed when the mouse is in the search text box.
     */
    @FXML
    void searchProduct(ActionEvent event) {
        if (allPartsTable.getItems().isEmpty()) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("There are no parts to search");
            alert.setTitle("Empty List");
            alert.showAndWait();
            return;

        } else if (searchProduct.getText().isEmpty()) {

            allPartsTable.setItems(Inventory.getallParts());
            return;
        }

        foundPart();

        if (!foundPart()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Part not found");
            alert.setTitle("not found");
            alert.showAndWait();
        }
    }

    /**
     * This is a switch method that will generate pop up alerts found by the data checks.
     *
     * @param errorCode the error code number that is passed from the data checks
     */
    private void errorCode(int errorCode) {

        switch (errorCode) {

            case (1): {

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Minimum Stock cannot be greater than Maximum Stock");
                alert.showAndWait();
                break;
            }
            case (2): {

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Inventory should be between Min and Max");
                alert.showAndWait();
                break;

            }
            case (3): {

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Part is already added");
                alert.showAndWait();
                break;
            }
            case (4): {

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Part is already associated with Product");
                alert.showAndWait();
                break;

            }

            case (5): {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Information Missing");
                alert.showAndWait();
                break;
            }

            case (6): {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Invalid Information");
                alert.showAndWait();
                break;
            }
        }
    }

    /**
     * Checks for errors during the attempt to add a product. Searches for a number of check conditions, including:
     * whether or not a product is already added
     * if stock is over Max or under Min
     * if Min is greater than Max
     * if part of the addition is empty or null
     * if found, it will call an error code.
     *
     * @return returns true if one of the search conditions is found
     */
    private boolean checkForErrors() {
        TextField[] fields = {productID, productName, productPrice, productMax, productMin, productInv};

        try {
            int x = (Integer.parseInt(productID.getText()))
                    | (Integer.parseInt(productInv.getText()))
                    | (Integer.parseInt(productMax.getText()))
                    | (Integer.parseInt(productMin.getText()));
        } catch (NumberFormatException n) {
            errorCode(6);
            return true;
        }

        try {
            Double x = Double.parseDouble(productPrice.getText());
        }

        catch (NumberFormatException e) {
            errorCode(6);
            return true;
        }


        for (TextField search : fields) {
            if ((search.getText().isEmpty()) | ((search.getText().isBlank())) | ((search.getText() == null))) {
                errorCode(5);
                return true;
            }

        }

        if (((Integer.parseInt(productMax.getText())) < Integer.parseInt(productMin.getText()))){
            errorCode(1);
            return true;
        }

        else if (((Integer.parseInt(productMin.getText())) > (Integer.parseInt(productInv.getText())))
                | (Integer.parseInt(productMax.getText())) < (Integer.parseInt(productInv.getText()))){
            errorCode(2);
            return true;
        }
        return false;
    }

}

