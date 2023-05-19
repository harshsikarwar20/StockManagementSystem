package com.geekster.StockManagementSystem.Service;

import com.geekster.StockManagementSystem.Model.Stock;
import com.geekster.StockManagementSystem.Model.StockType;
import com.geekster.StockManagementSystem.Repository.IStockRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class StockService {
    @Autowired
    IStockRepository stockRepository;
    public Iterable<Stock> getAllStock() {
        return stockRepository.findAll();
    }

    public String addStock(List<Stock> stockList) {
        Iterable<Stock> status = stockRepository.saveAll(stockList);
        if(status != null){
            return "Added successfully...";
        }else{
            return "Not Added...";
        }
    }

    public String deleteStock(Integer stockId) {
        stockRepository.deleteById(stockId);
        return "Deleted successfully...";
    }
    @Transactional
    public String updateStocksById(Double stockMarketCap, Integer stockId) {
        stockRepository.updateStocksById(stockMarketCap,stockId);
        return "Updated...";
    }

    public List<Stock> getStocksAbovePriceAndLowerDate(Double price, String date) {

        LocalDateTime myDate = LocalDateTime.parse(date);
        return stockRepository.findByStockPriceGreaterThanAndStockBirthTimeStampLessThanOrderByStockName(price, myDate);
    }

    public List<Stock> getAllStocksAboveMarketCap(Double capPercentage) {
        return stockRepository.getAllStocksAboveMarketCap(capPercentage);
    }
    @Transactional
    public void updateTypeById(StockType stockType, Integer id) {
        String enumValue = stockType.name();
        stockRepository.modifyStockTypeById(enumValue, id);

        throw new IllegalStateException("Harsh testing transactional...");
    }

    @Transactional
    public void updateStockById(Integer id, Stock myStock) {
        stockRepository.updateStockById(id,myStock.getStockId(),myStock.getStockName(),myStock.getStockPrice(),myStock.getStockBirthTimeStamp());
    }
}
