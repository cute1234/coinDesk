package com.coindesk.data.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.coindesk.data.entity.Currency;
import com.coindesk.data.repository.CurrencyRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Optional;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;

@Service
public class CurrencyService {
    @Autowired
    private CurrencyRepository currencyRepository;

    // 處理幣別DB維護功能
    public Optional<Currency> findById(String id) {
        return currencyRepository.findById(id);
    }
    
    //插入
    public Currency saveCurrency(Currency currency) {
        return currencyRepository.save(currency);
    }
 
    public void saveConindeskApi() {
    	try {
            JSONObject jsonObject = new JSONObject(transformData());
            JSONObject currencyInfoJson = jsonObject.getJSONObject("currencyInfo");
            JSONObject usdJson = currencyInfoJson.getJSONObject("USD");
            JSONObject gbpJson = currencyInfoJson.getJSONObject("GBP");
            JSONObject eurJson = currencyInfoJson.getJSONObject("EUR");

            Currency usdCurrency = new Currency();
            usdCurrency.setCode(usdJson.getString("code"));
            usdCurrency.setChineseName("美元");
            usdCurrency.setSymbol(usdJson.getString("symbol"));
            usdCurrency.setRate(usdJson.getString("rate"));
            usdCurrency.setDescription(usdJson.getString("description"));
            usdCurrency.setRateFloat(usdJson.getDouble("rate_float"));
            usdCurrency.setUpdateTime(jsonObject.getString("updateTime"));
            currencyRepository.save(usdCurrency);

            Currency gbpCurrency = new Currency();
            gbpCurrency.setCode(gbpJson.getString("code"));
            gbpCurrency.setChineseName("英鎊");
            gbpCurrency.setSymbol(gbpJson.getString("symbol"));
            gbpCurrency.setRate(gbpJson.getString("rate"));
            gbpCurrency.setDescription(gbpJson.getString("description"));
            gbpCurrency.setRateFloat(gbpJson.getDouble("rate_float"));
            gbpCurrency.setUpdateTime(jsonObject.getString("updateTime"));
            currencyRepository.save(gbpCurrency);

            Currency eurCurrency = new Currency();
            eurCurrency.setCode(eurJson.getString("code"));
            eurCurrency.setChineseName("歐元");
            eurCurrency.setSymbol(eurJson.getString("symbol"));
            eurCurrency.setRate(eurJson.getString("rate"));
            eurCurrency.setDescription(eurJson.getString("description"));
            eurCurrency.setRateFloat(eurJson.getDouble("rate_float"));
            eurCurrency.setUpdateTime(jsonObject.getString("updateTime"));
            currencyRepository.save(eurCurrency);
            

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    
    //更新
    @Transactional
    public Currency updateCurrency(String id, Currency currency) {
        currencyRepository.updateCurrency(id, 
                                          currency.getCode(), 
                                          currency.getChineseName(), 
                                          currency.getSymbol(), 
                                          currency.getRate(),
                                          currency.getRateFloat(),
                                          currency.getDescription(),
                                          currency.getUpdateTime());
        return currencyRepository.findById(id).orElse(null);
    }

    public void deleteCurrency(String id) {
        currencyRepository.deleteById(id);
    }

    // 呼叫 coindesk API
    public Map<String, Object> callCoinDeskAPI() {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject("https://api.coindesk.com/v1/bpi/currentprice.json", Map.class);
    }
    
    //轉換格式A. 更新時間（時間格式範例：1990/01/01 00:00:00）。 B. 幣別相關資訊（幣別，幣別中文名稱，以及匯率）。
    public Map<String, Object> transformData() {
        JSONObject originalJson = new JSONObject(callCoinDeskAPI());
        
        String updateTime = LocalDateTime.now().toString();
        updateTime = LocalDateTime.parse(updateTime)
                                .format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));

        Map<String, Object> newData = new HashMap<>();
        newData.put("updateTime", updateTime);

        JSONObject originalCurrencyInfo = originalJson.getJSONObject("bpi");
        Map<String, Object> currencyInfo = new HashMap<>();
        originalCurrencyInfo.keys().forEachRemaining(key -> {
            JSONObject currency = originalCurrencyInfo.getJSONObject(key);
            Map<String, Object> newCurrency = new HashMap<>();
            newCurrency.put("chinesename", getChineseName(key));
            newCurrency.put("code", currency.getString("code"));
            newCurrency.put("symbol", currency.getString("symbol"));
            newCurrency.put("rate", currency.getString("rate"));
            newCurrency.put("description", currency.getString("description"));
            newCurrency.put("rate_float", currency.getDouble("rate_float"));
            currencyInfo.put(key, newCurrency);
        });

        newData.put("currencyInfo", currencyInfo);

        return newData;
    }

    
    
    private String getChineseName(String currencyCode) {
        switch (currencyCode) {
            case "USD":
                return "美元";
            case "GBP":
                return "英鎊";
            case "EUR":
                return "歐元";
            default:
                return "";
        }
    }
}
