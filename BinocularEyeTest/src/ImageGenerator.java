
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.awt.Color;
import java.awt.Image;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;

import java.io.File;
import java.io.IOException;

public class ImageGenerator {
    
    /**
     * @param args
     */
    
   
   public static BufferedImage generateImage(int width, int height) {
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (Math.random() * 2 > 1) {
                    img.setRGB(x, y, Color.black.getRGB());
                } else {
                    img.setRGB(x, y, Color.white.getRGB());
                }
            }
        }
        
        return img;
        
    }
    
    public static void saveImage(String fname, Image img, String format) {
        File f;
        if (fname == "") {
            f = null;
            JFileChooser chooser = new JFileChooser();
            int returnVal = chooser.showSaveDialog(null);
            if (returnVal == chooser.APPROVE_OPTION) {
                f = chooser.getSelectedFile();
            }
        } else {
            f = new File(fname);
        }
        try {
            f.createNewFile();
            System.out.println("starting writing");
            boolean success = ImageIO.write((RenderedImage) img, format, f);
            if (success) {
                System.out.println("Successful");
            } else {
                System.out.println("Failed");
            }
            System.out.println("finished writing");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            System.out.println("problem");
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        int width = 1280;
        int height = 800;
        if (args.length == 2) {
            width = Integer.parseInt(args[0]);
            height = Integer.parseInt(args[1]);
        }
        System.out.println("Starting Image Generator");
        ImageGenerator.saveImage("./fsbg.png", ImageGenerator.generateImage(width, height), "png");
        System.out.println("Image Generator Finished");
    }

}
