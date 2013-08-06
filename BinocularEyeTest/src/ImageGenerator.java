
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
    
    
    public static BufferedImage generateImage(int width, int height, int patternType, int coverage) {
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        if (patternType == 0) {
        	System.out.println("Random");
        	for (int x = 0; x < width; x++) {
        		for (int y = 0; y < height; y++) {
        			if (Math.random() * 100 <= coverage) {
        				img.setRGB(x, y, Color.black.getRGB());
        			} else {
        				img.setRGB(x, y, Color.white.getRGB());
        			}
        		}
        	}
        }else if (patternType == 1) {
        	System.out.println("Horizontal");
        	for (int x = 0; x < width; x++) {
        		for (int y = 0; y < height; y++) {
        			if (y % 3 == 2) {
        				img.setRGB(x, y, Color.black.getRGB());
        			} else {
        				img.setRGB(x, y, Color.white.getRGB());
        			}
        		}
        	}
        } else if (patternType == 2) {
        	System.out.println("Vertical");
        	for (int x = 0; x < width; x++) {
        		for (int y = 0; y < height; y++) {
        			if (x % 3 == 1) {
        				img.setRGB(x, y, Color.black.getRGB());
        			} else {
        				img.setRGB(x, y, Color.white.getRGB());
        			}
        		}
        	}
        }
        return img;
        
    }
    
    public static BufferedImage generateImage(int width, int height) {
    	return generateImage(width, height, 0);
    }
    
    public static BufferedImage generateImage(int width, int height, int patternType) {
    	return generateImage(width, height, patternType, 50);
    }
    
    public static void saveImage(String fname, Image img, String format) {
        File f;
        if (fname.equals("")) {
            f = null;
            JFileChooser chooser = new JFileChooser();
            int returnVal = chooser.showSaveDialog(null);
            if (returnVal == chooser.APPROVE_OPTION) {
                f = chooser.getSelectedFile();
            }
        } else {
            f = new File("./" + fname + "." + format);
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
        int pattern = 0;
        int coverage = 50;
        String fname = "fsbg";
        String format = "png";
        if (args.length >= 2) {
            width = Integer.parseInt(args[0]);
            height = Integer.parseInt(args[1]);
        }
        System.out.println(args.length);
        if (args.length >= 3) {
        	System.out.println("\"" + args[2].toUpperCase() + "\"");
        	System.out.println(args[2].equals("HORIZONTAL"));
        	if (args[2].equals("RANDOM")) {
        		System.out.println("Pattern = 0");
        		pattern = 0;
        	} else if (args[2].equals("HORIZONTAL")) {
        		System.out.println("Pattern = 1");
        		pattern = 1;
        	} else if (args[2].equals("VERTICAL")) {
        		System.out.println("Pattern = 2");
        		pattern = 2;
        	}
        }
        if (args.length >= 4) {
        	coverage = Integer.parseInt(args[3]);
        }
        if (args.length >= 5) {
        	fname = args[4];
        }
        System.out.println("Starting Image Generator");
        ImageGenerator.saveImage(fname, ImageGenerator.generateImage(width, height, pattern, coverage), "png");
        System.out.println("Image Generator Finished");
    }

}
