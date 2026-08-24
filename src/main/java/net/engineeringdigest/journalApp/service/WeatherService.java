package net.engineeringdigest.journalApp.service;

import net.engineeringdigest.journalApp.api.response.WeatherResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {

    @Value("${weather.api.key}")
    private String apiKey;

    private static final String API ="http://api.weatherstack.com/current?access_key=API_KEY&query=CITY";

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RedisService redisService;

    public WeatherResponse getWeather(String city)
    {
        WeatherResponse weatherResponse = redisService.get("weather-of-" + city, WeatherResponse.class);
        if(weatherResponse!=null)
        {
            return weatherResponse;
        }
       else {
            String finalAPI = API.replace("CITY", city).replace("API_KEY", apiKey);
            // restTemplate.exchange use kar rahe ho toh ResponseEntity handle karni hogi
            ResponseEntity<WeatherResponse> response = restTemplate.exchange(finalAPI, HttpMethod.POST, null, WeatherResponse.class);
            WeatherResponse body = response.getBody();
            if(body!=null)
            {
                redisService.set("weather-of-"+ city, body, 300l);
            }
            return body;
        }
    }
}


