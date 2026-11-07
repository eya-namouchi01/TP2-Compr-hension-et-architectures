package hai913i.tp2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

// Step B3
class CutTest {

    static Cut cut(Coupling c, double cp) {
        Clustering cl = new Clustering(c.classes(), c);
        return new Cut(cl.root(), c, c.classes().size(), cp);
    }

    @Test
    void miniCp020GivesOneModule() throws IOException {
        Cut cut = cut(CouplingTest.mini(), 0.20);
        assertEquals(1, cut.modules().size());
        assertTrue(cut.respectsLimit());
    }

    @Test
    void miniCp050ViolatesLimit() throws IOException {
        Cut cut = cut(CouplingTest.mini(), 0.50);
        assertEquals(2, cut.modules().size());
        assertFalse(cut.respectsLimit());
    }

    @Test
    void validationCheckpoint() throws IOException {
        Coupling c = CouplingTest.validation();
        assertEquals(4, cut(c, 0.015).modules().size());
        assertEquals(7, cut(c, 0.025).modules().size());
        assertTrue(cut(c, 0.025).respectsLimit());
        assertEquals(10, cut(c, 0.045).modules().size());
        assertFalse(cut(c, 0.045).respectsLimit());
    }

    @Test
    void modulesCoverAllClassesAndRespectCp() throws IOException {
        Coupling c = CouplingTest.validation();
        double[] values = {0.015, 0.025, 0.045};
        for (double cp : values) {
            Cut cut = cut(c, cp);
            List<String> all = new ArrayList<>();
            for (Cluster m : cut.modules()) {
                all.addAll(m.members());
                if (m.members().size() >= 2) {
                    assertTrue(cut.internalCoupling(m) > cp);
                }
            }
            all.sort(null);
            // no class missing
            assertEquals(c.classes(), all);
        }
    }
}