package com.geekster.StockManagementSystem.Controller;

import com.geekster.StockManagementSystem.Model.Stock;
import com.geekster.StockManagementSystem.Model.StockType;
import com.geekster.StockManagementSystem.Service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
public class StockController {
    @Autowired
    StockService stockService;

    @GetMapping(value = "/stocks")
    private Iterable<Stock> getAllStock(){
        return stockService.getAllStock();
    }

    @PostMapping(value = "/add/list/stocks")
    private String addStock(@RequestBody List<Stock> stockList){
        return stockService.addStock(stockList);
    }

    @DeleteMapping(value = "delete/stock/by/{stockId}")
    private String deleteStock(@PathVariable Integer stockId){
        return stockService.deleteStock(stockId);
    }

    @PutMapping(value = "/update/stockMarketCap/{stockMarketCap}/stockId/{stockId}")
    private String updateStockById(@PathVariable Double stockMarketCap , @PathVariable Integer stockId){
        return stockService.updateStocksById(stockMarketCap,stockId);
    }

    @GetMapping(value = "abovePrice/price/{price}/lowerData/date/{date}")
    public List<Stock> getStocksAbovePriceAndLowerDate(@PathVariable Double price,@PathVariable String date) {
        return stockService.getStocksAbovePriceAndLowerDate(price,date);
    }

    //custom query
    @GetMapping(value = "/cap/{capPercentage}")
    public List<Stock> getAllStocksAboveMarketCap(@PathVariable Double capPercentage) {
        return stockService.getAllStocksAboveMarketCap(capPercentage);
    }

    @PutMapping(value = "stock/type/id")
    private void updateTypeById(@RequestParam StockType stockType , @RequestParam Integer id){
        stockService.updateTypeById(stockType,id);
    }
    @PutMapping(value = "/stock/{id}")
    public void updateStockById(@PathVariable Integer id, @RequestBody Stock myStock) {
        stockService.updateStockById(id,myStock);
    }
}
