package ru.akirakozov.sd.refactoring.html;

import ru.akirakozov.sd.refactoring.entity.Product;

import java.util.List;

public interface HtmlFormatter {

    String contentPage(String content);

    String headerContent(String header, String content);

    String productList(List<Product> products);

    String errorPage(String errorMessage);
}
