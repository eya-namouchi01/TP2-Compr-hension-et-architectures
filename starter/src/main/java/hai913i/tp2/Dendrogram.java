package hai913i.tp2;

import java.util.ArrayList;
import java.util.List;

public class Dendrogram {

    private final Cluster root ;
    private final List<Cluster> merges;

    public Dendrogram(Cluster root, List<Cluster> merges){
        this.root= root;
        this.merges= merges;
    }

  // Format 1: indented tree, one line per node
    public String indentedTree() {
        StringBuilder sb = new StringBuilder();
        writeTree(root, "", sb);
        return sb.toString();
    }

    private void writeTree(Cluster node, String indent, StringBuilder sb) {
        if (node.isLeaf()) {
            sb.append(indent + "- " + Report.shortName(node.members().get(0)) + "\n");
            return;
        }
        sb.append(indent + "+ étape " + (merges.indexOf(node)+1) + " (similarité " + Report.num(node.similarity()) + ") " + node + "\n");
        writeTree(node.left(), indent + "    ", sb);
        writeTree(node.right(), indent + "    ", sb);
    }

    // Format 2: one line per merge
    public String listMerge(){
        StringBuilder sb = new StringBuilder();
        sb.append("etape;similarite;groupeA;groupeB\n");
        for( Cluster c : merges) sb.append((merges.indexOf(c) + 1) + ";" + Report.num(c.similarity()) + ";" + c.left() + ";" + c.right() + "\n");
        return sb.toString();
    }

    // Format 3: DOT. Leaves at the bottom, each merge shows its step and its similarity
    public String toDot() {
        StringBuilder sb = new StringBuilder();
        sb.append("digraph dendrogramme {\n");
        sb.append("  rankdir=BT;\n");
        sb.append("  node [fontname=\"Helvetica\", fontsize=10];\n");
        writeDot(root, sb);
        sb.append("}\n");
        return sb.toString();
    }

    private void writeDot(Cluster node, StringBuilder sb) {
        String name = "n" + node.id();
        if (node.isLeaf()) {
            sb.append("  " + name + " [shape=box, label=\""
                    + Report.shortName(node.members().get(0)) + "\"];\n");
            return;
        }
        sb.append("  " + name + " [shape=ellipse, label=\"étape " + (merges.indexOf(node)+1) + "\\n" + Report.num(node.similarity()) + "\"];\n");
        writeDot(node.left(), sb);
        writeDot(node.right(), sb);
        sb.append("  n" + node.left().id() + " -> " + name + " [dir=none];\n");
        sb.append("  n" + node.right().id() + " -> " + name + " [dir=none];\n");
    }

    public int leafCount() {
        return countLeaves(root);
    }

    private int countLeaves(Cluster node) {
        if (node.isLeaf()) return 1;
        return countLeaves(node.left()) + countLeaves(node.right());
    }

    public int internalCount() {
        return countInternal(root);
    }

    private int countInternal(Cluster node) {
        if (node.isLeaf()) {
            return 0;
        }
        return 1 + countInternal(node.left()) + countInternal(node.right());
    }

    // Checks that the members of every internal node are exactly the members of its two children ( with no class in both )
    public boolean isValid() {
        return checkNode(root);
    }

    private boolean checkNode(Cluster node) {
        if (node.isLeaf()) return node.members().size() == 1;
        List<String> union = new ArrayList<>(node.left().members());
        for (String c : node.right().members()) {
            if (union.contains(c)) return false;
            union.add(c);
        }
        union.sort(null);
        if (!union.equals(node.members())) return false;
        return checkNode(node.left()) && checkNode(node.right());
    }

    public Cluster root() {
        return root;
    }

}
