package aspect.memorio.utils;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

public class Utils {

    private enum Measurement {
        YEAR(60 * 24 * 30 * 365, "year"),
        MONTH(60 * 24 * 30, "month"),
        WEEK(60 * 24 * 7, "week"),
        DAY(60 * 24, "day"),
        HOUR(60, "hour"),
        MINUTE(1, "minute");

        public final int multiplier;
        public final String label;

        Measurement(int multiplier, String label) {
            this.multiplier = multiplier;
            this.label = label;
        }
    }

    private static final List<Measurement> measurements = new ArrayList<Measurement>() {{
        add(Measurement.YEAR);
        add(Measurement.MONTH);
        add(Measurement.WEEK);
        add(Measurement.DAY);
        add(Measurement.HOUR);
        add(Measurement.MINUTE);
    }};

    public static String getTimeIntervalText(final long minutesDiff) {
        if (minutesDiff == 0) {
            return "Now";
        }

        for (int i = 0; i < measurements.size() - 1; ++i) {
            Measurement measurement = measurements.get(i);
            Measurement secondaryMeasurement = measurements.get(i + 1);
            if (minutesDiff >= measurement.multiplier) {
                int primaryCount = (int) minutesDiff / measurement.multiplier;
                int secondaryCount = (int) (minutesDiff - primaryCount * measurement.multiplier) / secondaryMeasurement.multiplier;
                String value = primaryCount + " " + measurement.label + "(s) ";
                if (secondaryCount > 0) {
                    value += secondaryCount + " " + secondaryMeasurement.label + "(s)";
                }

                return value;
            }
        }

        return minutesDiff + " " + measurements.get(measurements.size() - 1).label + "(s)";
    }

    public static String getTimeRemainingFromNowText(Date date) {
        long minutesDiff = (date.getTime() - new Date().getTime()) / (1000 * 60);
        return getTimeIntervalText(minutesDiff);
    }

}
