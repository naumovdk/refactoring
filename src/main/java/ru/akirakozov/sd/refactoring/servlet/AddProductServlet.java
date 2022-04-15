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
import java.sql.Statement;

/**
 * @author akirakozov
 */
public class AddProductServlet extends AbstractServlet {

    public AddProductServlet(HtmlFormatter htmlFormatter, ProductDao productDao) {
        super(htmlFormatter, productDao);
    }

    @Override
    protected void doGet(HttpServletRequest request, PrintWriter writer) {
        String name = getParameter(request, writer, "name");
        String priceString = getParameter(request, writer, "price");

        long price;
        try {
            price = Long.parseLong(priceString);
        } catch (NumberFormatException e) {
            writer.println("Failed due to \"price\" parameter invalidity");
            return;
        }

        productDao.insert(new Product(name, price));
        writer.println("OK");
    }
}
