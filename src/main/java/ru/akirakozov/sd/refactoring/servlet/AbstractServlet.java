package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.dao.ProductDao;
import ru.akirakozov.sd.refactoring.html.HtmlFormatter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public abstract class AbstractServlet extends HttpServlet {

    protected final HtmlFormatter htmlFormatter;

    protected final ProductDao productDao;

    public AbstractServlet(HtmlFormatter htmlFormatter, ProductDao productDao) {
        this.htmlFormatter = htmlFormatter;
        this.productDao = productDao;
    }

    abstract protected void doGet(HttpServletRequest request, PrintWriter writer);

    @Override
    protected final void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doGet(request, response.getWriter());
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }

    protected String getParameter(HttpServletRequest request, PrintWriter writer, String parameterName) {
        String parameter = request.getParameter(parameterName);
        if (parameter == null) {
            writer.println(htmlFormatter.errorPage(String.format("No valid '%s' parameter given", parameterName)));
        }
        return parameter;
    }
}
