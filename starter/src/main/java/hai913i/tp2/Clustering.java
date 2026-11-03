package hai913i.tp2;

import java.util.ArrayList;
import java.util.List;

public class Clustering {

    public static final double EPS = 1e-9;

    private CouplingFunction coupling;
    private final List<Cluster> merges = new ArrayList<>();
    private Cluster root;


    public Clustering(List<String> classNames, CouplingFunction coupling) {
        this.coupling = coupling;
        doClustering(classNames);
    }


    private void doClustering(List<String> classNames) {
        // Leaves: ids 0 to M-1, in alphabetical order of qualified name
        List<String> sorted = new ArrayList<>(classNames);
        sorted.sort(null);
        List<Cluster> active = new ArrayList<>();
        int nextId = 0;
        for (String c : sorted) {
            active.add(new Cluster(nextId, c));
            nextId++;
        }
        while (active.size() > 1) {
            Cluster bestA = null;
            Cluster bestB = null;
            double best = -1.0;
            for (int i = 0; i < active.size(); i++) {
                for (int j = i + 1; j < active.size(); j++) {
                    double s = similarity(active.get(i), active.get(j));
                    if (s > best + EPS) {
                        best = s;
                        bestA = active.get(i);
                        bestB = active.get(j);
                    }
                }
            }
            Cluster merged = new Cluster(nextId, bestA, bestB, best);
            nextId++;
            active.remove(bestA);
            active.remove(bestB);
            active.add(merged);
            merges.add(merged);
        }
        if (active.size() == 1) {
            root = active.get(0);
        }
    }

    public double similarity(Cluster cluster1, Cluster cluster2){
        double sum =0;
        for (String member1: cluster1.members()){
            for(String member2: cluster2.members()){
                sum+= coupling.get(member1,member2);
            }
        }
        return sum / (cluster1.members().size() *cluster2.members().size());
    }

    public Cluster root() {
        return root;
    }

    public List<Cluster> merges() {
        return merges;
    }


}