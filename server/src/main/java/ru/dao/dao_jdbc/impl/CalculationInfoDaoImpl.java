package ru.dao.dao_jdbc.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ru.calculate.domain.Calculation;
import ru.dao.ServiceRecommenceCalculate;
import ru.dao.dao_jdbc.model.CalculationInfo;

import java.sql.ResultSet;

/**
 * Реализует механизм сохранения данных о вычислении в базе данных.
 */
public class CalculationInfoDaoImpl implements ServiceRecommenceCalculate {

    private static Logger logger = LoggerFactory.getLogger(CalculationInfoDaoImpl.class);

    private final JdbcTemplate jdbcTemplate;

    private final String INSERT_SQL = "INSERT INTO calculationforrestart(" +
            "number, uid,treads) VALUES(?, ?, ?)";
    private final String SELECT_UID_SQL = "SELECT * FROM calculationforrestart WHERE uid = ?";
    private final String DELETE_UID_SQL = "DELETE FROM calculationforrestart WHERE uid = ?";

    private final RowMapper<CalculationInfo> ROW_MAPPER = (ResultSet resultSet, int rowNum) -> new CalculationInfo(
            resultSet.getInt("id"),
            resultSet.getString("uid"),
            resultSet.getInt("number"),
            resultSet.getInt("treads")
    );

    public CalculationInfoDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Получает сохраненные данные о вычислении.
     *
     * @param uid идентификатор вычисления.
     * @return данные о вычислении по модели Calculation.
     */
    @Override
    public Calculation getCalculation(String uid) {
        logger.info("предоставление данных для старта вычислений");
        CalculationInfo calculationInfo = jdbcTemplate.query(SELECT_UID_SQL, ROW_MAPPER, uid).stream().findFirst().orElse(null);
        if (calculationInfo == null) {
            return null;
        }
        Calculation calculation = new Calculation();
        calculation.setUid(calculationInfo.getUid());
        calculation.setNumber(calculationInfo.getNumber());
        calculation.setTreads(calculationInfo.getTreads());

        return calculation;
    }

    /**
     * Проверяет наличие сохраненного вычисления.
     *
     * @param uid идентификатор вычисления.
     * @return true если вычисление имеется.
     */
    @Override
    public boolean checkCalculation(String uid) {
        logger.info("проверка наличия незавершенного вычисления");
        return jdbcTemplate.query(SELECT_UID_SQL, ROW_MAPPER, uid).stream().findFirst().isPresent();
    }

    /**
     * Сохраняет данные о вычислении.
     *
     * @param calculation данные о вычислении по модели Calculation.
     */
    @Override
    public void saveDataCalculating(Calculation calculation) {
        logger.info("сохранение  CalculationInfo для возобновления вычислений с uid " + calculation.getUid());
        jdbcTemplate.update(INSERT_SQL, calculation.getNumber(), calculation.getUid(), calculation.getTreads());
    }

    /**
     * Удаляет данные о вычислении.
     *
     * @param uid идентификатор вычисления.
     */
    @Override
    public void deleteDataCalculating(String uid) {
        logger.info("удаление записи о вычислении");
        jdbcTemplate.update(DELETE_UID_SQL, uid);
    }

}
