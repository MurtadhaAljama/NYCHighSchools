package com.nychighschools.model;

import android.content.Context;
import android.util.Log;

import com.nychighschools.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Schools
 */
public class Schools {

    /**
     * An array of School items.
     */
    public static final List<School> ITEMS = new ArrayList<School>();

    /**
     * A map of sample (School) items, by ID.
     */
    public static final Map<String, School> ITEM_MAP = new HashMap<String, School>();

    //display 20 lines only
    private static final int SIZE = 20;

    //load data
    public static void loadData(Context context){

        //load SAT data
        Map<String, Sat> SatMap = loadSATData(context);

        ITEM_MAP.clear();

        InputStream inputStream = context.getResources().openRawResource(R.raw.highschooldirectory);
        BufferedReader br = null;
        int size = 0;

        try {
            br = new BufferedReader(new InputStreamReader(inputStream));

            br.readLine(); //ignore header line

            String line = null;

            while (size < SIZE && (line = br.readLine()) != null) {

                List<String> data = parseLine(line, ',', '"');

                School school = new School(data.get(0), data.get(1).replace("\"", ""), data.get(18).replace("\"", ""));
                school.SAT = SatMap.get(data.get(0));
                addItem(school);

                size++;
            }

        }
        catch (IOException ioe) {
            Log.e("loadData", "Exception while reading input " + ioe);
        }
        finally {
            // close the stream
            try {
                if (br != null) {
                    br.close();
                }
            }
            catch (IOException ioe) {
                Log.e("loadData", "Exception while reading input " + ioe);
            }
        }
    }

    /**
     * load SAT data
     * @param context
     * @return
     */
    private static Map<String, Sat> loadSATData(Context context){

        Map<String, Sat> SAT_MAP = new HashMap<>();

        InputStream inputStream = context.getResources().openRawResource(R.raw.sat_results);
        BufferedReader br = null;

        try {
            br = new BufferedReader(new InputStreamReader(inputStream));

            br.readLine(); //ignore header line

            String line = null;

            while ((line = br.readLine()) != null) {

                List<String> data = parseLine(line, ',', '"');

                try{
                    SAT_MAP.put(data.get(0), new Sat(data.get(0), Integer.parseInt(data.get(2)), Integer.parseInt(data.get(3)), Integer.parseInt(data.get(4)), Integer.parseInt(data.get(5))));
                }catch(Exception e){

                }
            }

        }
        catch (IOException ioe) {
            Log.e("loadSATData", "Exception while reading input " + ioe);
        }
        finally {
            // close the stream
            try {
                if (br != null) {
                    br.close();
                }
            }
            catch (IOException ioe) {
                Log.e("loadSATData", "Exception while reading input " + ioe);
            }
        }

        return SAT_MAP;
    }

    private static void addItem(School item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static final char DEFAULT_SEPARATOR = ',';
    private static final char DEFAULT_QUOTE = '"';

    //code to extract the data
    //retrieved from https://www.mkyong.com/java/how-to-read-and-parse-csv-file-in-java/
    public static List<String> parseLine(String cvsLine, char separators, char customQuote) {

        List<String> result = new ArrayList<>();

        //if empty, return!
        if (cvsLine == null && cvsLine.isEmpty()) {
            return result;
        }

        if (customQuote == ' ') {
            customQuote = DEFAULT_QUOTE;
        }

        if (separators == ' ') {
            separators = DEFAULT_SEPARATOR;
        }

        StringBuffer curVal = new StringBuffer();
        boolean inQuotes = false;
        boolean startCollectChar = false;
        boolean doubleQuotesInColumn = false;

        char[] chars = cvsLine.toCharArray();

        for (char ch : chars) {

            if (inQuotes) {
                startCollectChar = true;
                if (ch == customQuote) {
                    inQuotes = false;
                    doubleQuotesInColumn = false;
                } else {

                    //Fixed : allow "" in custom quote enclosed
                    if (ch == '\"') {
                        if (!doubleQuotesInColumn) {
                            curVal.append(ch);
                            doubleQuotesInColumn = true;
                        }
                    } else {
                        curVal.append(ch);
                    }

                }
            } else {
                if (ch == customQuote) {

                    inQuotes = true;

                    //Fixed : allow "" in empty quote enclosed
                    if (chars[0] != '"' && customQuote == '\"') {
                        curVal.append('"');
                    }

                    //double quotes in column will hit this!
                    if (startCollectChar) {
                        curVal.append('"');
                    }

                } else if (ch == separators) {

                    result.add(curVal.toString());

                    curVal = new StringBuffer();
                    startCollectChar = false;

                } else if (ch == '\r') {
                    //ignore LF characters
                    continue;
                } else if (ch == '\n') {
                    //the end, break!
                    break;
                } else {
                    curVal.append(ch);
                }
            }

        }

        result.add(curVal.toString());

        return result;
    }


    /**
     * A School representing a piece of content.
     */
    public static class School {

        public final String id;
        public final String name;
        public final String location;
        public Sat SAT;


        public School(String id, String name, String location) {
            this.id = id;
            this.name = name;
            this.location = location;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getLocation() {
            return location;
        }
    }

    public static class Sat{

        public final String id;
        public final int numSatTestTakers;
        public final int criticalReadingAvgScore;
        public final int mathAvgScore;
        public final int writingAvgScore;

        public Sat(String id, int numSatTestTakers, int criticalReadingAvgScore, int mathAvgScore, int writingAvgScore) {
            this.id = id;
            this.numSatTestTakers = numSatTestTakers;
            this.criticalReadingAvgScore = criticalReadingAvgScore;
            this.mathAvgScore = mathAvgScore;
            this.writingAvgScore = writingAvgScore;
        }

    }
}
