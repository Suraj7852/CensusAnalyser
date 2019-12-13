package censusanalyser;

import com.opencsv.bean.CsvBindByName;

public class USCensusCSV {
    public USCensusCSV() {
    }

    @CsvBindByName(column = "State", required = true)
    public String state;

    @CsvBindByName(column = "State Id", required = true)
    public String stateId;

    @CsvBindByName(column = "Population", required = true)
    public int population;

    @CsvBindByName(column = "Population Density", required = true)
    public double populationDensity;

    @CsvBindByName(column = "land area", required = true)
    public double totalArea;

    public USCensusCSV(String state, String stateId, int population, double populationDensity, double totalArea) {
        this.state = state;
        this.stateId = stateId;
        this.population = population;
        this.populationDensity = populationDensity;
        this.totalArea = totalArea;
    }
}
