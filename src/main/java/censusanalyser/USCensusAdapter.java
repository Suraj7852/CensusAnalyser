package censusanalyser;

import java.util.Map;

public class USCensusAdapter extends CensusAdapter {
    @Override
    public <E> Map<String, CensusDAO> loadCensusData(CensusAnalyser.Country country, String... csvFilePath) throws CensusAnalyserException {
        Map<String, CensusDAO> loadCensusData = super.loadCensusData(USCensusCSV.class, csvFilePath);
        return loadCensusData;
    }
}
