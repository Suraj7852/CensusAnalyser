package censusanalyser;

import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

public class USCensusAdapterTest {

    private static final String US_CENSUS_CSV_FILE_PATH = "./src/test/resources/USCensusData.csv";
    private static final String WRONG_CSV_FILE_PATH = "./src/main/resources/IndiaStateCensusData.csv";
    @Test
    public void givenUSCensusCSV_ShouldReturnExactCount() {
        USCensusAdapter usCensusAdapter = new USCensusAdapter();
        try {
            Map<String, CensusDAO> usCensusData = usCensusAdapter.loadCensusData(CensusAnalyser.Country.US,US_CENSUS_CSV_FILE_PATH);
            Assert.assertEquals(51,usCensusData.size());
        } catch (CensusAnalyserException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void givenUSCensusCSV_ThrowsExceptionWhen_WhenWrongFilePathSent() {
        USCensusAdapter usCensusAdapter = new USCensusAdapter();
        try {
            Map<String, CensusDAO> usCensusData = usCensusAdapter.loadCensusData(CensusAnalyser.Country.US,WRONG_CSV_FILE_PATH);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM,e.type);
        }
    }
}
