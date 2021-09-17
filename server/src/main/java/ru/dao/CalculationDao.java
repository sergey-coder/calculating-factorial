package ru.dao;

import org.springframework.stereotype.Component;
import ru.domain.Calculation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Совершает операции CRUD над колецией с сохраненной информацией о вычеслениях.
 */
@Component
public class CalculationDao {

    private final List<Calculation> listCalculation;

   {
       listCalculation = new ArrayList<>();
   }

    public void addNewCalculation(Calculation calculation) {
        listCalculation.add(calculation);
    }

    public Calculation findByUid(String uid) {
        return listCalculation.stream().filter(calculation -> calculation.getUid().equals(uid)).findFirst().get();
    }

    public boolean checkCalculationUid(String uid){
        Optional<Calculation> calculationEntity = listCalculation.stream().filter(calculation -> calculation.getUid().equals(uid)).findFirst();
        return calculationEntity.isPresent();
    }

    public void updateCalculation(Calculation calculation){
        Calculation entityForUpdate = findByUid(calculation.getUid());
        if(calculation.getResultCalculation()!=null){
            entityForUpdate.setResultCalculation(calculation.getResultCalculation());
        }

        if(calculation.getStatusCalculation()!=null){
            entityForUpdate.setStatusCalculation(calculation.getStatusCalculation());
        }
    }

}
