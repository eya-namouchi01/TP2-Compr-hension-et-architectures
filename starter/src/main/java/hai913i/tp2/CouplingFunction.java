package hai913i.tp2;

// A function that gives a coupling number for two classes
// The clustering and the cut only use this, they never see the call arcs.
public interface CouplingFunction {

    double get(String classA, String classB);
}
