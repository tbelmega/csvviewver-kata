package de.belmega.csvviewer;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class CsvData {
    public static final String LINE_SEPERATOR = "\n";
    public static final String COLUMN_SEPARATOR = ";";
    public static final String MENU_LINE = "N(ext page, P(revious page, F(irst page, L(ast page, eX(it";
    private final String[] alleZeilen;
    private final int linesPerPage;

    public CsvData(String data, int numberOfLinesPerPage) {
        this.alleZeilen = data.split(LINE_SEPERATOR);
        this.linesPerPage = numberOfLinesPerPage;
    }

    public int getNumberOfColumns() {
        // Zerlege erste Zeile nach Semikolon
        String[] kopfzeile = alleZeilen[0].split(COLUMN_SEPARATOR);
        // Ergebnis ist ein Array von Elementen; die Länge dieses Arrays ist die Anzahl der Spalten
        return kopfzeile.length;
    }

    public int[] calculateColumnWidthsForPage(int pageNumber) {
        String[] page = getPage(pageNumber);

        // create an Array of correct size. all fields are initialized with 0.
        int[] maxWidths = new int[getNumberOfColumns()];

        for (int i = 0; i < page.length; i++) { // Für jede Zeile ...

            String[] zellen = alleZeilen[i].split(COLUMN_SEPARATOR);

            for (int j = 0; j < zellen.length; j++) { // ... betrachte jede Spalte.
                if (maxWidths[j] < zellen[j].length()) // Wenn größer als bisheriges Maximum
                    maxWidths[j] = zellen[j].length(); // Setze neues Maximum
            }
        }

        return maxWidths;
    }

    /** Create a page of raw data from all data. */
    private String[] getPage(int pageNumber) {
        // Erzeuge leeres Array für die Page
        String[] page = new String[linesPerPage + 1];

        // Übernimm Titelzeile
        page[0] = alleZeilen[0];

        // Übernimm alle Zeilen beginnend bei startAtRow
        int startAtRow = ((pageNumber - 1) * linesPerPage);
        for (int i = 1; i < page.length; i++) {
            page[i] = alleZeilen[i + startAtRow];
        }

        return page;
    }

    public int getNumberOfPages() {
        int numberOfPages = alleZeilen.length / linesPerPage;
        // Angefangene Seite aufrunden (Math.ceil)
        int totalNumberOfPages = (int) Math.ceil(numberOfPages);
        return totalNumberOfPages;
    }

    /** Convert page of raw data to formatted data. */
    public String getPageFormatted(int pageNumber) {
        String[] pageZeilen = getPage(pageNumber);
        int[] columnWidths = calculateColumnWidthsForPage(pageNumber);

        StringBuilder prettyPrinted = new StringBuilder()
                .append(formatLine(pageZeilen[0], columnWidths)) // Titelzeile
                .append(headerSeparationLine(columnWidths)); // Titel-Trennzeile

        for (int i = 1; i < pageZeilen.length; i++) {
            prettyPrinted.append(formatLine(pageZeilen[i], columnWidths)); //Datenzeilen
        }

        prettyPrinted.append(MENU_LINE); // Menüzeile
        return prettyPrinted.toString();
    }

    /** Create a line with dashes and plus characters to separate headline from data.*/
    private String headerSeparationLine(int[] columnWidths) {
        StringBuilder lineBuilder = new StringBuilder();

        for (int i = 0; i < columnWidths.length; i++) {
            String dashes = formatCell("", columnWidths[i], '-');
            lineBuilder.append(dashes).append('+');
        }

        return lineBuilder.toString() + LINE_SEPERATOR;
    }

    /** Fill every cell of this line with space characters, to match the intended column width. */
    private String formatLine(String zeile, int[] columnWidths) {
        String[] zellen = zeile.split(COLUMN_SEPARATOR);

        StringBuilder lineBuilder = new StringBuilder();

        for (int i = 0; i < zellen.length; i++) {
            lineBuilder.append(formatCell(zellen[i], columnWidths[i], ' ')).append("|");
        }
        return lineBuilder.toString() + LINE_SEPERATOR;
    }

    /** Fill this cell with the given filler char, to match the intended column width. */
    private String formatCell(String cellValue, int columnWidth, char fillerChar) {
        int numberOfSpaces = columnWidth - cellValue.length();
        StringBuilder cellValueBuilder = new StringBuilder(cellValue);
        for (int i = 0; i < numberOfSpaces; i++) {
            cellValueBuilder.append(fillerChar);
        }
        cellValue = cellValueBuilder.toString();
        return cellValue;
    }

    public static void main(String[] args) throws IOException {
        int numberOfLinesPerPage = determineNumberOfPages(args);

        InputStream resourceAsStream = CsvData.class.getClassLoader().getResourceAsStream("besucher.csv");
        String data = IOUtils.toString(resourceAsStream, "utf-8");

        CsvData csv = new CsvData(data, numberOfLinesPerPage);

        runMainMenu(csv);
    }

    private static void runMainMenu(CsvData csv) {
        int currentPage = 2;
        char userInput = 'F';
        while (userInput != 'X') {
            String pageFormatted = csv.getPageFormatted(currentPage);
            System.out.println(pageFormatted);

            userInput = new Scanner(System.in).next().charAt(0);

            switch (userInput) {
                case 'F': currentPage = 1; break;
                case 'N': currentPage = Math.min(currentPage + 1, csv.getNumberOfPages()); break;
                case 'P': currentPage = Math.max(currentPage - 1, 1); break;
                case 'L': currentPage = csv.getNumberOfPages(); break;
            }
        }
    }

    private static int determineNumberOfPages(String[] args) {
        int numberOfLinesPerPage = 100;
        if (args.length > 0) {
            // Wenn Nutzer eine Zahl als Programmargument übergibt,
            // nutze diese für Anzahl der Zeilen pro Page
            numberOfLinesPerPage = Integer.parseInt(args[0]);
        }
        return numberOfLinesPerPage;
    }
}
