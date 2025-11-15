public class Product {
    private String name;
    private String description;
    private String productID;
    private double cost;

    public static final int NAME_SIZE = 35;
    public static final int DESCRIPTION_SIZE = 75;
    public static final int ID_SIZE = 6;
    public static final int RECORD_SIZE = (NAME_SIZE * 2) + (DESCRIPTION_SIZE * 2) +
            (ID_SIZE * 2) + 8;

    public Product(String name, String description, String productID, double cost) {
        this.name = name;
        this.description = description;
        this.productID = productID;
        this.cost = cost;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    /**
     * Pads a string to the specified length with spaces
     */
    private String padString(String str, int length) {
        if (str.length() > length) {
            return str.substring(0, length);
        }
        StringBuilder sb = new StringBuilder(str);
        while (sb.length() < length) {
            sb.append(" ");
        }
        return sb.toString();
    }

    /**
     * Returns name padded to NAME_SIZE characters
     */
    public String getNameForRandom() {
        return padString(name, NAME_SIZE);
    }

    /**
     * Returns description padded to DESCRIPTION_SIZE characters
     */
    public String getDescriptionForRandom() {
        return padString(description, DESCRIPTION_SIZE);
    }

    /**
     * Returns productID padded to ID_SIZE characters
     */
    public String getProductIDForRandom() {
        return padString(productID, ID_SIZE);
    }

    /**
     * Returns the product's data in CSV format
     */
    public String toCSV() {
        return name + "," + description + "," + productID + "," + cost;
    }

    /**
     * Returns the product's data in JSON format
     */
    public String toJSON() {
        String retString = "";
        final char DQ = '\u0022';
        retString = "{" + DQ + "name" + DQ + ":" + DQ + this.name + DQ + ",";
        retString += " " + DQ + "description" + DQ + ":" + DQ + this.description + DQ + ",";
        retString += " " + DQ + "productID" + DQ + ":" + DQ + this.productID + DQ + ",";
        retString += " " + DQ + "cost" + DQ + ":" + DQ + this.cost + DQ + "}";
        return retString;
    }

    /**
     * Returns the product's data in XML format
     */
    public String toXML() {
        String retString = "";
        retString = "<Product>";
        retString += "<name>" + name + "</name>";
        retString += "<description>" + description + "</description>";
        retString += "<productID>" + productID + "</productID>";
        retString += "<cost>" + cost + "</cost>";
        retString += "</Product>";
        return retString;
    }

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", productID='" + productID + '\'' +
                ", cost=" + cost +
                '}';
    }
}