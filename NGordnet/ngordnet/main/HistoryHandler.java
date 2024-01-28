package ngordnet.main;

import ngordnet.hugbrowsermagic.NgordnetQuery;
import ngordnet.hugbrowsermagic.NgordnetQueryHandler;
import ngordnet.ngrams.NGramMap;
import ngordnet.ngrams.TimeSeries;
import ngordnet.plotting.Plotter;
import org.knowm.xchart.XYChart;

import java.util.ArrayList;
import java.util.List;

public class HistoryHandler extends NgordnetQueryHandler {
    private NGramMap plot;

    public HistoryHandler(NGramMap plot) {
        this.plot = plot;
    }

    public String handle(NgordnetQuery q) {
        String response = "";
        List<String> words = q.words();
        int startYear = q.startYear();
        int endYear = q.endYear();

        ArrayList<TimeSeries> lts = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();
        for (String word: words) {
            if (!plot.weightHistory(word).isEmpty()) {
                labels.add(word);
                lts.add(plot.weightHistory(word, startYear, endYear));
            }
        }
        XYChart chart = Plotter.generateTimeSeriesChart(labels, lts);

        return Plotter.encodeChartAsString(chart);
    }
}
