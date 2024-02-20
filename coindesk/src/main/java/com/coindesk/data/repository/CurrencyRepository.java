package com.coindesk.data.repository;

import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.coindesk.data.entity.Currency;

public interface CurrencyRepository extends JpaRepository<Currency, String> {

	Optional<Currency> findById(String id);
    @Transactional
    @Modifying
    @Query(value = "UPDATE Currency SET " +
            "code = COALESCE(:code, code), " +
            "chinese_name = COALESCE(:chineseName, chinese_name), " +
            "symbol = COALESCE(:symbol, symbol), " +
            "rate = COALESCE(:rate, rate), " +
            "rate_Float = COALESCE(:rateFloat, rate_Float), " +
            "description = COALESCE(:description, description), " +
            "update_Time = COALESCE(:updateTime, update_Time) " +
            "WHERE id = :id", nativeQuery = true)
    void updateCurrency(@Param("id") String id, 
                        @Param("code") String code, 
                        @Param("chineseName") String chineseName, 
                        @Param("symbol") String symbol, 
                        @Param("rate") String rate,
                        @Param("rateFloat") Double rateFloat,
                        @Param("description") String description,
                        @Param("updateTime") String updateTime);
}