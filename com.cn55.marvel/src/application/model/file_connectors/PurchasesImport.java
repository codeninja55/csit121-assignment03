package application.model.file_connectors;

import application.model.Generator;
import application.model.category.Category;
import application.model.dao.DataStoreDAO;
import application.model.exceptions.ImportException;
import application.model.purchase.Purchase;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PurchasesImport extends Throwable implements ImportFromCSV {
    private final Pattern categoryRegex = Pattern.compile("\\[(.*?)]");

    public void executeImport(DataStoreDAO dataStore, BufferedReader reader) throws ImportException, IOException {
        if (reader.lines().count() == 0) {
            reader.close();
            throw new ImportException("PurchasesStorage.csv is empty", this);
        } else {
            reader.lines().forEach(line -> {
                HashMap<Integer, Category> categories = new HashMap<>();
                String categoriesAttr[];
                String[] readLine = line.split(CSV.DEFAULT_SEPARATOR_STR, 6);
                int receiptID = Integer.parseInt(readLine[2]);
                // readLine[0] = purchaseTime | [1] = receiptID | [2] = cardType | [3] = cardID
                // [4] = categoriesMap --> {[Category],[Category 2],..[Category n]}
                // Get the substring between the { } --> [Category],[Category 2],..[Category n]
                String categoriesLine = readLine[5].substring(1, readLine[5].lastIndexOf("}"));
                Matcher categoryRegexMatch = categoryRegex.matcher(categoriesLine);
                while(categoryRegexMatch.find()) {
                    // Match the string with the pattern defined to extract a group [...] then
                    // get a substring of that to remove the [ ].
                    String categoryStrObj = categoryRegexMatch.group().substring(1, categoryRegexMatch.group().lastIndexOf("]"));
                    // The string is now just id,name,description,amount which can be split on the delimiter
                    // to put into an array to be used.
                    categoriesAttr = categoryStrObj.split(CSV.DEFAULT_SEPARATOR_STR);
                    categories.put(Integer.parseInt(categoriesAttr[0]),
                            new Category(Integer.parseInt(categoriesAttr[0]), categoriesAttr[1],
                                    categoriesAttr[2], Double.parseDouble(categoriesAttr[3])));
                }

                Purchase importedPurchase = new Purchase(readLine[1], receiptID, readLine[3], readLine[4], categories);
                Generator.addReceiptID(receiptID);
                dataStore.createPurchase(importedPurchase);
            });
            reader.close();
        }
    }
}
