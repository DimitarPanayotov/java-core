package bg.sofia.uni.fmi.mjt.olympics.comparator;

import bg.sofia.uni.fmi.mjt.olympics.MJTOlympics;

import java.util.Comparator;

public class NationMedalComparator implements Comparator<String> {

    private final MJTOlympics olympics;

    public NationMedalComparator(MJTOlympics olympics) {
        this.olympics = olympics;
    }

    @Override
    public int compare(String nation1, String nation2) {
        if (nation1 == null && nation2 == null) return 0;
        if (nation1 == null) return -1;
        if (nation2 == null) return 1;

        int totalMedals1 = olympics.getTotalMedals(nation1);
        int totalMedals2 = olympics.getTotalMedals(nation2);

        int medalComparison = Integer.compare(totalMedals2, totalMedals1);
        if (medalComparison != 0) {
            return medalComparison;
        }

        return nation2.compareTo(nation1);
    }
}