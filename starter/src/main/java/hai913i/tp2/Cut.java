package hai913i.tp2;

import java.util.ArrayList;
import java.util.List;

public class Cut {

    private Coupling coupling;
    private double cp;
    private List<Cluster> modules = new ArrayList<>();
    private final int classCount;

    public Cut( Cluster root, Coupling coupling, int classCount, double cp){
        this.coupling=coupling;
        this.classCount= classCount;
        this.cp= cp;
        doCut(root);
    }

    public void doCut(Cluster cluster){
        if (cluster.isLeaf()) modules.add(cluster);
        else if (internalCoupling(cluster) > cp) modules.add(cluster);
        else{
            doCut(cluster.left());
            doCut(cluster.right());
            }
        }

    public double internalCoupling(Cluster cluster){
        if ( cluster.isLeaf() )return 0.0;
        List<String> members= cluster.members();
        double sum = 0;
        int pairs= 0;
        for( int i=0; i< members.size(); i++ ){
            for( int j= i+1; j< members.size(); j++){
                sum += coupling.get(members.get(i), members.get(j));
                pairs++;
            }
        }
        return sum/pairs;
    }

    public List<Cluster> modules(){
        return modules;
    }
    public double cp(){return cp;}
    public int classCount(){return classCount;}


    public boolean respectsLimit(){
        return modules.size() <= classCount/2;
    }




}
