package ru.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.dao.ServiceRecommenceCalculate;
import ru.dao.dao_file.ReadFile;
import ru.dao.dao_file.ReadWriteLock;
import ru.dao.dao_file.ServiceReadWriteFile;
import ru.dao.dao_file.WriteFile;
import ru.dao.dao_jdbc.impl.CalculationInfoDaoImpl;

@Configuration
public class ServerConfiguration {
    private static Logger logger = LoggerFactory.getLogger(ServerConfiguration.class);

    private final String TYPE_RECOMMENCE = "database";

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Value("${server.typeRecommenceCaiculate:file}")
    private String typeRecommenceCaiculate;

    /**
     * Инициализирует ServiceRecommenceCalculate классом реализующим механизм сохранения данных,
     * класс определяется в соответствии с значением server.typeRecommenceCaiculate,
     * значением по умолчанию является file, при котором сервис инициализируется классом ServiceReadWriteFile.
     * @return bean класса реализующего механизм сохранения данных.
     */
    @Bean
    public ServiceRecommenceCalculate getServiceRecommenceCalculate(){
        logger.info("Используемый тип сохранения данных " + typeRecommenceCaiculate);
        return typeRecommenceCaiculate.equals(TYPE_RECOMMENCE)?
                new CalculationInfoDaoImpl(jdbcTemplate) : new ServiceReadWriteFile(getReadFile(), getWriteFile());
    }

    private ReadWriteLock getReadWriteLock(){
        return new ReadWriteLock();
    }

    private ReadFile getReadFile(){
        return new ReadFile(getReadWriteLock());
    }

    private WriteFile getWriteFile(){
        WriteFile writeFile = new WriteFile(getReadWriteLock());
        writeFile.init();
        return writeFile;
    }

}
