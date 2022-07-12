package com.server.customer.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class CustomerRegistryDto {

    @Getter
    @AllArgsConstructor
    public static class CustomerRegistryRequest {
        private long id;
        private String user_id;
        private String password;
        private String last_name;
        private String first_name;
        private int birthday;
        private String gender;
        private String country_number;
        private String country_code;
        private String country_string;
    }
}
