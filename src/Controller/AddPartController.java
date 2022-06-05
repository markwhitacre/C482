package Controller;

import Model.InHouse;
import Model.Inventory;
import Model.OutSourced;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class AddPartController {


    @FXML
    private RadioButton inHouseRadioButton;

    @FXML
    private ToggleGroup addPartSelection;

    @FXML
    private RadioButton outSourcedRadioButton;

    @FXML
    private TextField partID;

    @FXML
    private TextField partName;

    @FXML
    private TextField partInv;

    @FXML
    private TextField partPrice;

    @FXML
    private TextField partMax;

    @FXML
    private TextField partCompany;

    @FXML
    private TextField partMin;

    @FXML
    private Label companyLabel;

    @FXML
    private Button cancelAddPartButton;

    @FXML
    private Button saveAddPartButton;

    /**
     * Calls for a part number to be generated and sets it in the PartID
     */
    void setPartToAdd() {
        partID.setText(String.valueOf(generatePartID()));
        inHouseRadioButton.setSelected(true);
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
                alert.setHeaderText("Cannot delete Product with associated parts");
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
     * Opens an alert asking the user to confirm that they would like to cancel
     * if "ok" is selected, the alert closes, the add part screen closes,
     * and the main screen opens.
     *
     * @param event the cancel button on the add screen is clicked
     * @throws IOException this exception is thrown if the loader cannot load the main screen
     */
    @FXML
    void cancelAddPart(MouseEvent event) throws IOException {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Are You Sure?");
        alert.setHeaderText("Are you sure you would like to cancel?");
        alert.showAndWait();
        if(alert.getResult() == ButtonType.OK) {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/MainScreen.fxml"));
            Controller.MainScreenController controller = new Controller.MainScreenController();
            loader.setController(controller);
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        }
        else {
            alert.close();
        }

    }

    /**
     * this will set the "Machine ID" and "Company name" label to "Machine ID" if the in house radio button is selected.
     * @param event the in house radio button is in the selected state
     */
    @FXML
    void partInHouse(MouseEvent event) {
        if (inHouseRadioButton.isSelected()) {
            companyLabel.setText("Machine ID");
        }

    }

    /**
     *this will set the "machine id" and "company name' label to "company name" if the OutSourced radio button is selected
     * @param event the OutSourced radio button is in the selected state
     */
    @FXML
    void partOutSourced(MouseEvent event) {
        if (outSourcedRadioButton.isSelected()) {
            companyLabel.setText("Company Name");
        }


    }

    /**
     * Generates a unique part ID by adding 1 to the size of the existing inventory.
     * @return returns a part ID as an integer
     */
    private int generatePartID() {
        return Inventory.getallParts().size() + 1;
    }

    /**
     *This method attempts to save a part. It first runs an error check, which produces an error popup if failed.
     * if that passes and the inhouse radio button is selected, it will run an additional check to make sure that the
     * machine ID is an integer.
     * if that passes, a new inhouse part is created and passed to the inventory add part constructor.
     *
     * there is no additional check for an outsourced part, as a company name can be just about anything. the outsourced
     * part is created and added to the inventory.
     *
     * the main screen is then loaded.
     *
     * @param event the add button is pressed
     * @throws IOException the main screen fails to load
     *
     *
     * I found an error in this code when writing this JavaDocs. I noticed that the additional check was located
     * before the split based on the radio button selection. Located in the spot that it was, it caused the user
     * to be unable to add a part if the company name had a letter in it, which it surely would.
     *
     * After verifying this in the program, I moved it to after the selection but before the addition of a new
     * inhouse program, which then acts appropriately by verifying that the machineid that is entered contains only integers.
     */
    @FXML
    void saveAddPart(MouseEvent event) throws IOException {

        checkForErrors();

        if (!checkForErrors()) {

            if (inHouseRadioButton.isSelected()) {

                try { int x = Integer.parseInt(partCompany.getText());}
                catch (NumberFormatException e){
                    errorCode(6);
                    return;
                }

                InHouse inhouse = new InHouse(
                        Integer.parseInt(partID.getText()),
                        partName.getText(),
                        Double.parseDouble(partPrice.getText()),
                        Integer.parseInt(partInv.getText()),
                        Integer.parseInt(partMin.getText()),
                        Integer.parseInt(partMax.getText()),
                        Integer.parseInt(partCompany.getText()));



                inhouse.setMachineID(Integer.parseInt(partCompany.getText()));

                Inventory.addPart(inhouse);

            } else if (outSourcedRadioButton.isSelected()) {

                OutSourced outSourced = new OutSourced(
                        Integer.parseInt(partID.getText()),
                        partName.getText(),
                        Double.parseDouble(partPrice.getText()),
                        Integer.parseInt(partInv.getText()),
                        Integer.parseInt(partMin.getText()),
                        Integer.parseInt(partMax.getText()),
                        partCompany.getText());
                outSourced.setCompanyName(partCompany.getText());

                Inventory.addPart(outSourced);
            }

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
     * Checks for errors during the attempt to add a part. Searches for a number of check conditions, including:
     * whether or not a part is already added
     * if stock is over Max or under Min
     * if Min is greater than Max
     * if part of the addition is empty or null
     * if found, it will call an error code.
     *
     * @return returns true if one of the search conditions is found
     */
    private boolean checkForErrors() {
        TextField[] fields = {partID, partName, partPrice, partMax, partMin, partCompany};

        try {
            int x = (Integer.parseInt(partID.getText()))
                    | (Integer.parseInt(partInv.getText()))
                    | (Integer.parseInt(partMax.getText()))
                    | (Integer.parseInt(partMin.getText()));
        } catch (NumberFormatException n) {
            errorCode(6);
            return true;
        }

        try {
            Double x = Double.parseDouble(partPrice.getText());
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

        for (int i = 0; i < Inventory.getallParts().size(); i++){
            if (Inventory.getallParts().get(i).getId() == (Integer.parseInt(partID.getText()))
                    | (Inventory.getallParts().get(i).getName().equalsIgnoreCase(partName.getText()))){

                errorCode(3);
                return true;
            }
        }

        if (((Integer.parseInt(partMax.getText())) < Integer.parseInt(partMin.getText()))){
            errorCode(1);
           return true;
        }

        else if (((Integer.parseInt(partMin.getText())) > (Integer.parseInt(partInv.getText())))
                | (Integer.parseInt(partMax.getText())) < (Integer.parseInt(partInv.getText()))){
            errorCode(2);
            return true;
        }
        return false;
     }
}
