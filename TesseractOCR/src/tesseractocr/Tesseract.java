package tesseractocr;

import java.io.File;
import net.sourceforge.tess4j.*;
import net.sourceforge.tess4j.TesseractException;


class Tesseract {
    /*public static void main(String[] args) {
        File imageFile = new File("manifest.mf");
    ITesseract instance = (ITesseract) new Tesseract();  // JNA Interface Mapping
    // ITesseract instance = new Tesseract1(); // JNA Direct Mapping
    instance.setDatapath("C:\\Users\\Eylem\\Desktop\\Tess4J-3.4.8-src\\Tess4J\\tessdata"); // path to tessdata directory

    try {
        String result = instance.doOCR(imageFile);
        System.out.println(result);
    } catch (TesseractException e) {
        System.err.println(e.getMessage());
    }
    }*/
    
      public static void main(String[] args) {
        File imageFile = new File("C:\\Users\\Eylem\\Desktop\\test.png");
        ITesseract instance =  (ITesseract) new Tesseract();  // JNA Interface Mapping
        // ITesseract instance = new Tesseract1(); // JNA Direct Mapping
        instance.setDatapath("tessdata"); // path to tessdata directory

        try {
            String result = instance.doOCR(imageFile);
            System.out.println(result);
        } catch (TesseractException e) {
            System.err.println(e.getMessage());
        }
    }
}
