package Model;

public class InHouse extends Part {

    private int machineID;

    /**
     * this is the constructor for the Inhouse extension of the Part model
     * @param id the part ID
     * @param name the part name
     * @param price price of the part
     * @param stock inventory level of the part
     * @param min minimum stock of the part in the warehouse
     * @param max maximum stock of the part in the warehouse
     * @param machineID Unique ID used as an index to find the product
     */
    public InHouse(int id, String name, double price, int stock, int min, int max, int machineID) {

        super(id, name, price, stock, min, max);
        setMachineID(machineID);
    }

    /**
     * returns the machine ID of this part
     * @return the machine ID from this part
     */
    public int getMachineID() {
        return machineID;
    }

    /**
     * sets the ID of this part
     * @param machineID the machine ID passed to it from the MachineID text field in the part add or modify screen
     */
    public void setMachineID(int machineID) {

        this.machineID = machineID;

    }
}
