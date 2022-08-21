package com.service;

import com.model.*;
import com.repository.InvoiceRepository;
import com.exceptions.ApplianceReadException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ShopService {
    private static final InvoiceRepository INVOICE_REPOSITORY = new InvoiceRepository();
    private static final Pattern APPLIANCE_PATTERN = Pattern.compile("([a-zA-Z0-9-. ]*),([a-zA-Z0-9-. ]*),([a-zA-Z0-9-. ]*)," +
            "([a-zA-Z0-9-. ]*),([a-zA-Z0-9-. ]*),([a-zA-Z0-9-. ]*),([a-zA-Z0-9-. ]*)");
    private static final Pattern ALL_DATA_PATTERN = Pattern.compile("[a-zA-Z0-9-]+");
    private static final String NUMBER_REGEX = "[0-9]+[\\.]?[0-9]*";
    private static final Float DIAGONAL_PRICE_BOUND = 100F;
    private static final Random RANDOM = new Random();
    private final Map<String,String> PRODUCT_PROPERTIES = new HashMap<>();
    private static List<Appliance> appliancesFromResources;
    private static List<String> models;
    private static List<String> series;
    private static List<String> countries;
    private static List<String> screenTypes;

    public ShopService() {
        ShopService.appliancesFromResources = getApplianceFromCSV();
    }

    public boolean saveInvoice(Invoice invoice) {
        INVOICE_REPOSITORY.save(invoice);
        return true;
    }

    public void clearInvoices() {
        INVOICE_REPOSITORY.clear();
    }

    public List<Invoice> getAllInvoices() {
        return INVOICE_REPOSITORY.getInvoices();
    }

    private void clearProductPropertiesMap() {
        PRODUCT_PROPERTIES.put("type", "none");
        PRODUCT_PROPERTIES.put("series", "none");
        PRODUCT_PROPERTIES.put("model", "none");
        PRODUCT_PROPERTIES.put("diagonal", "0");
        PRODUCT_PROPERTIES.put("screen type", "none");
        PRODUCT_PROPERTIES.put("country", "none");
        PRODUCT_PROPERTIES.put("price", "0");
    }

    private Optional<String> getPropertyName(String property) {
        if(isType(property)) return Optional.of("type");
        if(isSeries(property)) return Optional.of("series");
        if(isModel(property)) return Optional.of("model");
        if(isDiagonal(property)) return Optional.of("diagonal");
        if(isScreenType(property)) return Optional.of("screen type");
        if(isCountry(property)) return Optional.of("country");
        if(isPrice(property)) return Optional.of("price");
        return Optional.empty();
    }

    public Invoice generateRandomInvoice(Customer customer) {
        int numberOfProducts = RANDOM.nextInt(1,5);
        List<Appliance> invoiceProducts = new ArrayList<>();
        for(int i = 0; i < numberOfProducts; i++) {
            invoiceProducts.add(appliancesFromResources.get(RANDOM.nextInt(0,appliancesFromResources.size()-1)));
        }
        return new Invoice(invoiceProducts, customer);
    }

    private List<Appliance> getApplianceFromCSV() {
        List<Appliance> dataFromCSV = new LinkedList<>();
        resourcesInit();
        clearProductPropertiesMap();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream productsAsInputStream = loader.getResourceAsStream("Products.csv");
        try (final InputStreamReader streamReader = new InputStreamReader(productsAsInputStream);
             final BufferedReader fileBufferedReader = new BufferedReader(streamReader)) {
            String line;
            while ((line = fileBufferedReader.readLine()) != null) {
                final Matcher matcher = APPLIANCE_PATTERN.matcher(line);
                if (matcher.find()) {
                    try {
                        for (int i = 1; i <= 7; i++) {
                            if (matcher.group(i).equals(""))
                                throw new ApplianceReadException(line);

                            Optional<String> propertyName = getPropertyName(matcher.group(i));
                            if (propertyName.isPresent()) {
                                PRODUCT_PROPERTIES.put(propertyName.get(), matcher.group(i));
                            }
                        }
                    } catch (ApplianceReadException e) {
                        System.out.println(e);
                        clearProductPropertiesMap();
                        continue;
                    }
                }
                if (PRODUCT_PROPERTIES.get("type").equals("Television")) {
                    dataFromCSV.add(new Television(
                            PRODUCT_PROPERTIES.get("series"),
                            Float.parseFloat(PRODUCT_PROPERTIES.get("diagonal")),
                            PRODUCT_PROPERTIES.get("model"),
                            PRODUCT_PROPERTIES.get("screen type"),
                            PRODUCT_PROPERTIES.get("country"),
                            new BigDecimal(PRODUCT_PROPERTIES.get("price"))));
                } else if (PRODUCT_PROPERTIES.get("type").equals("Telephone")) {
                    dataFromCSV.add(new Telephone(
                            PRODUCT_PROPERTIES.get("series"),
                            PRODUCT_PROPERTIES.get("model"),
                            PRODUCT_PROPERTIES.get("screen type"),
                            new BigDecimal(PRODUCT_PROPERTIES.get("price"))));
                }
                clearProductPropertiesMap();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dataFromCSV;
    }

    private List<String> getAllDataFromResource(String fileName) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream csvAsInputStream = loader.getResourceAsStream(fileName);
        List<String> fileData = new ArrayList<>();
        try (final InputStreamReader streamReader = new InputStreamReader(csvAsInputStream);
             final BufferedReader fileBufferedReader = new BufferedReader(streamReader)) {
            String line;
            while ((line = fileBufferedReader.readLine()) != null) {
                final Matcher matcher = ALL_DATA_PATTERN.matcher(line);
                while (matcher.find()) {
                    fileData.add(matcher.group(0));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileData;
    }

    private void resourcesInit() {
        models = getAllDataFromResource("Brands.csv");
        series = getAllDataFromResource("Series.csv");
        countries = getAllDataFromResource("Countries.csv");
        screenTypes = getAllDataFromResource("Screen Types.csv");
    }

    private boolean isModel(String o) {
        return models.stream()
                .anyMatch(n-> n.equals(o));
    }

    private boolean isSeries(String o) {
        return series.stream()
                .anyMatch(n-> n.equals(o));
    }

    private boolean isCountry(String o) {
        return countries.stream()
                .anyMatch(n-> n.equals(o));
    }

    private boolean isType(String o) {
        return o.equals("Television") || o.equals("Telephone");
    }

    private boolean isScreenType(String o) {
        return screenTypes.stream()
                .anyMatch(n-> n.equals(o));
    }

    private boolean isPrice(String o) {
        return Pattern.matches(NUMBER_REGEX,o) && Float.parseFloat(o) > DIAGONAL_PRICE_BOUND;
    }

    private boolean isDiagonal(String o) {
        return Pattern.matches(NUMBER_REGEX,o) && Float.parseFloat(o) <= DIAGONAL_PRICE_BOUND;
    }
}
