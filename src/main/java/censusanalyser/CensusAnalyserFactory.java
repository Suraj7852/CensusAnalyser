package censusanalyser;

public class CensusAnalyserFactory {
    public static <E> CensusAdapter loadCensusData(CensusAnalyser.Country country) throws CensusAnalyserException {
        if (country.equals(CensusAnalyser.Country.INDIA)) {
            return new IndiaCensusAdapter();
        } else if (country.equals(CensusAnalyser.Country.US)) {
            return new USCensusAdapter();
        }
        throw new CensusAnalyserException("Invalid Country",CensusAnalyserException.ExceptionType.INVALID_COUNTRY);
    }
}
