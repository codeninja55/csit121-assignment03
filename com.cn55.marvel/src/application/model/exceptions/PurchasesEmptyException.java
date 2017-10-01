package application.model.exceptions;

public class PurchasesEmptyException extends Exception {
    private String message;

    public PurchasesEmptyException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
