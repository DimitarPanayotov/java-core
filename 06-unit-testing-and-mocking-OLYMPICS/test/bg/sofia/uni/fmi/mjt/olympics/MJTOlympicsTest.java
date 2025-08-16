package bg.sofia.uni.fmi.mjt.olympics;

import bg.sofia.uni.fmi.mjt.olympics.competition.Competition;
import bg.sofia.uni.fmi.mjt.olympics.competition.CompetitionResultFetcher;
import bg.sofia.uni.fmi.mjt.olympics.competitor.Athlete;
import bg.sofia.uni.fmi.mjt.olympics.competitor.Competitor;
import bg.sofia.uni.fmi.mjt.olympics.competitor.Medal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MJTOlympicsTest {

    @Mock
    private CompetitionResultFetcher resultFetcher;

    private Set<Competitor> competitors;
    private MJTOlympics olympics;

    @BeforeEach
    void setUp() {
        Competitor competitor1 = new Athlete("A1", "Athlete 1", "USA");
        Competitor competitor2 = new Athlete("A2", "Athlete 2", "USA");
        Competitor competitor3 = new Athlete("A3", "Athlete 3", "CHN");

        competitors = new HashSet<>();
        competitors.add(competitor1);
        competitors.add(competitor2);
        competitors.add(competitor3);

        olympics = new MJTOlympics(competitors, resultFetcher);
    }

    @Test
    void testUpdateMedalStatisticsWithValidCompetition() {
        Competition competition = mock(Competition.class);
        when(competition.competitors()).thenReturn(competitors);

        TreeSet<Competitor> ranking = new TreeSet<>(new Comparator<Competitor>() {
            @Override
            public int compare(Competitor c1, Competitor c2) {
                return c1.getIdentifier().compareTo(c2.getIdentifier());
            }
        });
        ranking.addAll(competitors);

        when(resultFetcher.getResult(competition)).thenReturn(ranking);

        olympics.updateMedalStatistics(competition);

        for (Competitor c : competitors) {
            assertFalse(c.getMedals().isEmpty());
        }

        assertTrue(olympics.getNationsMedalTable().containsKey("USA"));
        assertTrue(olympics.getNationsMedalTable().containsKey("CHN"));
    }

    @Test
    void testUpdateMedalStatisticsWithNullCompetition() {
        try {
            olympics.updateMedalStatistics(null);
        } catch (Exception e) {
            assertEquals(IllegalArgumentException.class, e.getClass());
        }
    }

    @Test
    void testUpdateMedalStatisticsWithUnregisteredCompetitors() {
        Competitor unregistered = new Athlete("A4", "Unregistered", "RUS");
        Set<Competitor> unregisteredCompetitors = new HashSet<>();
        unregisteredCompetitors.add(unregistered);

        Competition competition = mock(Competition.class);
        when(competition.competitors()).thenReturn(unregisteredCompetitors);

        try {
            olympics.updateMedalStatistics(competition);
        } catch (Exception e) {
            assertEquals(IllegalArgumentException.class, e.getClass());
        }
    }

    @Test
    void testGetNationsRankList() {
        Athlete a1 = new Athlete("A1", "Athlete 1", "USA");
        Athlete a2 = new Athlete("A2", "Athlete 2", "GER");
        Set<Competitor> competitors = new HashSet<>();
        competitors.add(a1);
        competitors.add(a2);

        CompetitionResultFetcher fetcher = mock(CompetitionResultFetcher.class);
        when(fetcher.getResult(any())).thenAnswer(new Answer<TreeSet<Competitor>>() {
            @Override
            public TreeSet<Competitor> answer(InvocationOnMock inv) throws Throwable {
                TreeSet<Competitor> result = new TreeSet<>(new Comparator<Competitor>() {
                    @Override
                    public int compare(Competitor c1, Competitor c2) {
                        return c1.getIdentifier().compareTo(c2.getIdentifier());
                    }
                });
                result.addAll(((Competition) inv.getArgument(0)).competitors());
                return result;
            }
        });

        MJTOlympics olympics = new MJTOlympics(competitors, fetcher);

        Competition comp = new Competition("Test", "Test", competitors);
        olympics.updateMedalStatistics(comp);

        TreeSet<String> ranking = olympics.getNationsRankList();
        assertFalse(ranking.isEmpty());
    }

    @Test
    void testGetTotalMedals() {
        Athlete a1 = new Athlete("A1", "Athlete 1", "USA");
        Set<Competitor> competitors = new HashSet<>();
        competitors.add(a1);

        CompetitionResultFetcher fetcher = mock(CompetitionResultFetcher.class);
        when(fetcher.getResult(any())).thenAnswer(new Answer<TreeSet<Competitor>>() {
            @Override
            public TreeSet<Competitor> answer(InvocationOnMock inv) throws Throwable {
                TreeSet<Competitor> result = new TreeSet<>(new Comparator<Competitor>() {
                    @Override
                    public int compare(Competitor c1, Competitor c2) {
                        return c1.getIdentifier().compareTo(c2.getIdentifier());
                    }
                });
                result.addAll(((Competition) inv.getArgument(0)).competitors());
                return result;
            }
        });

        MJTOlympics olympics = new MJTOlympics(competitors, fetcher);

        Competition comp = new Competition("Test", "Test", competitors);
        olympics.updateMedalStatistics(comp);

        assertEquals(1, olympics.getTotalMedals("USA"));
    }

    @Test
    void testGetTotalMedalsWithNullNationality() {
        try {
            olympics.getTotalMedals(null);
        } catch (Exception e) {
            assertEquals(IllegalArgumentException.class, e.getClass());
        }
    }

    @Test
    void testGetTotalMedalsWithUnregisteredNationality() {
        try {
            olympics.getTotalMedals("RUS");
        } catch (Exception e) {
            assertEquals(IllegalArgumentException.class, e.getClass());
        }
    }

    @Test
    void testGetRegisteredCompetitors() {
        assertEquals(3, olympics.getRegisteredCompetitors().size());

        try {
            olympics.getRegisteredCompetitors().add(mock(Competitor.class));
        } catch (Exception e) {
            assertEquals(UnsupportedOperationException.class, e.getClass());
        }
    }

    @Test
    void testGetNationsMedalTable() {
        assertTrue(olympics.getNationsMedalTable().isEmpty());

        try {
            olympics.getNationsMedalTable().put("TEST", new EnumMap<>(Medal.class));
        } catch (Exception e) {
            assertEquals(UnsupportedOperationException.class, e.getClass());
        }
    }
}