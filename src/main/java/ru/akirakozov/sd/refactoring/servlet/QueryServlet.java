package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.dao.ProductDao;
import ru.akirakozov.sd.refactoring.entity.Product;
import ru.akirakozov.sd.refactoring.html.HtmlFormatter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Collections;
import java.util.Optional;

/**
 * @author akirakozov
 */
public class QueryServlet extends AbstractServlet {

    public QueryServlet(HtmlFormatter htmlFormatter, ProductDao productDao) {
        super(htmlFormatter, productDao);
    }

    @Override
    protected void doGet(HttpServletRequest request, PrintWriter writer) {
        String command = getParameter(request, writer, "command");
        String content;

        switch (command) {
            case "max":
                content = productListPage("Product with max price: ", productDao.getMostExpensive());
                break;
            case "min":
                content = productListPage("Product with min price: ", productDao.getLessExpensive());
                break;
            case "sum":
                content = htmlFormatter.contentPage("Summary price: " + productDao.getPriceSum());
                break;
            case "count":
                content = htmlFormatter.contentPage("Number of products: " + productDao.getAmount());
                break;
            default:
                content = htmlFormatter.errorPage("Unknown command: " + command);
        }

        writer.println(content);
    }

    private String productListPage(String header, Optional<Product> productSupplier) {
        String content = productSupplier.map(product -> htmlFormatter.productList(Collections.singletonList(product)))
                .orElse("No products found");

        return htmlFormatter.headerContent(header, content);
    }
}