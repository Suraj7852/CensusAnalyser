package censusanalyser;

import com.google.gson.Gson;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toCollection;

public class CensusAnalyser {
    public enum Country {INDIA,US,PAKISTAN}
    Country country;
    Map<CSVField,Comparator<CensusDAO>> stateField = null;

    public CensusAnalyser() {
        this.stateField = new HashMap<>();
        this.stateField.put(CSVField.STATE,Comparator.comparing(census -> census.state));
        this.stateField.put(CSVField.POPULATION,Comparator.comparing(census -> census.population,Comparator.reverseOrder()));
        this.stateField.put(CSVField.DENSITY,Comparator.comparing(census -> census.populationDensity,Comparator.reverseOrder()));
        this.stateField.put(CSVField.AREA,Comparator.comparing(census -> census.totalArea,Comparator.reverseOrder()));
    }

    public Map<String, CensusDAO> loadCensusData(Country country, String... csvFilePath) throws CensusAnalyserException {
        CensusAdapter censusAdapter = CensusAnalyserFactory.loadCensusData(country);
        this.country = country;
        return censusAdapter.loadCensusData(country,csvFilePath);
    }

    public String getSortedCensusData(Map<String, CensusDAO> daoMap, CSVField field) throws CensusAnalyserException {
        if(daoMap == null || daoMap.size()==0) {
            throw new CensusAnalyserException("No census data", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }

        ArrayList censusDAOS = daoMap.values().stream()
                .sorted(this.stateField.get(field))
                .map(censusDAO -> censusDAO.getCensusDTO(country))
                .collect(toCollection(ArrayList::new));

        String sortedStateCensusJson = new Gson().toJson(censusDAOS);
        return sortedStateCensusJson;
    }
}
