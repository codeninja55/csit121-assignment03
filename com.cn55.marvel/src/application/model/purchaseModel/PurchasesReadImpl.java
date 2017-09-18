package application.model.purchaseModel;

import application.model.categoryModel.Category;
import application.model.Generator;
import application.model.ReadCSV;
import application.model.Shop;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PurchasesReadImpl implements ReadCSV {
    private static final String DEFAULT_SEPARATOR = ",";
    private Pattern categoryRegex = Pattern.compile("\\[(.*?)\\]");
    private BufferedReader input;

    public void read(BufferedReader reader) {
        this.input = reader;
        String line;

        try {
            while((line = input.readLine()) != null) {
                HashMap<Integer, Category> categories = new HashMap<>();
                String categoriesAttr[];
                String[] readLine = line.split(DEFAULT_SEPARATOR, 5);
                int receiptID = Integer.parseInt(readLine[1]);

                // readLine[0] = purchaseTime | [1] = receiptID | [2] = cardType | [3] = cardID
                // [4] = categories --> {[Category],[Category 2],..[Category n]}

                // Get the substring between the { } --> [Category],[Category 2],..[Category n]
                String categoriesLine = readLine[4].substring(1, readLine[4].lastIndexOf("}"));
                Matcher categoryRegexMatch = categoryRegex.matcher(categoriesLine);
                while(categoryRegexMatch.find()) {
                    // Match the string with the pattern defined to extract a group [...] then
                    // get a substring of that to remove the [ ].
                    String categoryStrObj = categoryRegexMatch.group().substring(1, categoryRegexMatch.group().lastIndexOf("]"));
                    // The string is now just id,name,description,amount which can be split on the delimiter
                    // to put into an array to be used.
                    categoriesAttr = categoryStrObj.split(DEFAULT_SEPARATOR);
                    categories.put(Integer.parseInt(categoriesAttr[0]),
                                    new Category(Integer.parseInt(categoriesAttr[0]), categoriesAttr[1],
                                            categoriesAttr[2], Double.parseDouble(categoriesAttr[3])));
                }

                Purchase importedPurchase = new Purchase(readLine[0], receiptID, readLine[2], readLine[3], categories);

                Generator.addReceiptID(receiptID);
                Shop.getShopInstance().getDataStore().createPurchase(importedPurchase);
            }
            closeFile();
        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
        }
    }

    public void closeFile() {
        try {
            input.close();
        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
        }
    }
}
