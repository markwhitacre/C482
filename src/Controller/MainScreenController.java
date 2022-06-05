package Controller;

import Model.Inventory;
import Model.Part;
import Model.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import static Model.Inventory.getallParts;
import static Model.Inventory.getallProducts;

public class MainScreenController implements Initializable {

    @FXML
    private Button exitMainFormButton;

    @FXML
    private Button addPartButton;

    @FXML
    private Button modifyPartButton;

    @FXML
    private Button deletePartButton;

    @FXML
    private TableView<Part> partsTable;

    @FXML
    private TableColumn<Part, Integer> partIDColumn;

    @FXML
    private TableColumn<Part, String> partNameColumn;

    @FXML
    private TableColumn<Part, Integer> partInventoryLevelColumn;

    @FXML
    private TableColumn<Part, Double> partPricePerUnitColumn;

    @FXML
    private TextField partsSearchBox;

    @FXML
    private Button partsSearchButton;

    @FXML
    private Button addProductButton;

    @FXML
    private Button modifyProductButton;

    @FXML
    private Button deleteProductButton;

    @FXML
    private TextField productSearchBox;

    @FXML
    private TableView<Product> productTable;

    @FXML
    private TableColumn<Product, Integer> productIDColumn;

    @FXML
    private TableColumn<Product, String> productNameColumn;

    @FXML
    private TableColumn<Product, Integer> productInventoryLevelColumn;

    @FXML
    private TableColumn<Product, Double> productPricePerUnitColumn;

    @FXML
    private Button productSearchButton;


    public MainScreenController() {
    }

    /**
     * This loads the main screen and calls the prep statement setPartToAdd,
     * which generates a part ID and sets it in the ID field on the next screen
     * @param event the add button in the parts pane is clicked
     * @throws IOException the add page is unable to load
     */
    @FXML
    void addPart(MouseEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AddPart.fxml"));
        AddPartController controller = new AddPartController();
        loader.setController(controller);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();

        AddPartController controller1 = loader.getController();
        controller1.setPartToAdd();
    }

    /**
     * loads the add product screen and calls for a part number to be generated and set in the ProductID text field on the next screen
     * @param event the add button in the parts pane is clicked
     * @throws IOException the add product pane fails to load
     */
    @FXML
    void addProduct(MouseEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AddProduct.fxml"));
        AddProductController controller = new AddProductController();
        loader.setController(controller);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();

        AddProductController controller1 = loader.getController();
        controller1.setProductToAdd();


    }

    @FXML
    void clearText(MouseEvent event) {

    }

