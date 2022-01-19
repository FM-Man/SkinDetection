import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

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

        for(int imageindex =0 ; imageindex < 200; imageindex++){
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

            for(int i=0; i<256; i++){
                for(int j=0; j<256; j++){
                    for(int k=0; k<256; k++){
                        probabilitySkin[i][j][k] = colourArraySkin[i][j][k]/skin;
                        probabilityNonSkin[i][j][k] = colourArrayNonSkin[i][j][k]/nonSkin;
                    }
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        readImage();
        System.out.println("reading done");

        BufferedImage SampleImage = ImageIO.read(new File("rb.jpg"));
        BufferedImage outputImage = new BufferedImage(SampleImage.getWidth(), SampleImage.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);

        int width = SampleImage.getWidth();
        int height = SampleImage.getHeight();
        int[] imageInPixels = SampleImage.getRGB(0, 0, width, height, null, 0, width);
        int[] imageOutPixels = new int[imageInPixels.length];

        for (int i = 0; i < imageInPixels.length; i++) {
            int alpha = (imageInPixels[i] & 0xFF000000) >> 24;
            int red = (imageInPixels[i] & 0x00FF0000) >> 16;
            int green = (imageInPixels[i] & 0x0000FF00) >> 8;
            int blue = (imageInPixels[i] & 0x000000FF) >> 0;

            // Make any change to the colors.
            if (  probabilityNonSkin[red][green][blue] > probabilitySkin[red][green][blue] ){
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
