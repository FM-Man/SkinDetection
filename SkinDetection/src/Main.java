import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {
    public static int[][][] colourArrayNonSkin = new int[256][256][256];
    public static int[][][] colourArraySkin = new int[256][256][256];

    public static void readImage() throws IOException {

        File[] Maskfolder = new File("Mask").listFiles();
        File[] RealFolder = new File("Real").listFiles();

        for(int imageindex =0 ; imageindex < 554/*Maskfolder.length*/; imageindex++){
            BufferedImage imgMask = ImageIO.read(Maskfolder[imageindex]);
            BufferedImage img = ImageIO.read(RealFolder[imageindex]);

            for(int i=0; i< 50 /*img.getHeight()*/; i++){
                for(int j=0; j< 50/*img.getWidth()*/; j++){
                    Color c = new Color(img.getRGB(i,j));
                    Color cM = new Color(imgMask.getRGB(i,j));
                    if(cM.getRed()<200 && cM.getBlue()<200 && cM.getGreen()<200)       //if white
                        colourArraySkin[cM.getRed()][cM.getGreen()][cM.getBlue()]++;
                    else colourArrayNonSkin[c.getRed()][c.getGreen()][c.getBlue()]++;
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        readImage();

        int sumR=0;
        int sumG=0;
        int sumB=0;
        for(int i =0 ; i<256;i++){
            sumR+=colourArraySkin[i][0][0];
            sumG+=colourArraySkin[0][i][0];
            sumB+=colourArraySkin[0][0][i];
        }

        System.out.println(sumR + " " + sumG + " "+ sumB);


    }
}
