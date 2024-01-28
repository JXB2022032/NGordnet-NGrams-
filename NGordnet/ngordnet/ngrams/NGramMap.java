package ngordnet.ngrams;

import edu.princeton.cs.algs4.In;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/** An object that provides utility methods for making queries on the
 *  Google NGrams dataset (or a subset thereof).
 *
 *  An NGramMap stores pertinent data from a "words file" and a "counts
 *  file". It is not a map in the strict sense, but it does provide additional
 *  functionality.
 *
 *  @author Josh Hug
 */
public class NGramMap {
    // Maps each word to its time series (yearly counts)
    private Map<String, TimeSeries> wordHistories = new HashMap<>();

    // Stores the total word counts per year
    private TimeSeries totalCounts = new TimeSeries();

    /** Constructs an NGramMap from WORDS-FILENAME and COUNTS-FILENAME. */
    public NGramMap(String wordsFilename, String countsFilename) {
        loadWords(wordsFilename);
        countWords(countsFilename);
    }

    private void loadWords(String wordsFilename) {
        In in = new In(wordsFilename);
        while (in.hasNextLine()) {
            String line = in.readLine();
            String[] tokens = line.split("\\s+");
            String word = tokens[0];
            int year = Integer.parseInt(tokens[1]);
            double count = Double.parseDouble(tokens[2]);

            // If the word already exists, update its TimeSeries, otherwise create a new TimeSeries
            if (!wordHistories.containsKey(word)) {
                TimeSeries ts = new TimeSeries();
                ts.put(year, count);
                wordHistories.put(word, ts);
            } else {
                wordHistories.get(word).put(year, count);
            }
        }
    }

    private void countWords(String countsFilename) {
        In in = new In(countsFilename);
        while (in.hasNextLine()) {
            String line = in.readLine();
            String[] tokens = line.split(",");
            int year = Integer.parseInt(tokens[0]);
            double numberOfWords = Double.parseDouble(tokens[1]);
            totalCounts.put(year, numberOfWords);
        }
    }

    /** Provides the history of WORD. The returned TimeSeries should be a copy,
     *  not a link to this NGramMap's TimeSeries. In other words, changes made
     *  to the object returned by this function should not also affect the
     *  NGramMap. This is also known as a "defensive copy". */
    public TimeSeries countHistory(String word) {
        TimeSeries ts;
        if (wordHistories.containsKey(word)) {
            ts = wordHistories.get(word);
            return ts;
        }
        return new TimeSeries();
    }

    /** Provides the history of WORD between STARTYEAR and ENDYEAR, inclusive of both ends. The
     *  returned TimeSeries should be a copy, not a link to this NGramMap's TimeSeries. In other words,
     *  changes made to the object returned by this function should not also affect the
     *  NGramMap. This is also known as a "defensive copy". */
    public TimeSeries countHistory(String word, int startYear, int endYear) {
        return new TimeSeries(wordHistories.get(word), startYear, endYear);
    }

    /** Returns a defensive copy of the total number of words recorded per year in all volumes. */
    public TimeSeries totalCountHistory() {
        return this.totalCounts;
    }

    /** Provides a TimeSeries containing the relative frequency per year of WORD compared to
     *  all words recorded in that year. */
    public TimeSeries weightHistory(String word) {
        if (!wordHistories.get(word).isEmpty())
            return wordHistories.get(word).dividedBy(totalCounts);
        return new TimeSeries();
    }

    /** Provides a TimeSeries containing the relative frequency per year of WORD between STARTYEAR
     *  and ENDYEAR, inclusive of both ends. */
    public TimeSeries weightHistory(String word, int startYear, int endYear) {
        if (!wordHistories.get(word).isEmpty())
            return new TimeSeries(wordHistories.get(word), startYear, endYear).dividedBy(totalCounts);
        return new TimeSeries();
    }

    /** Returns the summed relative frequency per year of all words in WORDS. */
    public TimeSeries summedWeightHistory(Collection<String> words) {
        TimeSeries ts = new TimeSeries();
        for (String word: words) {
            ts.plus(weightHistory(word));
        }
        return ts;
    }

    /** Provides the summed relative frequency per year of all words in WORDS
     *  between STARTYEAR and ENDYEAR, inclusive of both ends. If a word does not exist in
     *  this time frame, ignore it rather than throwing an exception. */
    public TimeSeries summedWeightHistory(Collection<String> words, int startYear, int endYear) {
        TimeSeries ts = new TimeSeries();
        for (String word: words) {
            if (wordHistories.containsKey(word)) {
                ts = ts.plus(weightHistory(word, startYear, endYear));
            }
        }
        return ts;
    }


}