    /**
     * Performs several checks on the selected part, generates errors if there are not parts or no part is selected
     * then prompts the user to confirm that they want the part deleted. If they confirm, the part is removed.
     * @param event the delete button in the parts pane is clicked.
     */
    @FXML
    void deletePart(MouseEvent event) {

        if (partsTable.getItems().isEmpty()) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Are You Sure?");
            error.setHeaderText("There are no parts to delete");
            error.showAndWait();
        } else if (partsTable.getSelectionModel().getSelectedItem() == null) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Cannot Delete Part");
            error.setHeaderText("You did not select a part");
            error.showAndWait();
        } else {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Are You Sure?");
            alert.setHeaderText("Are you sure you would like to delete this part?");
            alert.showAndWait();

            if (alert.getResult() == ButtonType.OK) {

                Part noMore = partsTable.getSelectionModel().getSelectedItem();
                Inventory.deletePart(noMore);
                partsTable.refresh();
            } else {
                alert.close();
            }
        }

    }
    /**
     * Performs several checks on the selected product, generates errors if there are not products or no product is selected
     * this also runs a check to validate that there is are no parts associated with the product selected
     * then prompts the user to confirm that they want the part deleted. If they confirm, the part is removed.
     * @param event the delete button in the products pane is clicked.
     */
    @FXML
    void deleteProduct(MouseEvent event) {
        Product product = productTable.getSelectionModel().getSelectedItem();

        if (productTable.getItems().isEmpty()) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Error");
            error.setHeaderText("There are no products to delete");
            error.showAndWait();
        } else if (productTable.getSelectionModel().getSelectedItem() == null) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Error");
            error.setHeaderText("You did not select a product");
            error.showAndWait();
        } else if (product.getAllAssociatedParts().size() > 0) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Cannot Delete Product");
            error.setHeaderText("Cannot delete a part with associated products");
            error.showAndWait();

        } else {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Are You Sure?");
            alert.setHeaderText("Are you sure you would like to delete this product?");
            alert.showAndWait();

            if (alert.getResult() == ButtonType.OK) {

                Product deleteMe = productTable.getSelectionModel().getSelectedItem();
                Inventory.deleteProduct(deleteMe);
                productTable.refresh();
            } else {
                alert.close();
            }
        }
    }

    /**
     * creates a confirmation that the user would like to exit the program. if "ok" is clicked, the program exits.
     * if the user cancels , the alert closes.
     * @param event the exit button on the main screen is clicked;
     */
    @FXML
    void exitProgram(MouseEvent event) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit Program?");
        alert.setHeaderText("Would you like to Exit?");
        alert.showAndWait();

        if (alert.getResult() == ButtonType.OK) {
            System.exit(0);
        } else {
            alert.close();
        }
    }

    /**
     * Validates that there is a part selected and creates an error if there is no part selected
     * if there is one selected, loads the modify screen and calls for the modify controller to load the part information
     * into the appropriate text fields.
     * @param event the modify button is pressed in the parts pane.
     * @throws IOException the modify part page does not load properly
     */
    @FXML
    void modifyPart(MouseEvent event) throws IOException {


        if (partsTable.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("No Part Selected");
            alert.setTitle("You did not select a Part");
            alert.showAndWait();
        } else {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ModifyPart.fxml"));
            ModifyPartController controller = new ModifyPartController();
            loader.setController(controller);
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();

            ModifyPartController controller1 = loader.getController();
            Part part = partsTable.getSelectionModel().getSelectedItem();
            controller1.setPartToModify(part);
        }

    }

    /**
     * Validates that there is a product selected and creates an error if one is not
     * if there is one selected, loads the modify screen and calls for the modify controller to load the product information
     * into the appropriate text fields.
     * @param event the modify button is pressed in the products pane.
     * @throws IOException the modify product page does not load properly
     */
    @FXML
    void modifyProduct(MouseEvent event) throws IOException {

        if (productTable.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("No Product Selected");
            alert.setTitle("You did not select a Product");
            alert.showAndWait();
        } else {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ModifyProduct.fxml"));
            ModifyProductController controller = new ModifyProductController();
            loader.setController(controller);
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();

            ModifyProductController controller1 = loader.getController();
            Product product = productTable.getSelectionModel().getSelectedItem();
            controller1.setProductToModify(product);
        }

    }

    /**
     * takes the input from the search box and runs a check to see if the text entered matches the ID number or
     * is contained in the part name.
     * @return if the information is found, returns true. otherwise, returns false.
     */
    private boolean foundPart() {
        ObservableList<Part> partList = FXCollections.observableArrayList();
        String searchPart = partsSearchBox.getText();

        for (int i = 0; i < getallParts().size(); i++) {

            if ((getallParts().get(i).getName().contains(searchPart))
                    || ((String.valueOf(getallParts().get(i).getId())).equals(searchPart))) {

                Part found = Inventory.getallParts().get(i);
                partsTable.getSelectionModel().select(found);

                partList.add(found);
                partsTable.setItems(partList);

                return true;
            }
        } return false;
    }

    /**
     * validates that there are parts in the parts table and generates an error if it is empty.
     * re-populates the table if the search box is empty.
     * runs the "foundPart" method, and if it returns as true, loads those parts in to a table, which is then passed to
     * the table view that the user sees.
     * @param event the return key is pressed while the mouse cursor is in the search box on the parts pane.
     */
    @FXML
    void searchPart(ActionEvent event) {

        if (partsTable.getItems().isEmpty()) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("There are no parts to search");
            alert.setTitle("Empty List");
            alert.showAndWait();
            return;

        } else if (partsSearchBox.getText().isEmpty()) {

            partsTable.setItems(Inventory.getallParts());
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
     * takes the input from the search box and runs a check to see if the text entered matches the ID number or
     * is contained in the product name.
     * @return if the information is found, returns true. otherwise, returns false.
     *
     *
     * I think a great addition here would be to add a search function that would allow you to search for products that
     * contain certain parts. The use case for this would be if you needed to recall or upgrade a particular part, and
     * need to find every product that part is involved in.
     */
    private boolean foundProduct() {
        ObservableList<Product> productList = FXCollections.observableArrayList();
        String searchProduct = productSearchBox.getText();

        for (int i = 0; i < getallProducts().size(); i++) {

            if ((getallProducts().get(i).getName().contains(searchProduct))
                    || ((String.valueOf(getallProducts().get(i).getId())).equals(searchProduct))) {

                Product found = Inventory.getallProducts().get(i);
                productTable.getSelectionModel().select(found);

                productList.add(found);
                productTable.setItems(productList);

                return true;
            }
        } return false;
    }
    /**
     * validates that there are products in the products table and generates an error if it is empty.
     * re-populates the table if the search box is empty.
     * runs the "foundProduct" method, and if it returns as true, loads those products in to a table, which is then passed to
     * the table view that the user sees.
     * @param event the return key is pressed while the mouse cursor is in the search box on the product pane.
     */

    @FXML
    void searchProduct(ActionEvent event) {
        {

            if (productTable.getItems().isEmpty()) {

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("There are no products to search");
                alert.setTitle("Empty List");
                alert.showAndWait();
                return;

            } else if (productSearchBox.getText().isEmpty()) {

                productTable.setItems(Inventory.getallProducts());
                return;
            }

            foundProduct();

            if (!foundProduct()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Product not found");
                alert.setTitle("not found");
                alert.showAndWait();
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        partsTable.setItems(getallParts());
        productTable.setItems(getallProducts());

        partIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInventoryLevelColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPricePerUnitColumn.setCellValueFactory(new PropertyValueFactory<>("price"));



        productIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        productInventoryLevelColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productPricePerUnitColumn.setCellValueFactory(new PropertyValueFactory<>("price"));




    }
}
