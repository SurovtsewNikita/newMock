package com.example.newMock.controller;

import com.example.newMock.model.RequestDTO;
import com.example.newMock.model.ResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

@RestController
public class MainController {
    private Logger log = LoggerFactory.getLogger(MainController.class);
    ObjectMapper mapper = new ObjectMapper();

    @PostMapping(
            value = "/info/postBalances",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public Object postBalance(@RequestBody RequestDTO requestDTO){
        try {
            String clientId = requestDTO.getClientId();
            char firstDigit = clientId.charAt(0);
            String rqUID = requestDTO.getRqUID();
            BigDecimal maxLimit;
            String valuta;
            Random random = new Random();


            if (firstDigit == '8') {
                maxLimit = BigDecimal.valueOf(2000).setScale(2, RoundingMode.HALF_UP);
                valuta  = "US";
            } else if (firstDigit == '9') {
                maxLimit = BigDecimal.valueOf(1000).setScale(2, RoundingMode.HALF_UP);
                valuta  = "EU";
            } else {
                maxLimit = BigDecimal.valueOf(10000).setScale(2, RoundingMode.HALF_UP);
                valuta  = "RU";
            }

            /* попытка через switch
            switch (firstDigit) {
                case '8':
                    maxLimit = BigDecimal.valueOf(2000).setScale(2, RoundingMode.HALF_UP);
                    valuta  = "US";
                    break;
                case '9':
                    maxLimit = BigDecimal.valueOf(1000).setScale(2, RoundingMode.HALF_UP);
                    valuta  = "EU";
                    break;
                default:
                    maxLimit = BigDecimal.valueOf(10000).setScale(2, RoundingMode.HALF_UP);
                    valuta  = "RU";
                    break;
            }

             */
            double rand = random.nextDouble() * maxLimit.doubleValue();
            BigDecimal balance = BigDecimal.valueOf(rand).setScale(2, RoundingMode.HALF_UP);

            ResponseDTO responseDTO = new ResponseDTO();


            responseDTO.setRqUID(rqUID);
            responseDTO.setClientId(clientId);
            responseDTO.setAccount(requestDTO.getAccount());
            responseDTO.setCurrency(valuta);
            responseDTO.setBalance(balance);
            responseDTO.setMaxLimit(maxLimit);


            log.error("********** RequestDTO **********" + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(requestDTO)); // mapper преобарзует в стрингу
            log.error("********** ResponseDTO **********" + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(responseDTO)); // mapper преобарзует в стрингу

            return responseDTO;
        } catch (Exception e) {
//            throw new RuntimeException(e); // как возможный вариант
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}
