package application.controller.validator;

@SuppressWarnings("unused")
public class FormValidData {
    private String cardID;
    private String categoryIDStr;
    private int categoryID;
    private String catValueStr;
    private Double catValue;
    private String email;

    /*============================== CONSTRUCTORS ==============================*/
    public FormValidData() {
        this.cardID = null;
        this.categoryIDStr = null;
        this.catValueStr = null;
        this.catValue = 0D;
        this.email = null;
    }

    /*============================== MUTATORS ==============================*/
    public void setCardID(String cardID) { this.cardID = cardID.toUpperCase(); }
    public void setCategoryIDStr(String categoryIDStr) {
        this.categoryIDStr = categoryIDStr;
    }
    public void setCatValueStr(String catValueStr) {
        this.catValueStr = catValueStr;
    }
    public void setCatValue(Double catValue) {
        this.catValue = catValue;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setCategoryID(int categoryID) { this.categoryID = categoryID; }

    /*============================== ACCESSORS ==============================*/
    public String getCardID() {
        return cardID;
    }
    public String getCatValueStr() {
        return catValueStr;
    }
    public Double getCatValue() {
        return catValue;
    }
    public String getEmail() {
        return email;
    }
    public String getCategoryIDStr() { return categoryIDStr; }
    public int getCategoryID() {
        return categoryID;
    }
}
