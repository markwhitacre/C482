package Controller;

import Model.InHouse;
import Model.Inventory;
import Model.OutSourced;
import Model.Part;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class ModifyPartController {

    Part part;

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

    void loadMainScreen() throws IOException {


    }

    /**
     * this function is called when coming to the screen. It sets the part information to the appropriate text fields
     * and changes last text field label to the appropriate label
     * @param part this is the part that was selected on the main screen
     */
    void setPartToModify(Part part) {

        if( part instanceof InHouse){
            inHouseRadioButton.setSelected(true);
            partID.setText(Integer.toString(part.getId()));
            partInv.setText(Integer.toString(part.getStock()));
            partMax.setText(Integer.toString(part.getMax()));
            partMin.setText(Integer.toString(part.getMin()));
            partName.setText(part.getName());
            partPrice.setText(Double.toString(part.getPrice()));
            companyLabel.setText("Machine ID");
            partCompany.setText(Integer.toString(((InHouse) part).getMachineID()));
        }
         else if (part instanceof OutSourced) {
            partID.setText(Integer.toString(part.getId()));
            partInv.setText(Integer.toString(part.getStock()));
            partMax.setText(Integer.toString(part.getMax()));
            partMin.setText(Integer.toString(part.getMin()));
            partName.setText(part.getName());
            partPrice.setText(Double.toString(part.getPrice()));
            outSourcedRadioButton.setSelected(true);
            companyLabel.setText("Company Name");
            partCompany.setText(((OutSourced) part).getCompanyName());
        }


    }

    /**
     * sends a confirmation to the user to confirm that they wanted to exit. If they click ok, the modify screen
     * closes and the main screen opens. If cancelled, the alert closes.
     * @param event the cancel button is clicked on the screen
     * @throws IOException the main screen is unable to open
     */
        @FXML
    void cancelModifyPart(MouseEvent event) throws IOException {

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

            else{
                alert.close();
            }

    }

    /**
     * this will set the "Machine ID" and "Company name" label to "Machine ID" if the in house radio button is selected.
     * @param event the in house radio button is in the selected state
     */

    @FXML
    void partInHouse(MouseEvent event) {
        companyLabel.setText("Machine ID");

    }

    /**
     *this will set the "machine id" and "company name' label to "company name" if the OutSourced radio button is selected
     * @param event the OutSourced radio button is in the selected state
     */

    @FXML
    void partOutSourced(MouseEvent event) {
        companyLabel.setText("Company Name");

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
     * @param event the save button is clicked on the modify part screen
     * @throws IOException the main screen cannot be loaded
     */
    @FXML
    void saveModifyPart(MouseEvent event) throws IOException {
        checkForErrors();



        if (inHouseRadioButton.isSelected()) {

            if (!checkForErrors()) {
                try { int x = Integer.parseInt(partCompany.getText());}
                catch (NumberFormatException e){
                    errorCode(6);
                    return;
                }
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

            Inventory.updatePart(Integer.parseInt(partID.getText()), inhouse);

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

            Inventory.updatePart(Integer.parseInt(partID.getText()), outSourced);
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
}
