package com.coindesk.data.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.coindesk.data.entity.Currency;
import com.coindesk.data.service.CurrencyService;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/currency")
public class CurrencyController {
/*
 創表的SQL
 CREATE TABLE Currency (
    currency_code VARCHAR(3) PRIMARY KEY,
    chinese_name VARCHAR(50)
);
 */
    @Autowired
    private CurrencyService currencyService;

    // 查詢幣別對應表資料API
    //http://localhost:8080/api/currency/1
    @GetMapping("/{id}")
    public ResponseEntity<Optional<Currency>> getCurrency(@PathVariable String id) {
        Optional<Currency> currency = currencyService.findById(id);
        return ResponseEntity.ok(currency);
    }

    // 新增幣別對應表資料API
    /* http://localhost:8080/api/currency
    {
   "code": "USD",
   "chineseName": "美元",
   "symbol": "&#36;",
   "rate": "51,925.625",
   "description": "United States Dollar",
   "updateTime": "2024-02-15T14:58:04+00:00"
   }
    */
    @PostMapping
    public ResponseEntity<Currency> addCurrency(@RequestBody Currency currency) {
        Currency savedCurrency = currencyService.saveCurrency(currency);
        return ResponseEntity.ok(savedCurrency);
    }
    
    // 加入 coindesk API
    @GetMapping("/saveConindeskApi")
    public String saveConindeskApi() {
    	currencyService.saveConindeskApi();
        return "success";
    }


    // 更新幣別對應表資料API
/* http://localhost:8080/api/currency/1
  {
    "code": "USD",
    "chineseName": "美元",
    "symbol": "&#36;",
    "rate": "51,925.625",
    "description": "United States Dollar",
    "updateTime": "2024/02/20 13:59:24"
}
 */
    @PutMapping("/{id}")
    public ResponseEntity<Currency> updateCurrency(@PathVariable String id, @RequestBody Currency currency) {
        Currency updatedCurrency = currencyService.updateCurrency(id, currency);
        return ResponseEntity.ok(updatedCurrency);
    }

    // 刪除幣別對應表資料API
    //http://localhost:8080/api/currency/1
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCurrency(@PathVariable String id) {
        currencyService.deleteCurrency(id);
        return ResponseEntity.ok().build();
    }

    // 呼叫 coindesk API
    @GetMapping("/coindesk")
    public ResponseEntity<Map<String, Object>> callCoinDeskAPI() {
        Map<String, Object> coindeskData = currencyService.callCoinDeskAPI();
        return ResponseEntity.ok(coindeskData);
    }

    // 呼叫資料轉換的API
    @GetMapping("/transform")
    public ResponseEntity<Map<String, Object>> transformData() {
        Map<String, Object> transformedData = currencyService.transformData();
        return new ResponseEntity<>(transformedData, HttpStatus.OK);
    }
}

