package ru.akirakozov.sd.refactoring.html;

import ru.akirakozov.sd.refactoring.entity.Product;

import java.util.List;
import java.util.stream.Collectors;

public class HtmlFormatterImpl implements HtmlFormatter {

    @Override
    public String contentPage(String content) {
        return String.format("<html><body>%s</body></html>", content);
    }

    @Override
    public String headerContent(String header, String content) {
        return contentPage(String.format("<h1>%s</h1>%s", header, content));
    }

    @Override
    public String productList(List<Product> products) {
        return products.stream()
                .map(product -> String.format("%s\t%s</br>", product.getName(), product.getPrice()))
                .collect(Collectors.joining());
    }

    @Override
    public String errorPage(String errorMessage) {
        return "ERROR: " + errorMessage;
    }
}
