package de.belmega.csvviewer;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

// Zeit, eine ganze Seite im vorgegebenen Format zu erhalten
public class CsvDataTest {

    public static final String TEST_DATA = 
            "Name;Age;City" + System.lineSeparator() +
            "Peter;42;New York" + System.lineSeparator() +
            "Paul;57;London" + System.lineSeparator() +
            "Mary;35;Munich" + System.lineSeparator() +
            "Jaques;66;Paris" + System.lineSeparator() +
            "Yuri;23;Moscow" + System.lineSeparator() +
            "Stephanie;47;Stockholm" + System.lineSeparator() +
            "Nadia;29;Madrid" + System.lineSeparator();

    public static final String FIRST_PAGE =
                    "Name |Age|City    |" + System.lineSeparator() +
                    "-----+---+--------+" + System.lineSeparator() +
                    "Peter|42 |New York|" + System.lineSeparator() +
                    "Paul |57 |London  |" + System.lineSeparator() +
                    "Mary |35 |Munich  |" + System.lineSeparator() +
                    "N(ext page, P(revious page, F(irst page, L(ast page, eX(it";
    
    @Test
    public void testThatTestDataIsRecognizedAs3Columns() throws Exception {
        //arrange
        CsvData csvData = new CsvData(TEST_DATA, 1);

        //act
        int numberOfColumns = csvData.getNumberOfColumns();

        //assert
        assertThat(numberOfColumns, is(equalTo(3)));
    }

    @Test
    public void testThatColumnWidthsAreDeterminedByLongestValuePerPage() throws Exception {
        //arrange
        CsvData csvData = new CsvData(TEST_DATA, 4);

        //act
        int[] widths = csvData.calculateColumnWidthsForPage(1);


        //assert
        assertThat(widths[0], is(equalTo(6)));
        assertThat(widths[1], is(equalTo(3)));
        assertThat(widths[2], is(equalTo(8)));
    }

    @Test
    public void testThatDataIsCutIntoPages() throws Exception {
        //arrange
        int numberOfLinesPerPage = 4;
        CsvData csvData = new CsvData(TEST_DATA, numberOfLinesPerPage);

        //act
        int pages = csvData.getNumberOfPages();

        //assert
        assertThat(pages, is(equalTo(2))); // 7 Zeilen bei 4 Zeilen pro Seite = 2
    }

    @Test
    public void testThatFirstPageIsPrettyPrinted() throws Exception {
        //arrange
        int numberOfLinesPerPage = 3;
        CsvData csvData = new CsvData(TEST_DATA, numberOfLinesPerPage);

        //act
        String firstPage = csvData.getPageFormatted(1);

        //assert
        assertThat(firstPage, is(equalTo(FIRST_PAGE)));
    }
}
