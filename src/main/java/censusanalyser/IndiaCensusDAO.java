package censusanalyser;

public class IndiaCensusDAO {
    String state;
    String stateCode;
    int areaInSqKm;
    int densityPerSqKm;
    int population;

    public IndiaCensusDAO(IndiaCensusCSV indiaCensusCSV) {
        state = indiaCensusCSV.state;
        areaInSqKm = indiaCensusCSV.areaInSqKm;
        densityPerSqKm = indiaCensusCSV.densityPerSqKm;
        population = indiaCensusCSV.population;
    }
}
