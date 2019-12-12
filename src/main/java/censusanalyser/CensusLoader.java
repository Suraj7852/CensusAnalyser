package censusanalyser;

import CSVBuilder.CSVBuilderException;
import CSVBuilder.CSVBuilderFactory;
import CSVBuilder.ICSVBuilder;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.StreamSupport;

public class CensusLoader {
    public  <E> Map<String, CensusDAO> loadCensusData(String csvFilePath, Class<E> censusCSVCLass) throws CensusAnalyserException {
        Map<String, CensusDAO> censusStateMap = new HashMap<>();
        try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath))
        ){

            ICSVBuilder csvBuilder = CSVBuilderFactory.createCSVBuilder();
            Iterator<E> csvFileIterator = csvBuilder.getCSVFileIterator(reader,censusCSVCLass);
            Iterable<E> csvIterable = () -> csvFileIterator;
            if (censusCSVCLass.getName().equals("censusanalyser.IndiaCensusCSV")) {
                StreamSupport.stream(csvIterable.spliterator(),false).
                        map(IndiaCensusCSV.class::cast).
                        forEach(censusCSV -> censusStateMap.put(censusCSV.state,new CensusDAO(censusCSV)));
            } else if (censusCSVCLass.getName().equals("censusanalyser.USCensusCSV")) {
                StreamSupport.stream(csvIterable.spliterator(),false).
                        map(USCensusCSV.class::cast).
                        forEach(censusCSV -> censusStateMap.put(censusCSV.state,new CensusDAO(censusCSV)));
            }

            return censusStateMap;
        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        } catch (CSVBuilderException e) {
            throw new CensusAnalyserException(e.getMessage(),CensusAnalyserException.ExceptionType.UNABLE_TO_PARSE);
        } catch (RuntimeException e) {
            throw new CensusAnalyserException(e.getMessage(),CensusAnalyserException.ExceptionType.HEADER_MISMATCH);
        }
    }
}
