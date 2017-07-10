package com.wt.issuetracker.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfString;
import com.itextpdf.text.pdf.PdfWriter;
import com.wt.issuetracker.entity.Issue;
import com.wt.issuetracker.entity.Message;


public class CustomPDFBuilder extends AbstractCustomPDFView{

    @Override
    protected void buildPdfDocument(Map<String, Object> model, Document doc,
            PdfWriter writer, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        // get data model which is passed by the Spring container
        
    	Boolean b = (Boolean)model.get("error");
    	if(!b){
    	
    	List<Message> messageList = (List<Message>) model.get("messageList");
        Issue issue = (Issue)model.get("pdfIssue");
    	
        Paragraph headerParagraph = new Paragraph("Issue Details");
        headerParagraph.setAlignment(Element.ALIGN_CENTER);
        Font boldFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
        headerParagraph.setFont(boldFont);
        doc.add(headerParagraph);
    	
    	Paragraph issueP = new Paragraph();
    	
    	issueP.add(new Chunk("Issue Id : "+issue.getIssueId()));
    	issueP.add(Chunk.NEWLINE);
    	issueP.add(new Chunk("Title : "+issue.getTitle()));
    	issueP.add(Chunk.NEWLINE);
    	issueP.add(new Chunk("Description : "+issue.getDescription()));
    	issueP.add(Chunk.NEWLINE);
    	issueP.add(new Chunk("Assigned To : "+issue.getPrimaryAssigneeId() + " - " + issue.getPrimaryAssigneeId().getFirstName() + " " + issue.getPrimaryAssigneeId().getLastName()));
    	issueP.add(Chunk.NEWLINE);
    	issueP.add(new Chunk("Team Id : "+issue.getTeamId().getTeamId()));
    	issueP.add(Chunk.NEWLINE);
    	issueP.add(new Chunk("Team Name : "+issue.getTeamId().getTeamName() ));
    	issueP.add(Chunk.NEWLINE);
    	issueP.add(new Chunk("Created On : "+issue.getCreationDate() ));
    	issueP.add(Chunk.NEWLINE);
    	issueP.add(new Chunk("Status : " + issue.getStatus() ));
    	issueP.add(Chunk.NEWLINE);
    	issueP.add(Chunk.NEWLINE);
    	
    	doc.add(issueP);
    	
    	doc.add(new Paragraph("Actions Log"));
         
        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100.0f);
        table.setWidths(new float[] {1.0f, 3.0f, 2.0f, 2.0f, 2.0f});
        table.setSpacingBefore(10);
         
        // define font for table header row
        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(BaseColor.WHITE);
         
        // define table header cell
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(BaseColor.DARK_GRAY);
        cell.setPadding(5);
         
        // write table header
        
       cell.setPhrase(new Phrase("Message Id", font));
       table.addCell(cell);
        
        cell.setPhrase(new Phrase("Message", font));
        table.addCell(cell);
         
        cell.setPhrase(new Phrase("Date", font));
        table.addCell(cell);
 
        cell.setPhrase(new Phrase("Sender", font));
        table.addCell(cell);
         
        cell.setPhrase(new Phrase("Action Performed", font));
        table.addCell(cell);

         
        // write table row data
        for (Message aMessage : messageList) {
            table.addCell("" + aMessage.getMessageId());
            table.addCell(aMessage.getDescription());
            table.addCell(aMessage.getSentDate().toString());
            table.addCell(aMessage.getSenderName());
            table.addCell(aMessage.getActionPerformed());
        }
         
        doc.add(table);
    	}
    	else{

    		doc.add(new Paragraph("ERROR OCCURRED"));
    	}
    	
    }
}
