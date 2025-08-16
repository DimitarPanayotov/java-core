package bg.sofia.uni.fmi.mjt.olympics.comparator;

import bg.sofia.uni.fmi.mjt.olympics.MJTOlympics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NationMedalComparatorTest {

    @Mock
    private MJTOlympics olympics;

    private NationMedalComparator comparator;

    @BeforeEach
    void setUp() {
        comparator = new NationMedalComparator(olympics);
    }

    @Test
    void testCompareWithDifferentMedalCounts() {
        when(olympics.getTotalMedals("USA")).thenReturn(10);
        when(olympics.getTotalMedals("China")).thenReturn(8);

        assertTrue(comparator.compare("USA", "China") < 0); // USA should come before China
        assertTrue(comparator.compare("China", "USA") > 0);
    }

    @Test
    void testCompareWithEqualMedalCounts() {
        when(olympics.getTotalMedals("USA")).thenReturn(10);
        when(olympics.getTotalMedals("GBR")).thenReturn(10);

        assertTrue(comparator.compare("GBR", "USA") > 0); // GBR comes after USA alphabetically
        assertTrue(comparator.compare("USA", "GBR") < 0);
    }

    @Test
    void testCompareWithSameNation() {
        when(olympics.getTotalMedals("USA")).thenReturn(10);
        assertEquals(0, comparator.compare("USA", "USA"));
    }
}
