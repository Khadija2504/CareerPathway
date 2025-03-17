package com.CareerPathway.CareerPathway.util;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.element.Image;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

public class PdfCertificateGenerator {

    public static String generateCertificate(String employeeFirstName, String employeeLastName, String careerPathName, String outputPath) throws FileNotFoundException, MalformedURLException {
        File directory = new File(outputPath).getParentFile();
        if (!directory.exists()) {
            boolean created = directory.mkdirs();
            if (!created) {
                throw new RuntimeException("Failed to create directory: " + directory.getAbsolutePath());
            }
        }

        PdfWriter writer = new PdfWriter(outputPath);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc, PageSize.A4.rotate());

        document.setMargins(50, 50, 50, 50);

        Text title = new Text("Certificate of Completion")
                .setFontSize(36)
                .setBold()
                .setFontColor(ColorConstants.DARK_GRAY);
        Paragraph titleParagraph = new Paragraph(title)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(30);
        document.add(titleParagraph);

        Table line = new Table(1).setWidth(400).setHorizontalAlignment(HorizontalAlignment.CENTER);
        line.addCell(new Cell().setHeight(2).setBackgroundColor(ColorConstants.DARK_GRAY).setBorder(Border.NO_BORDER));
        document.add(line);

        Text nameText = new Text(employeeLastName + " " + employeeFirstName)
                .setFontSize(28)
                .setBold()
                .setFontColor(ColorConstants.BLUE);
        Paragraph nameParagraph = new Paragraph(nameText)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginTop(20)
                .setMarginBottom(10);
        document.add(nameParagraph);

        Text careerText = new Text("has successfully completed the " + careerPathName + " career path.")
                .setFontSize(18)
                .setFontColor(ColorConstants.BLACK);
        Paragraph careerParagraph = new Paragraph(careerText)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(30);
        document.add(careerParagraph);

        Image logo = new Image(ImageDataFactory.create("src/main/resources/static/logo.png"));
        logo.setWidth(100);
        logo.setHorizontalAlignment(HorizontalAlignment.CENTER);
        document.add(logo);

        Text signatureText = new Text("Signature: ________________________")
                .setFontSize(14)
                .setFontColor(ColorConstants.DARK_GRAY);
        Paragraph signatureParagraph = new Paragraph(signatureText)
                .setTextAlignment(TextAlignment.RIGHT)
                .setMarginTop(50);
        document.add(signatureParagraph);

        document.close();

        return outputPath;
    }
}
