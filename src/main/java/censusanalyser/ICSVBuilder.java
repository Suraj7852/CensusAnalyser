package censusanalyser;

import java.io.Reader;
import java.util.Iterator;
import java.util.List;

public interface ICSVBuilder<E> {
    public <E> Iterator<E> getCSVFileIterator(Reader reader, Class csvClass) throws CSVBuilderException;
    public <E> List<E> getCSVFileList(Reader reader, Class csvClass) throws CSVBuilderException;
}
