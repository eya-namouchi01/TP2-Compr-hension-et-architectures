package hai913i.tp2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;

// A1 TEST on the mini dataset and on the validation dataset
class CouplingTest {

    static Coupling mini() throws IOException {
        return new Coupling(Facts.load(Path.of("src", "test", "resources"), "facts-mini"));
    }

    static Coupling validation() throws IOException {
        return new Coupling(Facts.load(Path.of("..", "resources")));
    }

    @Test
    void miniValues() throws IOException {
        Coupling c = mini();
        assertEquals(1, c.internalArcs());
        assertEquals(3, c.edgesInter());
        assertEquals(2.0 / 3, c.get("demo.A", "demo.B"), 1e-9);
        assertEquals(1.0 / 3, c.get("demo.B", "demo.C"), 1e-9);
        assertEquals(0.0, c.get("demo.A", "demo.C"), 1e-9);
    }

    @Test
    void symmetricAndZeroDiagonal() throws IOException {
        Coupling c = mini();
        assertEquals(c.get("demo.A", "demo.B"), c.get("demo.B", "demo.A"));
        assertEquals(0.0, c.get("demo.A", "demo.A"));
        assertTrue(validation().isSymmetricWithZeroDiagonal());
    }

    @Test
    void sumIsOne() throws IOException {
        assertEquals(1.0, mini().sum(), 1e-9);
        assertEquals(1.0, validation().sum(), 1e-9);
    }

    @Test
    void valuesBetweenZeroAndOne() throws IOException {
        for (Coupling.Pair p : validation().nonZeroPairs()) {
            assertTrue(p.value() > 0 && p.value() <= 1);
        }
    }

    @Test
    void validationCheckpoint() throws IOException {
        Coupling c = validation();
        assertEquals(7, c.internalArcs());
        assertEquals(37, c.edgesInter());

        List<Coupling.Pair> pairs = c.nonZeroPairs();
        assertEquals(19, pairs.size());
        assertEquals("library.model.Item", pairs.get(0).classA());
        assertEquals("library.service.LoanService", pairs.get(0).classB());
        assertEquals(4, pairs.get(0).arcs());
        assertEquals(4.0 / 37, pairs.get(0).value(), 1e-9);

        // Dvd and LoanException are in no pair
        for (Coupling.Pair p : pairs) {
            assertTrue(!p.classA().endsWith(".Dvd") && !p.classB().endsWith(".Dvd"));
            assertTrue(!p.classA().endsWith(".LoanException") && !p.classB().endsWith(".LoanException"));
        }
    }
}
