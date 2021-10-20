package ru.dao.dao_jdbc.impl;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.transaction.support.TransactionTemplate;
import ru.calculate.domain.Calculation;
import ru.dao.dao_jdbc.model.CalculationInfo;

import java.sql.ResultSet;
import java.util.List;

public class CalculationDaoImplTest {

    private EmbeddedDatabase embeddedDatabase;

    private JdbcTemplate jdbcTemplate;

    private CalculationInfoDaoImpl calculationDaoImpl;

    private TransactionTemplate transactionTemplate;

    @Before
    public void setUp() {
        embeddedDatabase = new EmbeddedDatabaseBuilder()
                .addScript("schema.sql")
                .setType(EmbeddedDatabaseType.H2)
                .build();

        jdbcTemplate = new JdbcTemplate(embeddedDatabase);
        transactionTemplate = new TransactionTemplate();
        calculationDaoImpl = new CalculationInfoDaoImpl(jdbcTemplate);
    }

    @Test
    public void saveDataCalculating() {
        Calculation calculation1 = new Calculation();
        calculation1.setNumber(5);
        calculation1.setUid("testUidFirst");
        calculation1.setTreads(3);
        calculationDaoImpl.saveDataCalculating(calculation1);

        Calculation calculation2 = new Calculation();
        calculation2.setNumber(50);
        calculation2.setUid("testUidSecond");
        calculation2.setTreads(30);
        calculationDaoImpl.saveDataCalculating(calculation2);

        List<CalculationInfo> calculationInfoList = testFindAll();
        Assert.assertNotNull(calculationInfoList);
        Assert.assertEquals(2, calculationInfoList.size());
        Assert.assertEquals(calculation1.getNumber(), java.util.Optional.of(calculationInfoList.get(0).getNumber()).get());
        Assert.assertEquals(calculation1.getUid(), java.util.Optional.of(calculationInfoList.get(0).getUid()).get());
        Assert.assertEquals(calculation1.getTreads(), java.util.Optional.of(calculationInfoList.get(0).getTreads()).get());
    }

    @Test
    public void getCalculation() {
        Calculation calculation = new Calculation();
        calculation.setNumber(5);
        calculation.setUid("testUidFirst");
        calculation.setTreads(3);
        calculationDaoImpl.saveDataCalculating(calculation);

        Assert.assertNull(calculationDaoImpl.getCalculation("failUid"));
        Assert.assertNotNull(calculationDaoImpl.getCalculation(calculation.getUid()));
    }

    @Test
    public void checkCalculationFalse() {
        Assert.assertFalse(calculationDaoImpl.checkCalculation("failUid"));
    }

    @Test
    public void checkCalculationTrue() {
        Calculation calculation = new Calculation();
        calculation.setNumber(5);
        calculation.setUid("testUid");
        calculation.setTreads(3);
        calculationDaoImpl.saveDataCalculating(calculation);

        Assert.assertTrue(calculationDaoImpl.checkCalculation("testUid"));
    }

    @Test
    public void deleteDataCalculating() {
        Calculation calculation = new Calculation();
        calculation.setNumber(5);
        calculation.setUid("testUid");
        calculation.setTreads(3);

        calculationDaoImpl.saveDataCalculating(calculation);
        Assert.assertTrue(calculationDaoImpl.checkCalculation("testUid"));

        calculationDaoImpl.deleteDataCalculating("testUid");
        Assert.assertFalse(calculationDaoImpl.checkCalculation("testUid"));
    }

    private List<CalculationInfo> testFindAll() {
        return jdbcTemplate.query("select * from calculationforrestart", ROW_MAPPER);
    }

    private final RowMapper<CalculationInfo> ROW_MAPPER = (ResultSet resultSet, int rowNum) -> new CalculationInfo(
            resultSet.getInt("id"),
            resultSet.getString("uid"),
            resultSet.getInt("number"),
            resultSet.getInt("treads")
    );

    @After
    public void tearDown() {
        embeddedDatabase.shutdown();
    }

}