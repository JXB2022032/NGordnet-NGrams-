package ngordnet.main;

import ngordnet.hugbrowsermagic.NgordnetQuery;
import ngordnet.hugbrowsermagic.NgordnetQueryHandler;
import ngordnet.ngrams.NGramMap;

import java.util.List;

public class HistoryTextHandler extends NgordnetQueryHandler {

    private NGramMap map;
    public HistoryTextHandler(NGramMap map) {
        this.map = map;
    }

    public String handle(NgordnetQuery q) {
        String response = "";
        List<String> words = q.words();
        int startYear = q.startYear();
        int endYear = q.endYear();
        for (String word: words) {
           response += word + ": ";
           response += this.map.weightHistory(word, startYear, endYear).toString() + "\n";
        }
        return response;
    }


}
