package application.model.exceptions;

public class ImportEmptyException extends Exception {
    private String message;

     public ImportEmptyException(String message) {
         super(message);
         this.message = message;
     }

    @Override
    public String getMessage() {
        return message;
    }
}
