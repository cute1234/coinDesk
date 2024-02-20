package com.coindesk.data;

import com.coindesk.data.entity.Currency;
import com.coindesk.data.repository.CurrencyRepository;
import com.coindesk.data.service.CurrencyService;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@SpringBootTest
@ExtendWith(SpringExtension.class)
@TestPropertySource(locations = "classpath:test-application.properties")
public class CurrencyControllerUnitTest {

    @Mock
    private CurrencyRepository currencyRepository;

    @InjectMocks
    private CurrencyService currencyService;

    @Test
    public void testFindById() {
        Optional<Currency>  currencyById = currencyService.findById("1");
        String currencyAsString = currencyById.map(Object::toString).orElse("Currency not found");
        System.out.println(currencyAsString);
    }

    @Test
    public void testSaveCurrency() throws JSONException {
    	currencyService.saveConindeskApi();
    }

    @Test
    public void testUpdateCurrency() {
        Currency currency = new Currency();
        currency.setCode("USD");
        currency.setChineseName("testsetsetste");       	
        Currency result = currencyService.updateCurrency("67", currency);
        System.out.println(result);
    }

    @Test
    public void testDeleteCurrency() {
    	currencyService.deleteCurrency("66");
        doNothing().when(currencyRepository).deleteById("66");
        verify(currencyRepository, times(1)).deleteById("66");
    }
    
    @Test
    public void testCallCoinDeskAPI() {
    	 Map<String, Object> map = currencyService.callCoinDeskAPI();
    	 System.out.println("Map: " + map);
    }
    
    @Test
    public void testTransformData() {
    	 Map<String, Object> map = currencyService.transformData();
    	 System.out.println("Map: " + map);
    }

}
