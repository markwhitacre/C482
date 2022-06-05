package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Inventory {

    private static final ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static final ObservableList<Product> allProducts = FXCollections.observableArrayList();

    /**
     * adds a part to the master parts list
     * @param newPart the part to be added
     */
    public static void addPart(Part newPart){
        allParts.add(newPart);

    }

    /**
     * adds a product to the master product list
     * @param newProduct the product to be added
     */
    public static void addProduct(Product newProduct){
        allProducts.add(newProduct);

    }

    /**
     * searches for the product ID in the master parts list
     * @param partID ID of the part that is being searched for
     * @return the part that is found
     */
    public static Part lookupPart(int partID){
        return null;
    }

    /**
     * searches for the product ID in the master product list
     * @param productID the product being searched for
     * @return the product that was found
     */
    public static Product lookupProduct(int productID){

        return null;
    }

    /**
     * updates the parts information based on the information
     *
     * @param index part ID number
     * @param selectedPart Part to be updated
     */
    public static void updatePart(int index, Part selectedPart){

        for (int i = 0; i < getallParts().size(); i++){
            if (index == getallParts().get(i).getId()){
                allParts.set(i, selectedPart);
            }
        }

    }

    /**
     * updates the product that is called with the new Product
     *
     * @param index Product ID number
     * @param newProduct Product that will be updated
     */
    public static void updateProduct(int index, Product newProduct){

        for (int i = 0; i < getallProducts().size(); i++){
            if (index == getallProducts().get(i).getId()){
                allProducts.set(i, newProduct);
            }
        }

    }

    /**
     * removes the part from the master inventory list
     * @param selectedPart the part to be removed
     * @return if the part was removed, returns true
     */
    public static boolean deletePart(Part selectedPart){

        allParts.remove(selectedPart);

        return true;
    }

    /**
     * removes the product from the master inventory list
     * @param selectedProduct the product to be removed
     * @return true if the product was removed
     */
    public static boolean deleteProduct(Product selectedProduct){

        allProducts.remove(selectedProduct);

        return true;


    }

    /**
     * public view of the master parts list
     * @return the master parts list
     */
    public static ObservableList<Part> getallParts() {

        return allParts;
    }

    /**
     * Public view of the master product list
     * @return master product list
     */
    public static ObservableList<Product> getallProducts() {

        return allProducts;
    }
}

