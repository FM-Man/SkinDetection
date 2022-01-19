import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;

public class Main {
    public static int[][][] colourArrayNonSkin = new int[256][256][256];
    public static int[][][] colourArraySkin = new int[256][256][256];
    public static double skin=0;
    public static double nonSkin=0;
    public static double[][][] probabilitySkin = new double[256][256][256];
    public static double[][][] probabilityNonSkin = new double[256][256][256];

    public static void readImage() throws IOException {

        File[] Maskfolder = new File("Mask").listFiles();
        File[] RealFolder = new File("Real").listFiles();

        for(int imageindex =0 ; imageindex < 500; imageindex++){
            BufferedImage imgMask = ImageIO.read(Maskfolder[imageindex]);
            BufferedImage img = ImageIO.read(RealFolder[imageindex]);

            for(int i=0; i< img.getHeight(); i++){
                for(int j=0; j< img.getWidth(); j++){
                    Color c = new Color(img.getRGB(j, i));
                    Color cM = new Color(imgMask.getRGB(j, i));
                    if(cM.getRed()<200 && cM.getBlue()<200 && cM.getGreen()<200){
                        colourArraySkin[c.getRed()][c.getGreen()][c.getBlue()]++;
                        skin++;
                        //probabilitySkin[c.getRed()][c.getGreen()][c.getBlue()] =(double) (colourArraySkin[c.getRed()][c.getGreen()][c.getBlue()]/skin);
                    }
                    else {
                        colourArrayNonSkin[c.getRed()][c.getGreen()][c.getBlue()]++;
                        nonSkin++;
                        //probabilityNonSkin[c.getRed()][c.getGreen()][c.getBlue()] =(double) (colourArrayNonSkin[c.getRed()][c.getGreen()][c.getBlue()]/nonSkin);
                    }
                }
            }
            System.out.println(imageindex + "reading done");
        }

        System.out.println("reading done");

        FileWriter skinProbFile = new FileWriter("skin.txt",true);
        FileWriter nonSkinProbFile = new FileWriter("nonskin.txt");
        for(int i=0; i<256; i++){
            for(int j=0; j<256; j++){
                for(int k=0; k<256; k++){
                    probabilitySkin[i][j][k] = colourArraySkin[i][j][k]/skin;
                    probabilityNonSkin[i][j][k] = colourArrayNonSkin[i][j][k]/nonSkin;
                    skinProbFile.write(probabilitySkin[i][j][k]+"\n");
                    nonSkinProbFile.write(probabilityNonSkin[i][j][k]+"\n");
                    System.out.println(i + " " + j + " " +k);
                }
            }
        }
        skinProbFile.close();
        nonSkinProbFile.close();
    }

    public static void main(String[] args) throws IOException {
        //readImage();
        //already trained with 500 images
        
        Scanner scS = new Scanner(new File("skin.txt"));
        Scanner scNS = new Scanner(new File("nonskin.txt"));

        for(int i=0; i<16777216; i++){
            int x = i;
            int c = x%256;
            x/=256;
            int b = x%256;
            x/=256;
            int a = x;

            double ss = scS.nextDouble();
            double sns = scNS.nextDouble();
            probabilityNonSkin[a][b][c] = sns;
            probabilitySkin[a][b][c] = ss;

            //System.out.println(a+" "+b+" "+c);
        }

        BufferedImage SampleImage = ImageIO.read(new File("in4.jpg"));
        BufferedImage outputImage = new BufferedImage(SampleImage.getWidth(), SampleImage.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);

        int width = SampleImage.getWidth();
        int height = SampleImage.getHeight();
        int[] imageInPixels = SampleImage.getRGB(0, 0, width, height, null, 0, width);
        int[] imageOutPixels = new int[imageInPixels.length];

        for (int i = 0; i < imageInPixels.length; i++) {
            int alpha = (imageInPixels[i] & 0xFF000000) >> 24;
            int red = (imageInPixels[i] & 0x00FF0000) >> 16;
            int green = (imageInPixels[i] & 0x0000FF00) >> 8;
            int blue = (imageInPixels[i] & 0x000000FF);

            // not face=black
            if ( probabilitySkin[red][green][blue] / probabilityNonSkin[red][green][blue] > 0.35  ){
                red = 255;
                green = 250;
                blue = 250;

            } else {
                red =10 ;
                green = 10;
                blue =10;
            }

            // At last, store in output array:
            imageOutPixels[i] = (alpha & 0xFF) << 24 | (red & 0xFF) << 16 | (green & 0xFF) << 8 | (blue & 0xFF);

        }
        outputImage.setRGB(0, 0, width, height, imageOutPixels, 0, width);

        File outputFile = new File("outputFile.png");
        ImageIO.write(outputImage, "png", outputFile);
    }
}
