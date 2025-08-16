package bg.sofia.uni.fmi.mjt.olympics.competitor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class AthleteTest {

    @Test
    void testAthleteConstructor() {
        Athlete athlete = new Athlete("A1", "Usain Bolt", "Jamaica");

        assertEquals("A1", athlete.getIdentifier());
        assertEquals("Usain Bolt", athlete.getName());
        assertEquals("Jamaica", athlete.getNationality());
        assertTrue(athlete.getMedals().isEmpty());
    }

    @Test
    void testAddMedal() {
        Athlete athlete = new Athlete("A1", "Usain Bolt", "Jamaica");
        athlete.addMedal(Medal.GOLD);

        assertEquals(1, athlete.getMedals().size());
        assertTrue(athlete.getMedals().contains(Medal.GOLD));
    }

    @Test
    void testAddNullMedal() {
        Athlete athlete = new Athlete("A1", "Usain Bolt", "Jamaica");

        try {
            athlete.addMedal(null);
        } catch (Exception e) {
            assertEquals(IllegalArgumentException.class, e.getClass());
        }
    }

    @Test
    void testGetMedalsReturnsUnmodifiableCollection() {
        Athlete athlete = new Athlete("A1", "Usain Bolt", "Jamaica");
        Collection<Medal> medals = athlete.getMedals();

        try {
            medals.add(Medal.GOLD);
        } catch (Exception e) {
            assertEquals(UnsupportedOperationException.class, e.getClass());
        }
    }

    @Test
    void testEqualsAndHashCode() {
        Athlete athlete1 = new Athlete("A1", "Usain Bolt", "Jamaica");
        Athlete athlete2 = new Athlete("A1", "Usain Bolt", "Jamaica");
        Athlete athlete3 = new Athlete("A2", "Michael Phelps", "USA");

        athlete1.addMedal(Medal.GOLD);
        athlete2.addMedal(Medal.GOLD);

        assertEquals(athlete1, athlete2);
        assertNotEquals(athlete1, athlete3);
        assertEquals(athlete1.hashCode(), athlete2.hashCode());
        assertNotEquals(athlete1.hashCode(), athlete3.hashCode());
    }
}