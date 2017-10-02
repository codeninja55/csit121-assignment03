package application.model.file_connectors;

import application.model.Generator;
import application.model.category.Category;
import application.model.dao.DataStoreDAO;
import application.model.exceptions.ImportException;
import application.model.purchase.Purchase;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PurchasesImport extends Throwable implements ImportFromCSV {
    private final Pattern categoryRegex = Pattern.compile("\\[(.*?)]");

    public void executeImport(DataStoreDAO dataStore, Path file) throws ImportException, IOException {
        if (Files.lines(file).count() == 0) {
            throw new ImportException("PurchasesStorage.csv is empty", this);
        } else {
            Files.newBufferedReader(file).lines().forEach(line -> {
                HashMap<Integer, Category> categories = new HashMap<>();
                String categoriesArr[];
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
                    categoriesArr = categoryStrObj.split(CSV.DEFAULT_SEPARATOR_STR);
                    categories.put(Integer.parseInt(categoriesArr[1]),
                            new Category(Integer.parseInt(categoriesArr[1]), categoriesArr[2],
                                    categoriesArr[3], Double.parseDouble(categoriesArr[4])));
                }

                Purchase importedPurchase = new Purchase(readLine[1], receiptID, readLine[3], readLine[4], categories);
                Generator.addReceiptID(receiptID);
                dataStore.createPurchase(importedPurchase);
            });
        }
    }
}
