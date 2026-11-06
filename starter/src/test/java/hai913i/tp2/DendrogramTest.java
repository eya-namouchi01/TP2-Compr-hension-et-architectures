package hai913i.tp2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.junit.jupiter.api.Test;

// Step B2
class DendrogramTest {

    static Dendrogram validation() throws IOException {
        Clustering cl = ClusteringTest.run(CouplingTest.validation());
        return new Dendrogram(cl.root(), cl.merges());
    }

    @Test
    void fifteenLeavesAndFourteenInternalNodes() throws IOException {
        Dendrogram d = validation();
        assertEquals(15, d.leafCount());
        assertEquals(14, d.internalCount());
    }

    @Test
    void membersAreDisjointUnionOfChildren() throws IOException {
        assertTrue(validation().isValid());
    }

    @Test
    void rootHasAllClassesOnce() throws IOException {
        Dendrogram d = validation();
        assertEquals(CouplingTest.validation().classes(), d.root().members());
    }

    @Test
    void mergeListHasOneLinePerMerge() throws IOException {
        String[] lines = validation().listMerge().split("\n");
        assertEquals(1 + 14, lines.length);
        assertTrue(lines[1].startsWith("1;0.1081;"));
    }
}
