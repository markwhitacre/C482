package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Product {

    private final ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;

    /**
     * Constructor for the product model creates a new instance of product when the parameters are passed to it.
     * @param id product ID
     * @param name Product Name
     * @param price Product Price
     * @param stock current stock of the price
     * @param min minimum stock of the product
     * @param max maximum stock of the product
     */
    public Product(int id, String name, double price, int stock, int min, int max){
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }

    /**
     * gets the product ID
     * @return product id
     */
    public int getId() {
        return id;
    }

    /**
     *sets the ID of this instance of the product
     * @param id the ID parameter passed to it from the add or modify product screen
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * gets the product name
     * @return name of the product
     */
    public String getName() {
        return name;
    }

    /**
     * sets the name of the product
     * @param name the name of the product passed to it from the add or modify product screen
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * gets the product price
     * @return the product price
     */
    public double getPrice() {
        return price;
    }

    /**
     * sets the price of this instnace of the product
     * @param price the price that was sent to it from the add or modify product screen
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * gets the product stock
     * @return current stock of the product
     */
    public int getStock() {
        return stock;
    }

    /**
     * sets the stock level of this instance of the product model
     * @param stock stock level integer passed to it from the add or modify product screen
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * gets the minimum product stock
     * @return minimum stock level
     */
    public int getMin() {
        return min;
    }

    /**
     * sets the minimum stock level for this product
     * @param min minimum stock level
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
     * gets the maxiumum product stock
     * @return maximum stock level
     */
    public int getMax() {
        return max;
    }

    /**
     * sets the maximum part level for this product
     * @param max maximum stock
     */
    public void setMax(int max) {
        this.max = max;
    }

    /**
     * gets all associated parts
     * @return all parts associated with this product
     */
    public ObservableList<Part> getAllAssociatedParts() {
        return associatedParts;
    }

    /**
     * adds a part to the associated parts list
     * @param part the part to be added to this instances associated parts list
     */
    public void addAssociatedPart(Part part) {
        associatedParts.add(part);

    }

    /**
     * removes an associated part from this instances list
     * @param selectedAssociatedPart the part that needs to be removed
     * @return true if the part was removed correctly
     */
    public boolean deleteAssociatedPart(Part selectedAssociatedPart){
        associatedParts.remove(selectedAssociatedPart);
        return true;
    }


}


