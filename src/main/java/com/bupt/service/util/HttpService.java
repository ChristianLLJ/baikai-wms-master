package com.bupt.service.util;

import com.bupt.result.BaokaiHttpResult;
import com.bupt.result.HttpResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class HttpService {
    @Autowired
    RestTemplate restTemplate;
    @Async
    public <D> BaokaiHttpResult<?> httpResponse(String url, D d){
        String headUrl = url;
        RequestEntity<D> headRequestEntity = RequestEntity
                .post(headUrl)
                .header("Accept", MediaType.APPLICATION_JSON.toString())
                .header("app_key","BF0081CDF26EEDA1853D911C9235116B")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(d); //也可以是DTO
        System.out.println(headRequestEntity);
        ResponseEntity<BaokaiHttpResult> resultResponseEntity = restTemplate.exchange(headRequestEntity,BaokaiHttpResult.class);
        return resultResponseEntity.getBody();
    }
}
