package censusanalyser;

import com.google.gson.Gson;

import java.util.*;
import java.util.stream.Collectors;

public class CensusAnalyser {
    public enum Country {INDIA,US}
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
        return censusAdapter.loadCensusData(country,csvFilePath);
    }

    public String getSortedCensusData(Map<String, CensusDAO> map, CSVField field) throws CensusAnalyserException {
        if(map == null || map.size()==0) {
            throw new CensusAnalyserException("No census data", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        List<CensusDAO> censusDAOS = map.values().stream().collect(Collectors.toList());
        this.sort(censusDAOS, this.stateField.get(field));
        String sortedStateCensusJson = new Gson().toJson(censusDAOS);
        return sortedStateCensusJson;
    }

    private void sort(List<CensusDAO> censusList, Comparator<CensusDAO> censusComparator) {
        for (int i=0;i<censusList.size();i++) {
            for (int j=0;j<censusList.size()-i-1;j++) {
                CensusDAO census1 = censusList.get(j);
                CensusDAO census2 = censusList.get(j+1);
                if (censusComparator.compare(census1,census2) > 0) {
                    censusList.set(j, census2);
                    censusList.set(j+1, census1);
                }
            }
        }
    }
}
