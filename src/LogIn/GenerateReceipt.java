package LogIn;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;

import javax.swing.*;
import java.io.FileOutputStream;
import java.util.Date;

/**
 *
 *
 * Created by Malone-Ibra on 7/22/2018.
 */
public class GenerateReceipt {
    public GenerateReceipt(Users users, long receiptNo, Paragraph []paragraphs){
        Document document = new Document();
        Rectangle rect = new Rectangle(350,700);
        document.setPageSize(rect);
        try{
            //System.out.println("_______access fxn1");
            PdfWriter.getInstance(document, new FileOutputStream("RECEIPT.pdf"));
            document.open();
            document.add(new Paragraph("Vintage Lux Hotel", FontFactory.getFont(FontFactory.TIMES_BOLD,15,Font.BOLD)));
            document.add(new Paragraph("Customer Index: "+receiptNo, FontFactory.getFont(FontFactory.TIMES_BOLD,15, Font.BOLD)));
            document.add(new Paragraph(new Date().toString()));
            document.add(new Paragraph("_________________________________________"));
            document.add(new Paragraph(" "));
            for (int k=0;k<paragraphs.length;k++) {
                if (paragraphs[k]==null){
                    break;
                }
                document.add(paragraphs[k]);

            }
            document.add(new Paragraph(" "));
            document.add(new Paragraph("______________________________________"));
            document.add(new Paragraph("Served By - "+users.userName,FontFactory.getFont(FontFactory.TIMES_ROMAN,12,Font.NORMAL)));
            document.add(new Paragraph("Branch: "+"Nairobi, Kenya",FontFactory.getFont(FontFactory.TIMES_ROMAN,12,Font.NORMAL)));
            document.close();
            JOptionPane.showMessageDialog(null, "Successfully Generated");
        }catch(Exception e ){
            e.printStackTrace();
        }
    }
}
