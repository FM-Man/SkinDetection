import java.io.Serializable;

public class Probability implements Serializable {
    public int[][][] probSkin;
    public int[][][] probNonSkin;
    public Probability(int[][][] skin, int[][][] nonSkin){
        probSkin = skin;
        probNonSkin = nonSkin;
    }
}
