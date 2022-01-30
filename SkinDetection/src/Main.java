import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * The Probability file is bigger than 100 MB.
 * So I can not upload it. but after once you don't need 69'th line anymore
 * */

public class Main {
    public static int[][][] colourArrayNonSkin = new int[256][256][256];
    public static int[][][] colourArraySkin = new int[256][256][256];
//    public static double skin=0;
//    public static double nonSkin=0;
//    public static double[][][] probabilitySkin = new double[256][256][256];
//    public static double[][][] probabilityNonSkin = new double[256][256][256];

    public static void readImage() throws IOException {

        File[] MaskFolder = new File("Mask").listFiles();
        File[] RealFolder = new File("Real").listFiles();

        for(int imageIndex =0 ; imageIndex < 500; imageIndex++){
            BufferedImage imgMask = ImageIO.read(MaskFolder[imageIndex]);
            BufferedImage img = ImageIO.read(RealFolder[imageIndex]);

            for(int i=0; i< img.getHeight(); i++){
                for(int j=0; j< img.getWidth(); j++){
                    Color c = new Color(img.getRGB(j, i));
                    Color cM = new Color(imgMask.getRGB(j, i));
                    if(cM.getRed()<220 && cM.getBlue()<220 && cM.getGreen()<220){
                        colourArraySkin[c.getRed()][c.getGreen()][c.getBlue()]++;
                        //skin++;
                    }
                    else {
                        colourArrayNonSkin[c.getRed()][c.getGreen()][c.getBlue()]++;
                        //nonSkin++;
                    }
                }
            }
            System.out.println(imageIndex + "reading done");
        }

        System.out.println("reading done");

//        for(int i=0; i<256; i++){
//            for(int j=0; j<256; j++){
//                for(int k=0; k<256; k++){
//                    probabilitySkin[i][j][k] = colourArraySkin[i][j][k]/skin;
//                    probabilityNonSkin[i][j][k] = colourArrayNonSkin[i][j][k]/nonSkin;
//
//                    System.out.println(i + " " + j + " " +k);
//                }
//            }
//        }

        Probability prob = new Probability(colourArraySkin,colourArrayNonSkin);
        System.out.println("obj done");
        FileOutputStream probability = new FileOutputStream("probability.txt");
        ObjectOutputStream obj = new ObjectOutputStream(probability);
        obj.writeObject(prob);
        System.out.println("writing done");
        probability.close();
        obj.close();
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        //readImage();
        //comment this line after once

        System.out.println("reading started");
        ObjectInputStream input = new ObjectInputStream(new FileInputStream("probability.txt"));
        Probability inProb = (Probability) input.readObject();
        System.out.println("reading done");
        colourArraySkin = inProb.probSkin;
        colourArrayNonSkin = inProb.probNonSkin;
        System.out.println("assigning done");

        BufferedImage SampleImage = ImageIO.read(new File("in5.jpg"));
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

            // face = white
            if (!(colourArraySkin[red][green][blue] > .15 * colourArrayNonSkin[red][green][blue])) {
                red =10 ;
                green = 10;
                blue =10;
            }

            imageOutPixels[i] = (alpha & 0xFF) << 24 | (red & 0xFF) << 16 | (green & 0xFF) << 8 | (blue & 0xFF);

        }
        outputImage.setRGB(0, 0, width, height, imageOutPixels, 0, width);

        File outputFile = new File("outputFile.png");
        ImageIO.write(outputImage, "png", outputFile);
    }
}
