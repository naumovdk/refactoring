package ru.akirakozov.sd.refactoring;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import ru.akirakozov.sd.refactoring.configuration.Configuration;
import ru.akirakozov.sd.refactoring.configuration.ConfigurationImpl;
import ru.akirakozov.sd.refactoring.dao.ProductDao;
import ru.akirakozov.sd.refactoring.dao.ProductDaoImpl;
import ru.akirakozov.sd.refactoring.html.HtmlFormatter;
import ru.akirakozov.sd.refactoring.html.HtmlFormatterImpl;
import ru.akirakozov.sd.refactoring.servlet.AbstractServlet;
import ru.akirakozov.sd.refactoring.servlet.AddProductServlet;
import ru.akirakozov.sd.refactoring.servlet.GetProductsServlet;
import ru.akirakozov.sd.refactoring.servlet.QueryServlet;
import ru.akirakozov.sd.refactoring.sql.SqlRequestService;
import ru.akirakozov.sd.refactoring.sql.SqlRequestServiceImpl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * @author akirakozov
 */
public class Main {

    public static void main(String[] args) throws Exception {

        Configuration configuration = ConfigurationImpl.getInstance();

        SqlRequestService sqlRequestService = new SqlRequestServiceImpl(configuration.sqlUrl());

        HtmlFormatter htmlFormatter = new HtmlFormatterImpl();

        ProductDao productDao = new ProductDaoImpl(sqlRequestService);
        productDao.initialize();

        Server server = new Server(8081);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);

        registerServlet(context, new AddProductServlet(htmlFormatter, productDao), "/add-product");
        registerServlet(context, new GetProductsServlet(htmlFormatter, productDao), "/get-products");
        registerServlet(context, new QueryServlet(htmlFormatter, productDao), "/query");

        server.start();
        server.join();
    }

    private static void registerServlet(ServletContextHandler contextHandler, AbstractServlet servlet, String url) {
        contextHandler.addServlet(new ServletHolder(servlet), url);
    }
}
