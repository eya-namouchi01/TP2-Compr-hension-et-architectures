package hai913i.tp2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Coupling between classes, C(A,B) = r(A,B) / |E_inter|
public class Coupling implements CouplingFunction {

    public record Pair(String classA, String classB, int arcs, double value) {
    }

    private final List<String> classes;
    private int internalArcs = 0;
    // |E_inter|
    private int edgesInter = 0;

    // key : "A,B" , value : r(A,B)
    private final Map<String, Integer> arcsByPair = new HashMap<>();

    public Coupling(FactsInterface facts) {
        classes = facts.classNames();

        for (Facts.CallArc arc : facts.calls()) {
            String a = arc.callerClass();
            String b = arc.calleeClass();
            if (a.equals(b)) {
                internalArcs++;
            } else {
                edgesInter++;
                String key = key(a, b);
                if (arcsByPair.containsKey(key)) {
                    // pair already seen
                    arcsByPair.put(key, arcsByPair.get(key) + 1);
                } else {
                    // first arc for this pair
                    arcsByPair.put(key, 1);
                }
            }
        }
    }

    // An arc A->B and an arc B->A are counted for the same pair
    private static String key(String a, String b) {
        if (a.compareTo(b) < 0) {
            return a + "," + b;
        }
        return b + "," + a;
    }

    public int arcs(String a, String b) {
        if (a.equals(b)) {
            return 0;
        }
        String key = key(a, b);
        if (arcsByPair.containsKey(key)) {
            return arcsByPair.get(key);
        }
        return 0;
    }
    @Override
    public double get(String a, String b) {
        if (edgesInter == 0) {
            return 0.0;
        }
        return (double) arcs(a, b) / edgesInter;
    }

    public List<String> classes() {
        return classes;
    }

    public int internalArcs() {
        return internalArcs;
    }

    public int edgesInter() {
        return edgesInter;
    }

    // All the pairs with a non-zero coupling by decreasing coupling
    public List<Pair> nonZeroPairs() {
        List<Pair> pairs = new ArrayList<>();
        int max = 0;
        for (int i = 0; i < classes.size(); i++) {
            for (int j = i + 1; j < classes.size(); j++) {
                String a = classes.get(i);
                String b = classes.get(j);
                int r = arcs(a, b);
                if (r > 0) {
                    pairs.add(new Pair(a, b, r, get(a, b)));
                    if (r > max) {
                        max = r;
                    }
                }
            }
        }
        //sort
        List<Pair> sorted = new ArrayList<>();
        for (int r = max; r >= 1; r--) {
            for (Pair p : pairs) {
                if (p.arcs() == r) {
                    sorted.add(p);
                }
            }
        }
        return sorted;
    }

    // Sum of all C(A,B)
    public double sum() {
        double total = 0.0;
        for (int i = 0; i < classes.size(); i++) {
            for (int j = i + 1; j < classes.size(); j++) {
                total += get(classes.get(i), classes.get(j));
            }
        }
        return total;
    }

    // C(A,B) = C(B,A) and C(A,A) = 0 for all classes
    public boolean isSymmetricWithZeroDiagonal() {
        for (String a : classes) {
            if (get(a, a) != 0.0) {
                return false;
            }
            for (String b : classes) {
                if (get(a, b) != get(b, a)) {
                    return false;
                }
            }
        }
        return true;
    }
}
