package org.example.dto;

import java.util.ArrayList;
import java.util.List;

public class DataSetupResponse {
    public String testShowId;
    public List<String> testSeatIds;
    public String testScreenId;


    public DataSetupResponse() {
        testSeatIds = new ArrayList<>();
    }
}
