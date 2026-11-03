package hai913i.tp2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class ClusteringTest {

    static Clustering run(Coupling c) {
        return new Clustering(c.classes(), c);
    }

    @Test
    void miniMerges() throws IOException {
        List<Cluster> merges = run(CouplingTest.mini()).merges();
        assertEquals(2, merges.size());
        assertEquals(List.of("demo.A", "demo.B"), merges.get(0).members());
        assertEquals(2.0 / 3, merges.get(0).similarity(), 1e-9);
        assertEquals(1.0 / 6, merges.get(1).similarity(), 1e-9);
    }

    @Test
    void validationFirstThreeMerges() throws IOException {
        List<Cluster> merges = run(CouplingTest.validation()).merges();
        assertEquals(14, merges.size());
        assertEquals("{Item} + {LoanService}", merges.get(0).left() + " + " + merges.get(0).right());
        assertEquals(4.0 / 37, merges.get(0).similarity(), 1e-9);
        assertEquals("{LibraryApp} + {Catalog}", merges.get(1).left() + " + " + merges.get(1).right());
        assertEquals(3.0 / 37, merges.get(1).similarity(), 1e-9);
        assertEquals(9.0 / 148, merges.get(2).similarity(), 1e-9);
        assertEquals(0.0, merges.get(12).similarity(), 1e-9);
        assertEquals(0.0, merges.get(13).similarity(), 1e-9);
    }

    @Test
    void similaritiesNeverGoUp() throws IOException {
        List<Cluster> merges = run(CouplingTest.validation()).merges();
        for (int i = 1; i < merges.size(); i++) {
            assertTrue(merges.get(i).similarity() <= merges.get(i - 1).similarity() + Clustering.EPS);
        }
    }

    @Test
    void twoRunsGiveSameMerges() throws IOException {
        Coupling c = CouplingTest.validation();
        assertEquals(text(run(c).merges()), text(run(c).merges()));
    }

    private static List<String> text(List<Cluster> merges) {
        List<String> out = new ArrayList<>();
        for (Cluster m : merges) {
            out.add(m.left() + " + " + m.right() + " " + Report.num(m.similarity()));
        }
        return out;
    }

    @Test
    void tieBreakKeepsSmallestPair() {
        // Same matrix as TieBreakDemo
        // (0,2), (1,3) and (2,3) are at 0.25
        List<String> names = List.of("g0", "g1", "g2", "g3");
        double[][] sim = {
            {0.00, 0.10, 0.25, 0.05},
            {0.10, 0.00, 0.05, 0.25},
            {0.25, 0.05, 0.00, 0.25},
            {0.05, 0.25, 0.25, 0.00},
        };
        CouplingFunction f = (a, b) -> sim[names.indexOf(a)][names.indexOf(b)];

        Cluster first = new Clustering(names, f).merges().get(0);
        assertEquals(0, first.left().id());
        assertEquals(2, first.right().id());
    }
}