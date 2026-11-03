package hai913i.tp2;

import java.util.List;
import java.util.Locale;

// Text shown to the user for each step. Nothing is computed here.
public class Report {

    // library.model.Item -> Item
    public static String shortName(String qualifiedName) {
        int dot = qualifiedName.lastIndexOf('.');
        return qualifiedName.substring(dot + 1);
    }

    // 4 decimals, always with a dot
    public static String num(double value) {
        return String.format(Locale.ROOT, "%.4f", value);
    }

    // Step A1
    public static String coupling(Coupling c) {
        StringBuilder sb = new StringBuilder();
        List<Coupling.Pair> pairs = c.nonZeroPairs();
        int m = c.classes().size();

        sb.append("Arcs internes écartés : " + c.internalArcs() + "\n");
        sb.append("|E_inter| : " + c.edgesInter() + "\n");
        sb.append("Couples non nuls : " + pairs.size() + " sur " + (m * (m - 1) / 2) + "\n");
        sb.append("Somme des couplages : " + num(c.sum()) + "\n");
        sb.append("Symétrie et diagonale nulle : " + (c.isSymmetricWithZeroDiagonal() ? "oui" : "NON") + "\n");
        sb.append("Couples par couplage décroissant :\n");
        for (Coupling.Pair p : pairs) {
            sb.append("  " + shortName(p.classA()) + " - " + shortName(p.classB())
                    + " : r = " + p.arcs() + ", C = " + p.arcs() + "/" + c.edgesInter()
                    + " = " + num(p.value()) + "\n");
        }
        return sb.toString();
    }

    // Step A2
    public static String graph(CouplingGraph g) {
        StringBuilder sb = new StringBuilder();
        sb.append("Noeuds : " + g.nodes().size() + "\n");
        sb.append("Arêtes : " + g.edges().size() + "\n");
        sb.append("Les cinq couples les plus couplés :\n");
        for (CouplingGraph.Edge e : g.top(5)) {
            sb.append("  " + shortName(e.classA()) + " - " + shortName(e.classB()) + " : " + num(e.weight()) + "\n");
        }
        sb.append("Classes isolées :");
        for (String c : g.isolatedClasses()) {
            sb.append(" " + shortName(c));
        }
        sb.append("\n");
        return sb.toString();
    }

    // Step B1
    public static String merges(List<Cluster> merges) {
        StringBuilder sb = new StringBuilder();
        sb.append("Nombre de fusions : " + merges.size() + "\n");
        for (int i = 0; i < merges.size(); i++) {
            Cluster m = merges.get(i);
            sb.append("  Étape " + (i + 1) + " : " + m.left() + " + " + m.right() + " -> " + num(m.similarity()) + "\n");
        }
        return sb.toString();
    }

}