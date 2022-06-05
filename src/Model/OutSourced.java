package Model;

public class OutSourced extends Part {

    private String companyName;

    /**
     * constructor of the the Outsourced extension of the part method.
     * @param id part ID
     * @param name part name
     * @param price part price
     * @param stock part stock
     * @param min minimum inventory level
     * @param max maximum inventory level
     * @param companyName Name of the company that produced this part
     */
    public OutSourced(int id, String name, double price, int stock, int min, int max, String companyName) {

        super(id, name, price, stock, min, max);

           setCompanyName(companyName);
    }

    /**
     * returns the company name
     * @return the company name of this part
     */
    public String getCompanyName() { return companyName;
    }

    /**
     * sets the company name of the product
     * @param companyName Company name passed to this method by textfield in the add or modify part screen
     */
    public void setCompanyName(String companyName) {
        this.companyName =  companyName;
    }
}
