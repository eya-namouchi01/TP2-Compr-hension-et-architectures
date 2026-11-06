package hai913i.tp2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Cluster {
    private final int id;
    public List<String> members() {
        return members;
    }
    private final List<String> members;
    private final Cluster left;
    private final Cluster right;
    private final double similarity;

    // One class
    public Cluster(int id, String className) {
        this.id = id;
        this.members = List.of(className);
        this.left = null;
        this.right = null;
        this.similarity = 0.0;
    }
    //merge of two groups
    public Cluster(int id, Cluster left, Cluster right, double similarity) {
        this.id = id;
        List<String> allMembers = new ArrayList<>(left.members);
        allMembers.addAll(right.members);
        Collections.sort(allMembers);
        this.members = Collections.unmodifiableList(allMembers);
        this.left = left;
        this.right = right;
        this.similarity =similarity;
    }

    public boolean isLeaf() {
        return left == null;
    }

    public int id() {
        return id;
    }

    public double similarity() {
        return similarity;
    }

    public Cluster right() {
        return right;
    }

    public Cluster left() {
        return left;
    }

    // Display: short names of the members
    @Override
    public String toString() {
        List<String> names = new ArrayList<>();
        for (String m : members) {
            names.add(Report.shortName(m));
        }
        return "{" + String.join(", ", names) + "}";
    }
}
