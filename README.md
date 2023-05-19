# StockManagementSystem
##### :green_square: Its a StockManagementSystem
## :hash: Frameworks and Languages Used -
    1. SpringBoot
    2. JAVA
    3. Postman
-----------------------------------------------------------------------------------------------------------------------------------------------------------------------
## :hash: Dataflow (Functions Used In)
### :green_square: 1. Model - I have used one model which is Stock.java
#### :large_orange_diamond: Stock.java
```java
package com.geekster.StockManagementSystem.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class Stock {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer stockId;

    @Column(unique = true)
    private String stockName;

    private Double stockPrice;

    private Integer stockOwnerCount;

    @Enumerated(EnumType.STRING)//only for watching strings in DB.
    private StockType stockType; //CAN ONLY BE 0,1,2

    private Double stockMarketCap;

    private LocalDateTime stockBirthTimeStamp;

}
```
##### To See Model
:heavy_check_mark: [Model](https://github.com/harshsikarwar20/StockManagementSystem/tree/master/src/main/java/com/geekster/StockManagementSystem/Model)
-----------------------------------------------------------------------------------------------------------------------------------------------------------------------
### :green_square: 2. Service - I have used one service which is StockService.java
#### :large_orange_diamond: StockService.java
```java
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
```
#### To See Service
:heavy_check_mark: [Service](https://github.com/harshsikarwar20/StockManagementSystem/tree/master/src/main/java/com/geekster/StockManagementSystem/Service)
-----------------------------------------------------------------------------------------------------------------------------------------------------------

### :green_square: 3. Controller - I have used one controller which is StockController.java
#### :large_orange_diamond: StockController.java
```java
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
```
#### To See Controller
:heavy_check_mark: [Controller](https://github.com/harshsikarwar20/StockManagementSystem/tree/master/src/main/java/com/geekster/StockManagementSystem/Controller)
-----------------------------------------------------------------------------------------------------------------------------------------------------------------------
### :green_square: 4. Repository - I have used one Dao which is TodoDao.java
#### :large_orange_diamond: TodoDao.java
```java
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface IStockRepository extends CrudRepository<Stock,Integer> {

    @Modifying
    @Query(value = "update Stock set STOCK_MARKET_CAP =:stockMarketCap where Stock_id = :stockId" , nativeQuery = true)
    public void updateStocksById(Double stockMarketCap, Integer stockId);

    List<Stock> findByStockPriceGreaterThanAndStockBirthTimeStampLessThanOrderByStockName(Double price, LocalDateTime date);

    @Query(value = "select * from STOCK where STOCK_MARKET_CAP > :capPercentage" , nativeQuery = true)
    List<Stock> getAllStocksAboveMarketCap(Double capPercentage);

    @Modifying
    @Query(value = "update stock set STOCK_TYPE = :myType where Stock_id = :id", nativeQuery = true)
    void modifyStockTypeById( String myType,Integer id);

    @Modifying
    @Query(value = "update stock set stock_id = :stockId, stock_name = :stockName, stock_price= :stockPrice, stock_Birth_Time_Stamp =:stockBirthTimeStamp where stock_id = :id",nativeQuery = true)
    void updateStockById(Integer id, Integer stockId, String stockName, Double stockPrice, LocalDateTime stockBirthTimeStamp);

}
```
#### To See Dao
:heavy_check_mark: [Dao](https://github.com/harshsikarwar20/StockManagementSystem/tree/master/src/main/java/com/geekster/StockManagementSystem/Repository)
-----------------------------------------------------------------------------------------------------------------------------------------------------------------------
## :hash: DataStructures Used in Project
    1. ARRAYLIST
    2. List
-------------------------------------------------------------------------------------------------------------------------------------------------------
## :hash: Project Summary
### :large_orange_diamond: Project result 

### :green_square: Get All Stock By Browser - https://localhost:8080/stocks
### :green_square: Added Stock By Postman - https://localhost:8080/add/list/stocks

#### :small_blue_diamond: Image after hitting the PostMapping on Postman.
![ss2](https://github.com/harshsikarwar20/StockManagementSystem/assets/123385605/84ec8f08-59d2-46ee-bef7-ac1443f4193b)
#### :small_blue_diamond: Image after hitting the GetMapping on h2-console, we can see one post.
![ss3](https://github.com/harshsikarwar20/StockManagementSystem/assets/123385605/a393cd34-897c-4b18-88a4-0016944281d8)
### :green_square: Update Stock By Postman - https://localhost:8080/stock/{id}

#### :small_blue_diamond: Image after hitting the PutMapping on Postman.
![ss6](https://github.com/harshsikarwar20/StockManagementSystem/assets/123385605/aa0c38db-a7d6-4f02-8198-198117d38890)
#### :small_blue_diamond: Image after hitting the h2-console on Brower, we can see one updation
![ss7](https://github.com/harshsikarwar20/StockManagementSystem/assets/123385605/a7381411-5752-4078-b732-7e670f7b4e80)
### :green_square: Delete User By Postman - https://localhost:8080/delete/stock/by/{stockId}

#### :small_blue_diamond: Image after hitting the DeleteMapping on Postman.
![ss4](https://github.com/harshsikarwar20/StockManagementSystem/assets/123385605/ae1ccb34-226c-4099-a5d4-957d07805621)
#### :small_blue_diamond: Image after hitting the GetMapping on Brower, we can see one deletion
![ss5](https://github.com/harshsikarwar20/StockManagementSystem/assets/123385605/ecbdb559-640c-4642-96d2-e783dab6e81e)
