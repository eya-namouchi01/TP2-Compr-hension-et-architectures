package hai913i.tp2;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CouplingGraphTest {

    @Test
    void validationCheckpoint() throws IOException {
        CouplingGraph g = CouplingGraph.fromCoupling(CouplingTest.validation());
        assertEquals(15, g.nodes().size());
        assertEquals(19, g.edges().size());
        assertEquals(List.of("library.model.Dvd", "library.service.LoanException"),
                g.isolatedClasses());

        List<CouplingGraph.Edge> top = g.top(5);
        assertEquals(4.0 / 37, top.get(0).weight(), 1e-9);
        assertEquals(4.0 / 37, top.get(1).weight(), 1e-9);
        assertEquals(3.0 / 37, top.get(4).weight(), 1e-9);
    }

    @Test
    void isSameGraph(@TempDir Path dir) throws IOException {
        CouplingGraph gf = CouplingGraph.fromCoupling(CouplingTest.validation());
        Path file = dir.resolve("graphe.csv");
        gf.saveFile(file);

        CouplingGraph gfReloaded = CouplingGraph.load(file);
        assertEquals(gf.nodes(), gfReloaded.nodes());
        assertEquals(gf.edges(), gfReloaded.edges());
    }
}
