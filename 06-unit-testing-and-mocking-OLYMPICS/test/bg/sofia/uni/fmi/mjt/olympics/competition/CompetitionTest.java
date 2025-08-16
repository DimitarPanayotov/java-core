package bg.sofia.uni.fmi.mjt.olympics.competition;

import bg.sofia.uni.fmi.mjt.olympics.competitor.Competitor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class CompetitionTest {

    @Test
    void testCompetitionConstructorWithValidParameters() {
        Set<Competitor> competitors = new HashSet<>();
        competitors.add(mock(Competitor.class));
        Competition competition = new Competition("100m Sprint", "Athletics", competitors);

        assertEquals("100m Sprint", competition.name());
        assertEquals("Athletics", competition.discipline());
        assertEquals(1, competition.competitors().size());
    }

    @Test
    void testCompetitionConstructorWithNullName() {
        Set<Competitor> competitors = new HashSet<>();
        competitors.add(mock(Competitor.class));

        try {
            new Competition(null, "Athletics", competitors);
        } catch (Exception e) {
            assertEquals(IllegalArgumentException.class, e.getClass());
        }
    }

    @Test
    void testCompetitionConstructorWithBlankName() {
        Set<Competitor> competitors = new HashSet<>();
        competitors.add(mock(Competitor.class));

        try {
            new Competition("", "Athletics", competitors);
        } catch (Exception e) {
            assertEquals(IllegalArgumentException.class, e.getClass());
        }
    }

    @Test
    void testCompetitionConstructorWithNullDiscipline() {
        Set<Competitor> competitors = new HashSet<>();
        competitors.add(mock(Competitor.class));

        try {
            new Competition("100m Sprint", null, competitors);
        } catch (Exception e) {
            assertEquals(IllegalArgumentException.class, e.getClass());
        }
    }

    @Test
    void testCompetitionConstructorWithBlankDiscipline() {
        Set<Competitor> competitors = new HashSet<>();
        competitors.add(mock(Competitor.class));

        try {
            new Competition("100m Sprint", "", competitors);
        } catch (Exception e) {
            assertEquals(IllegalArgumentException.class, e.getClass());
        }
    }

    @Test
    void testCompetitionConstructorWithNullCompetitors() {
        try {
            new Competition("100m Sprint", "Athletics", null);
        } catch (Exception e) {
            assertEquals(IllegalArgumentException.class, e.getClass());
        }
    }

    @Test
    void testCompetitionConstructorWithEmptyCompetitors() {
        Set<Competitor> emptySet = new HashSet<>();

        try {
            new Competition("100m Sprint", "Athletics", emptySet);
        } catch (Exception e) {
            assertEquals(IllegalArgumentException.class, e.getClass());
        }
    }

    @Test
    void testCompetitorsReturnsUnmodifiableSet() {
        Set<Competitor> competitors = new HashSet<>();
        competitors.add(mock(Competitor.class));
        Competition competition = new Competition("100m Sprint", "Athletics", competitors);

        try {
            competition.competitors().add(mock(Competitor.class));
        } catch (Exception e) {
            assertEquals(UnsupportedOperationException.class, e.getClass());
        }
    }

    @Test
    void testEqualsAndHashCode() {
        Set<Competitor> competitors1 = new HashSet<>();
        competitors1.add(mock(Competitor.class));

        Set<Competitor> competitors2 = new HashSet<>();
        competitors2.add(mock(Competitor.class));

        Competition competition1 = new Competition("100m Sprint", "Athletics", competitors1);
        Competition competition2 = new Competition("100m Sprint", "Athletics", competitors2);
        Competition competition3 = new Competition("200m Sprint", "Athletics", competitors1);

        assertEquals(competition1, competition2);
        assertNotEquals(competition1, competition3);
        assertEquals(competition1.hashCode(), competition2.hashCode());
        assertNotEquals(competition1.hashCode(), competition3.hashCode());
    }
}