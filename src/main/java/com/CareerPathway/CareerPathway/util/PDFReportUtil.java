package com.CareerPathway.CareerPathway.util;
import com.CareerPathway.CareerPathway.dto.CareerPathProgressDetail;
import com.CareerPathway.CareerPathway.dto.ProgressMetricsDTO;
import com.CareerPathway.CareerPathway.dto.SkillAssessmentDetail;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Component
public class PDFReportUtil {

    public byte[] generatePdfReport(ProgressMetricsDTO metrics) throws DocumentException, IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, outputStream);

        document.open();

        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK);
        Paragraph title = new Paragraph("Employee Progress Report", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        document.add(new Paragraph(" "));

        document.add(new Paragraph("Skill Assessment Progress: " + metrics.getSkillAssessmentProgress() + "%"));

        document.add(new Paragraph("Career Path Progress: " + metrics.getCareerPathProgress() + "%"));

        document.add(new Paragraph("Training Progress: " + metrics.getTrainingProgress() + "%"));

        document.add(new Paragraph("Goal Progress: " + metrics.getGoalProgress() + "%"));

        document.add(new Paragraph(" "));
        document.add(new Paragraph("Skill Assessments:"));
        PdfPTable skillTable = new PdfPTable(4);
        skillTable.addCell("Skill Name");
        skillTable.addCell("Score");
        skillTable.addCell("Max Score");
        skillTable.addCell("Progress (%)");

        for (SkillAssessmentDetail skill : metrics.getSkillAssessmentDetails()) {
            skillTable.addCell(skill.getSkillName());
            skillTable.addCell(String.valueOf(skill.getScore()));
            skillTable.addCell(String.valueOf(skill.getMaxScore()));
            skillTable.addCell(String.valueOf(skill.getProgressPercentage()));
        }
        document.add(skillTable);

        document.add(new Paragraph(" "));
        document.add(new Paragraph("Career Paths:"));
        PdfPTable careerTable = new PdfPTable(4);
        careerTable.addCell("Career Path Name");
        careerTable.addCell("Total Steps");
        careerTable.addCell("Completed Steps");
        careerTable.addCell("Progress (%)");

        for (CareerPathProgressDetail career : metrics.getCareerPathProgressDetails()) {
            careerTable.addCell(career.getCareerPathName());
            careerTable.addCell(String.valueOf(career.getTotalSteps()));
            careerTable.addCell(String.valueOf(career.getCompletedSteps()));
            careerTable.addCell(String.valueOf(career.getProgressPercentage()));
        }
        document.add(careerTable);

        document.close();
        return outputStream.toByteArray();
    }
}
