package tesseractocr;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.log.Log;
import com.mysql.jdbc.Connection;
import java.sql.DriverManager;
import java.awt.Image;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import static javax.swing.text.StyleConstants.Size;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.TesseractException;
import net.sourceforge.tess4j.util.LoadLibs;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import static org.opencv.imgcodecs.Imgcodecs.IMREAD_GRAYSCALE;
import org.opencv.imgproc.Imgproc;
import static org.opencv.imgproc.Imgproc.ADAPTIVE_THRESH_MEAN_C;
import static org.opencv.imgproc.Imgproc.THRESH_BINARY;

public class Main extends JFrame{    
    DefaultTableModel dtm=new DefaultTableModel();
    Object[] kolonlar={"Id","İşletme Adı","Tarih","Fiş No","Ürünler"};
    Object[] satirlar={"1","2","3","4","5"};
    
    JButton button;
    JLabel label;
    TextArea textArea;
    JButton button1;
    JLabel label1;
    JTextField textField;
    JLabel label2;
    JTextField textField1;
    JButton button2;
    JLabel label3;
    JTable table;
    
    String path;
    JFileChooser fileChooser=new JFileChooser();
    
    public Main(){
        button=new JButton("Seç");
        button.setBounds(20, 180, 80, 30);
        label=new JLabel();
        label.setBounds(10, 10, 135, 150);
        button1=new JButton("Parse");
        button1.setBounds(240, 180, 80, 30);
        textArea=new TextArea();
        textArea.setBounds(190, 10, 200, 150);
        label1=new JLabel("İşletme Adı:");
        label1.setBounds(400, 7, 135, 50);
        textField=new JTextField();
        textField.setBounds(480, 20, 135, 25);
        label2=new JLabel("Tarih:");
        label2.setBounds(400, 45, 135, 50);
        textField1=new JTextField();
        textField1.setBounds(480, 58, 135, 25);
        button2=new JButton("Ara");
        button2.setBounds(480, 100, 80, 30);
        label3=new JLabel("Tüm Bilgiler");
        label3.setBounds(10, 205, 135, 50);
        table=new JTable();
        table.setBounds(10, 250, 608, 100);
        add(button);
        add(label);
        add(button1);
        add(textArea);
        add(label1);
        add(textField);
        add(label2);
        add(textField1);
        add(button2);
        add(label3);
        add(table);
        
        button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                
                fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
                FileNameExtensionFilter extensionFilter=new FileNameExtensionFilter("*.Images", "jpg","gif","png");
                fileChooser.addChoosableFileFilter(extensionFilter);
                fileChooser.setAcceptAllFileFilterUsed(false);
                int result=fileChooser.showSaveDialog(null);
                if(result==JFileChooser.APPROVE_OPTION){
                    File selectedFile=fileChooser.getSelectedFile();
                    path=selectedFile.getAbsolutePath();
                    System.out.println(path);
                    label.setIcon(ResizeImage(path));
                    
                }
                else if(result==JFileChooser.CANCEL_OPTION){
                    System.out.println("Dosya seçilemedi!");
                }
                
                
            }
        });
        
        button1.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                
                System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
                File f=fileChooser.getSelectedFile();
                String file=f.getAbsolutePath();
                Mat src=Imgcodecs.imread(file, 0);
                Mat dst=new Mat();
                Size size = new Size(src.rows()*2, src.rows()*2);
                Imgproc.resize(src, dst, size, 0, 0, Imgproc.INTER_AREA);
                Imgproc.adaptiveThreshold(src, dst, 125, Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY , 11, 12);
                Imgcodecs.imwrite("yeni.png", dst);
                File imageFile = new File(path);
                ITesseract instance = new net.sourceforge.tess4j.Tesseract();
                //instance.setLanguage("tur");
                File tessDataFolder = LoadLibs.extractTessResources("tessdata\\tur.traineddata"); // Maven build bundles English data

                try {
                    String result2;
                    result2 = instance.doOCR(imageFile);
                    System.out.println(result2);
                    textArea.setText(result2);
                } catch (TesseractException ex) {
                    System.err.println(ex.getMessage());
                }
            }
        });
        
        button2.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Connection con=(Connection) DriverManager.getConnection("jdbc:mysql:localhost:3306/yazlabdb", "root", "170520");
                    Statement myst=(Statement)con.createStatement();
                    ResultSet rSet=myst.executeQuery("select * from tblbilgiler");
                    while(rSet.next()){
                        System.out.println(rSet.getString("id")+"-"+rSet.getString("isletmeadi")+"-"+rSet.getString("tarih")+"-"+rSet.getString("fisno")+"-"+rSet.getString("urunler"));
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                dtm.setColumnIdentifiers(satirlar);
                dtm.addRow(kolonlar);
                table.setModel(dtm);
            }
        });
        
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(700, 400);
        setVisible(true);
        
    }
    public ImageIcon ResizeImage(String ImagePath){
        ImageIcon icon=new ImageIcon(ImagePath);
        Image img=icon.getImage();
        Image newImg=img.getScaledInstance(label.getWidth(), label.getHeight(), Image.SCALE_SMOOTH);
        ImageIcon image=new ImageIcon(newImg);
        return image;
        
    }
    
    public static void main(String[] args) {
        new Main();

        
        /*Tesseract tesseract = new Tesseract();
        tesseract.getClass();*/
    }
   
}
