package com.minch.config;

import bitronix.tm.BitronixTransactionManager;
import jakarta.transaction.SystemException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.logging.Level;
import java.util.logging.Logger;


@Configuration
@EnableTransactionManagement
public class TransactionManagerConfig {

    @Bean
    public BitronixTransactionManager bitronixTransactionManager() throws SystemException {
        // 配置 Bitronix 事务管理器
        try {
            BitronixTransactionManager transactionManager = new BitronixTransactionManager();
            transactionManager.setTransactionTimeout(60);
            return transactionManager;
        } catch (javax.transaction.SystemException ex) {
            Logger.getLogger(TransactionManagerConfig.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
