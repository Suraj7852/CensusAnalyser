package censusanalyser;

import CSVBuilder.CSVBuilderException;
import CSVBuilder.CSVBuilderFactory;
import CSVBuilder.ICSVBuilder;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class CensusAnalyser {
    Map<String,IndiaCensusDAO> censusStateMap = null;
    Map<CSVField,Comparator<IndiaCensusDAO>> stateField = null;

    public CensusAnalyser() {
        this.censusStateMap = new HashMap<>();
        this.stateField = new HashMap<>();
        this.stateField.put(CSVField.STATE,Comparator.comparing(census -> census.state));
    }

    public int loadIndiaCensusData(String csvFilePath) throws CensusAnalyserException {
        try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath))
                ){

            ICSVBuilder csvBuilder = CSVBuilderFactory.createCSVBuilder();
            Iterator<IndiaCensusCSV> csvFileIterator = csvBuilder.getCSVFileIterator(reader,IndiaCensusCSV.class);
            Iterable<IndiaCensusCSV> csvIterable = () -> csvFileIterator;
            StreamSupport.stream(csvIterable.spliterator(),false).forEach(censusCSV ->
                    censusStateMap.put(censusCSV.state,new IndiaCensusDAO(censusCSV)));

            return censusStateMap.size();
        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        } catch (CSVBuilderException e) {
           throw new CensusAnalyserException(e.getMessage(),CensusAnalyserException.ExceptionType.UNABLE_TO_PARSE);
        } catch (RuntimeException e) {
            throw new CensusAnalyserException(e.getMessage(),CensusAnalyserException.ExceptionType.HEADER_MISMATCH);
        }
    }

    public int loadIndiaCensusCode(String csvFilePath) throws CensusAnalyserException {
        int count=0;
        try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath))
        ){
            ICSVBuilder csvBuilder = CSVBuilderFactory.createCSVBuilder();
            Iterator<IndiaStateCodeCSV> stateCSVIterator = csvBuilder.
                                                               getCSVFileIterator(reader,IndiaStateCodeCSV.class);
            while (stateCSVIterator.hasNext()) {
                count++;
                IndiaStateCodeCSV stateCSV = stateCSVIterator.next();
                IndiaCensusDAO censusDAO = censusStateMap.get(stateCSV.state);
                if (censusDAO == null ) continue;
                censusDAO.stateCode = stateCSV.stateCode;
            }

            return count;
        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        } catch (CSVBuilderException e) {
            throw new CensusAnalyserException(e.getMessage(),CensusAnalyserException.ExceptionType.UNABLE_TO_PARSE);
        } catch (RuntimeException e) {
            throw new CensusAnalyserException(e.getMessage(),CensusAnalyserException.ExceptionType.UNABLE_TO_CAPTURE_CSV_HEADER);
        }
    }

    public String getStateWiseSortedCensusData(CSVField field) throws CensusAnalyserException {
        if(censusStateMap == null || censusStateMap.size()==0) {
            throw new CensusAnalyserException("No census data", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        List<IndiaCensusDAO> censusDAOS = censusStateMap.values().stream().collect(Collectors.toList());
        this.sort(censusDAOS, this.stateField.get(field));
        String sortedStateCensusJson = new Gson().toJson(censusDAOS);
        return sortedStateCensusJson;
    }

    private void sort(List<IndiaCensusDAO> censusList, Comparator<IndiaCensusDAO> censusComparator) {
        for (int i=0;i<censusList.size();i++) {
            for (int j=0;j<censusList.size()-i-1;j++) {
                IndiaCensusDAO census1 = censusList.get(j);
                IndiaCensusDAO census2 = censusList.get(j+1);
                if (censusComparator.compare(census1,census2) > 0) {
                    censusList.set(j, census2);
                    censusList.set(j+1, census1);
                }
            }
        }
    }
}
