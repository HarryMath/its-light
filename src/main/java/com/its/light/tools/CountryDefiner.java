package com.its.light.tools;

import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.stream.Collectors;

@Component
public class CountryDefiner {

    public String getCountry(String ipAddress) {
        try {
            String response = requestTo("https://api.ip2country.info/ip?" + ipAddress);
            return response
                    .split("countryName\": \"")[1]
                    .split("\",")[0];
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public boolean isCountryExists(String country) throws IOException {
        String response = requestTo("https://restcountries.eu/rest/v2/name/" + country);
        if (response.startsWith("{") ) return false;
        String resultCountry = response
                .split("name\":\"")[1]
                .split("\",")[0];
        return resultCountry.equalsIgnoreCase(country);
    }

    private String requestTo(String URL) throws IOException {
        URL url = new URL(URL);
        URLConnection connection = url.openConnection();
        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        return br.lines().collect(Collectors.joining());
    }
}
