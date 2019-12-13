package censusanalyser;

import java.util.Map;

public class CensusAnalyserFactory {
    public <E> Map<String, CensusDAO> loadCensusData(CensusAnalyser.Country country, String... csvFilePath) throws CensusAnalyserException {
        if (country.equals(CensusAnalyser.Country.INDIA)) {
            return new IndiaCensusAdapter().loadCensusData(CensusAnalyser.Country.INDIA, csvFilePath);
        } else if (country.equals(CensusAnalyser.Country.US)) {
            return new USCensusAdapter().loadCensusData(CensusAnalyser.Country.US, csvFilePath);
        }
        throw new CensusAnalyserException("Invalid Country",CensusAnalyserException.ExceptionType.INVALID_COUNTRY);
    }
}
