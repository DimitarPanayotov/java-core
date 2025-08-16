package bg.sofia.uni.fmi.mjt.olympics.competition;

import bg.sofia.uni.fmi.mjt.olympics.competitor.Athlete;
import bg.sofia.uni.fmi.mjt.olympics.competitor.Competitor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Comparator;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CompetitionResultFetcherTest {

    @Test
    void testCompetitionResultFetcherImplementation() {
        CompetitionResultFetcher fetcher = mock(CompetitionResultFetcher.class);
        Competition competition = mock(Competition.class);
        TreeSet<Competitor> expectedResult = new TreeSet<>(Comparator.comparing(Competitor::getName));

        Competitor c1 = new Athlete("A1", "Zoe", "USA");
        Competitor c2 = new Athlete("A2", "Alice", "CHN");
        expectedResult.add(c1);
        expectedResult.add(c2);

        when(fetcher.getResult(competition)).thenReturn(expectedResult);

        TreeSet<Competitor> result = fetcher.getResult(competition);

        assertEquals(2, result.size());
        assertEquals("Alice", result.first().getName()); // First in alphabetical order
        assertEquals("Zoe", result.last().getName());
    }
}