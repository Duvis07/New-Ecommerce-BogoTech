package com.newpiece.infrastructure.controller;

import com.newpiece.application.service.ProductService;
import com.newpiece.application.service.StockService;
import com.newpiece.domain.Stock;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/home")
@Slf4j
public class HomeController {

    private final ProductService productService;
    private final StockService stockService;
    public HomeController(ProductService productService, StockService stockService) {
        this.productService = productService;
        this.stockService = stockService;
    }

    @GetMapping
    public String home(Model model, HttpSession httpSession){
        model.addAttribute("products", productService.getProducts());
        try {
            model.addAttribute("id", Integer.parseInt(httpSession.getAttribute("iduser").toString()));
        }catch (Exception e){

        }

        return "home";
    }
    @GetMapping("/product-detail/{id}")
    public String productDetail(@PathVariable Integer id, Model model, HttpSession httpSession){
        List<Stock> stocks = stockService.getStockByProduct(productService.getProductById(id));

        Integer lastBalance = null;
        // Verificamos si la lista stocks no está vacía
        if (!stocks.isEmpty()) {
            // conseguimos el último balance si hay elementos en la lista
            lastBalance = stocks.get(stocks.size() - 1).getBalance();
        }

        log.info("Id product: {}", id);
        log.info("stock size: {}", stocks.size());

        model.addAttribute("product", productService.getProductById(id));
        model.addAttribute("stock", lastBalance);
        try {
            model.addAttribute("id", Integer.parseInt(httpSession.getAttribute("iduser").toString()));
        }catch (Exception e){

        }
        return "user/productdetail";
    }
}
