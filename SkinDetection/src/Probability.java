import java.io.Serializable;

public class Probability implements Serializable {
    public double[][][] probSkin;
    public double[][][] probNonSkin;
    public Probability(double[][][] skin, double[][][] nonSkin){
        probSkin = skin;
        probNonSkin = nonSkin;
    }
}
