package ru.akirakozov.sd.refactoring.dao;

import ru.akirakozov.sd.refactoring.entity.Product;
import ru.akirakozov.sd.refactoring.sql.SqlRequestService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ProductDaoImpl implements ProductDao {

    private final SqlRequestService sqlRequestService;

    public ProductDaoImpl(SqlRequestService sqlRequestService) {
        this.sqlRequestService = sqlRequestService;
    }

    @Override
    public void initialize() {
        sqlRequestService.executeUpdate(
                "CREATE TABLE IF NOT EXISTS PRODUCT " + "(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                        "NAME           TEXT    NOT NULL," + " PRICE          INT     NOT NULL)");
    }

    @Override
    public void insert(Product product) {
        sqlRequestService.executeUpdate(String.format("INSERT INTO PRODUCT (NAME, PRICE) VALUES (\"%s\", %s)",
                product.getName(),
                product.getPrice()
        ));
    }

    @Override
    public List<Product> getAll() {
        return getProducts("SELECT * FROM PRODUCT").collect(Collectors.toList());
    }

    @Override
    public Optional<Product> getMostExpensive() {
        return getProducts("SELECT * FROM PRODUCT ORDER BY PRICE DESC LIMIT 1").findAny();
    }

    @Override
    public Optional<Product> getLessExpensive() {
        return getProducts("SELECT * FROM PRODUCT ORDER BY PRICE LIMIT 1").findAny();
    }

    @Override
    public int getPriceSum() {
        return getSingleInteger("SELECT SUM(price) FROM PRODUCT");
    }

    @Override
    public int getAmount() {
        return getSingleInteger("SELECT COUNT(*) FROM PRODUCT");
    }

    private Stream<Product> getProducts(String query) {
        return sqlRequestService.executeQuery(query).stream().map((map) -> new Product(
                getField(map, "name", String.class),
                getField(map, "price", Long.class)
        ));
    }

    private static <T> T getField(Map<String, Object> stringObjectMap, String fieldName, Class<T> tClass) {
        Object field = stringObjectMap.get(fieldName);
        if (field == null || !field.getClass().equals(tClass)) {
            throw new RuntimeException(String.format("Product must contain valid \"%s\"", fieldName));
        }
        return tClass.cast(field);
    }

    private static <T> T getSingleElement(List<T> list) {
        if (list.size() != 1) {
            throw new RuntimeException("Invalid SQL response");
        }
        return list.get(0);
    }

    private Integer getSingleInteger(String query) {
        Map<String, Object> stringObjectMap = getSingleElement(sqlRequestService.executeQuery(query));
        Object result = stringObjectMap.get(getSingleElement(new ArrayList<>(stringObjectMap.keySet())));

        if (!(result instanceof Integer)) {
            throw new RuntimeException("Invalid database response");
        }
        return (Integer) result;
    }
}
