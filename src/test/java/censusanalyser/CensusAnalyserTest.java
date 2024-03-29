package censusanalyser;

import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Map;

public class CensusAnalyserTest {

    private static final String INDIA_CENSUS_CSV_FILE_PATH = "./src/test/resources/IndiaStateCensusData.csv";
    private static final String WRONG_CSV_FILE_PATH = "./src/main/resources/IndiaStateCensusData.csv";
    private static final String INDIA_STATE_CSV_FILE_PATH = "./src/test/resources/IndiaStateCode.csv";
    private static final String US_CENSUS_CSV_FILE_PATH = "./src/test/resources/USCensusData.csv";
    private static final String US_CENSUS_CSV_FILE_PATH_SORT = "./src/test/resources/USCensusDataForChecking.csv";
    private static final String US_CENSUS_CSV_FILE_PATH_SORT_DENSITY = "./src/test/resources/USCensusDataForGreaterDensity.csv";
    private static final String US_CENSUS_CSV_FILE_PATH_SORT_AREA = "./src/test/resources/USCensusDataGreaterArea.csv";

    CensusAnalyser censusAnalyser = new CensusAnalyser();
    @Test
    public void givenIndianCensusCSVFileReturnsCorrectRecords() {
        try {
            Map<String, CensusDAO> numOfRecords = censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA,INDIA_CENSUS_CSV_FILE_PATH);
            Assert.assertEquals(29,numOfRecords.size());
        } catch (CensusAnalyserException e) { }
    }

    @Test
    public void givenIndiaCensusData_WithWrongFile_ShouldThrowException() {
        try {
            ExpectedException exceptionRule = ExpectedException.none();
            exceptionRule.expect(CensusAnalyserException.class);
            censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA,WRONG_CSV_FILE_PATH);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM,e.type);
        }
    }

    @Test
    public void givenIndianStateCSV_ShouldReturnExactCount() {
        try {
            Map<String, CensusDAO> indiaCensusDataCount = censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA,INDIA_CENSUS_CSV_FILE_PATH, INDIA_STATE_CSV_FILE_PATH);
            Assert.assertEquals(29,indiaCensusDataCount.size());
        } catch (CensusAnalyserException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void givenIndianCensusData_WhenSortedOnState_ShouldReturnSortedResult() {
        try {
            Map<String,CensusDAO> map = censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA,INDIA_CENSUS_CSV_FILE_PATH,INDIA_STATE_CSV_FILE_PATH);
            String sortedCensusData = censusAnalyser.getSortedCensusData(map,CSVField.STATE);
            CensusDAO[] censusCSV = new Gson().fromJson(sortedCensusData, CensusDAO[].class);
            Assert.assertEquals("Andhra Pradesh",censusCSV[0].state);
        } catch (CensusAnalyserException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void givenIndianCensusData_WhenFilePath_NotGIven_ThrowsException() {
        try {
            Map<String,CensusDAO> map = censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA,INDIA_CENSUS_CSV_FILE_PATH,INDIA_STATE_CSV_FILE_PATH);
            String sortedCensusData = censusAnalyser.getSortedCensusData(map, CSVField.STATE);
            IndiaCensusCSV[] censusCSV = new Gson().fromJson(sortedCensusData, IndiaCensusCSV[].class);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.NO_CENSUS_DATA,e.type);
        }
    }

    @Test
    public void givenIndianCensusData_WhenWrongHeader_throwsException() {
        try {
            Map<String,CensusDAO> map = censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA,INDIA_CENSUS_CSV_FILE_PATH,INDIA_STATE_CSV_FILE_PATH);
            censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA,"/home/admin1/Desktop/suraj/CensusAnalyser/CensusAnalyser/src/test/resources/IndiaStateCensusWrongHeader.csv");
            String sortedCensusData = censusAnalyser.getSortedCensusData(map, CSVField.STATE);
            IndiaCensusCSV[] censusCSV = new Gson().fromJson(sortedCensusData, IndiaCensusCSV[].class);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.HEADER_MISMATCH,e.type);
        }
    }

    @Test
    public void givenIndianCensusData_WhenFilePath_NotPassed_throwsException() {
        try {
            Map<String,CensusDAO> map = censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA,INDIA_CENSUS_CSV_FILE_PATH,INDIA_STATE_CSV_FILE_PATH);
            censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA,"",INDIA_CENSUS_CSV_FILE_PATH);
            String sortedCensusData = censusAnalyser.getSortedCensusData(map, CSVField.STATE);
            IndiaCensusCSV[] censusCSV = new Gson().fromJson(sortedCensusData, IndiaCensusCSV[].class);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.HEADER_MISMATCH,e.type);
        }
    }

    @Test
    public void givenIndianCensusCode_WhenFilePath_NotPassed_throwsException() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA,INDIA_CENSUS_CSV_FILE_PATH,"");
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.UNABLE_TO_CAPTURE_CSV_HEADER,e.type);
        }
    }

    @Test
    public void givenIndianCensusCode_WhenWrongFilePath_throwsException() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA,"/home/admin1/Desktop/suraj/CensusAnalyser/CensusAnalysersrc/test/resources/IndiaStateCensusData.csv");
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM,e.type);
        }
    }

    @Test
    public void givenIndianCensusCode_WhenHeaderMismatch_throwsExcetion() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA,"/home/admin1/Desktop/suraj/CensusAnalyser/CensusAnalyser/src/test/resources/IndiaStateCodeWrongHeader.csv");
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.HEADER_MISMATCH,e.type);
        }
    }

    @Test
    public void givenUSCensusCSV_ShouldReturnExactCount() {
        CensusAnalyser censusAnalyser = new CensusAnalyser();
        try {
            Map<String, CensusDAO> usCensusData = censusAnalyser.loadCensusData(CensusAnalyser.Country.US,US_CENSUS_CSV_FILE_PATH);
            Assert.assertEquals(51,usCensusData.size());
        } catch (CensusAnalyserException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void givenIndianCensusData_WhenSortedOnPopulation_ShouldReturnSortedResult() {
        try {
            Map<String,CensusDAO> map = censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA,INDIA_CENSUS_CSV_FILE_PATH,INDIA_STATE_CSV_FILE_PATH);
            String sortedCensusData = censusAnalyser.getSortedCensusData(map,CSVField.POPULATION);
            CensusDAO[] censusCSV = new Gson().fromJson(sortedCensusData, CensusDAO[].class);
            Assert.assertEquals("Uttar Pradesh",censusCSV[0].state);
        } catch (CensusAnalyserException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void givenIndianCensusData_WhenSortedOnPopulationDensity_ShouldReturnSortedResult() {
        try {
            Map<String,CensusDAO> map = censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA,INDIA_CENSUS_CSV_FILE_PATH,INDIA_STATE_CSV_FILE_PATH);
            String sortedCensusData = censusAnalyser.getSortedCensusData(map,CSVField.DENSITY);
            CensusDAO[] censusCSV = new Gson().fromJson(sortedCensusData, CensusDAO[].class);
            Assert.assertEquals("Bihar",censusCSV[0].state);
        } catch (CensusAnalyserException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void givenIndianCensusData_WhenSortedBasedOnArea_ShouldReturnSortedResult() {
        try {
            Map<String,CensusDAO> map = censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA,INDIA_CENSUS_CSV_FILE_PATH,INDIA_STATE_CSV_FILE_PATH);
            String sortedCensusData = censusAnalyser.getSortedCensusData(map,CSVField.AREA);
            CensusDAO[] censusCSV = new Gson().fromJson(sortedCensusData, CensusDAO[].class);
            Assert.assertEquals("Rajasthan",censusCSV[0].state);
        } catch (CensusAnalyserException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void givenUSCensusData_WhenSortedOnState_ShouldReturnSortedResult() {
        CensusAnalyser censusAnalyser = new CensusAnalyser();
        try {
            Map<String, CensusDAO> usCensusData = censusAnalyser.loadCensusData(CensusAnalyser.Country.US,US_CENSUS_CSV_FILE_PATH);
            String sortedCensusData = censusAnalyser.getSortedCensusData(usCensusData,CSVField.STATE);
            CensusDAO[] censusDAOS = new Gson().fromJson(sortedCensusData, CensusDAO[].class);
            Assert.assertEquals("Alabama",censusDAOS[0].state);
        } catch (CensusAnalyserException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void givenUSCensusData_WhenSortedOnPopulation_ShouldReturnSortedResult() {
        CensusAnalyser censusAnalyser = new CensusAnalyser();
        try {
            Map<String, CensusDAO> usCensusData = censusAnalyser.loadCensusData(CensusAnalyser.Country.US,US_CENSUS_CSV_FILE_PATH);
            String sortedCensusData = censusAnalyser.getSortedCensusData(usCensusData,CSVField.POPULATION);
            CensusDAO[] censusDAOS = new Gson().fromJson(sortedCensusData, CensusDAO[].class);
            Assert.assertEquals("Indiana",censusDAOS[0].state);
        } catch (CensusAnalyserException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void givenUSCensusData_WhenSortedOnArea_ShouldReturnSortedResult() {
        CensusAnalyser censusAnalyser = new CensusAnalyser();
        try {
            Map<String, CensusDAO> usCensusData = censusAnalyser.loadCensusData(CensusAnalyser.Country.US,US_CENSUS_CSV_FILE_PATH);
            String sortedCensusData = censusAnalyser.getSortedCensusData(usCensusData,CSVField.AREA);
            CensusDAO[] censusDAOS = new Gson().fromJson(sortedCensusData, CensusDAO[].class);
            Assert.assertEquals("Alaska",censusDAOS[0].state);
        } catch (CensusAnalyserException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void givenUSCensusData_WhenSortedOnPopulationDensity_ShouldReturnSortedResult() {
        CensusAnalyser censusAnalyser = new CensusAnalyser();
        try {
            Map<String, CensusDAO> usCensusData = censusAnalyser.loadCensusData(CensusAnalyser.Country.US,US_CENSUS_CSV_FILE_PATH);
            String sortedCensusData = censusAnalyser.getSortedCensusData(usCensusData,CSVField.DENSITY);
            CensusDAO[] censusDAOS = new Gson().fromJson(sortedCensusData, CensusDAO[].class);
            Assert.assertEquals("District of Columbia",censusDAOS[0].state);
        } catch (CensusAnalyserException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void givenIndianCensusCSVFile_WhenInvalidCountry_ThrowsException() {
        try {
            censusAnalyser.loadCensusData(CensusAnalyser.Country.PAKISTAN,  INDIA_CENSUS_CSV_FILE_PATH,WRONG_CSV_FILE_PATH);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.INVALID_COUNTRY,e.type);
        }
    }

    @Test
    public void givenIndianCensusData_ifCannotLoad_ThrowsException() {
        try {
            Map<String,CensusDAO> map=null;
            String sortedCensusData = censusAnalyser.getSortedCensusData(map,CSVField.DENSITY);
            CensusDAO[] censusCSV = new Gson().fromJson(sortedCensusData, CensusDAO[].class);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.NO_CENSUS_DATA,e.type);
        }
    }

    @Test
    public void givenIndianCensusData_mostPopulousStateWithDensity() {
        try {
            Map<String, CensusDAO> usCensusData = censusAnalyser.loadCensusData(CensusAnalyser.Country.US,US_CENSUS_CSV_FILE_PATH);
            String state = censusAnalyser.mostPopulousState(usCensusData, CSVField.POPULATION);
            Assert.assertEquals("California",state);
        } catch (CensusAnalyserException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void givenIndianCensusData_mostPopulousStateWithDensity_ForSameArea() {
        try {
            Map<String, CensusDAO> usCensusData = censusAnalyser.loadCensusData(CensusAnalyser.Country.US,US_CENSUS_CSV_FILE_PATH_SORT);
            String state = censusAnalyser.mostPopulousState(usCensusData, CSVField.POPULATION);
            Assert.assertEquals("Indiana",state);
        } catch (CensusAnalyserException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void givenIndianCensusData_mostPopulousStateWithDensity_ForSameArea_ButGreaterDensity() {
        try {
            Map<String, CensusDAO> usCensusData = censusAnalyser.loadCensusData(CensusAnalyser.Country.US,US_CENSUS_CSV_FILE_PATH_SORT_DENSITY);
            String state = censusAnalyser.mostPopulousState(usCensusData, CSVField.POPULATION);
            Assert.assertEquals("California",state);
        } catch (CensusAnalyserException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void givenIndianCensusData_mostPopulousStateWithDensity_ForSameArea_ButGreaterArea() {
        try {
            Map<String, CensusDAO> usCensusData = censusAnalyser.loadCensusData(CensusAnalyser.Country.US,US_CENSUS_CSV_FILE_PATH_SORT_AREA);
            String state = censusAnalyser.mostPopulousState(usCensusData, CSVField.POPULATION);
            Assert.assertEquals("Indiana",state);
        } catch (CensusAnalyserException e) {
            e.printStackTrace();
        }
    }
}
