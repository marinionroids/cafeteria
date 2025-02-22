package com.marin.cafeteria.services;


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.marin.cafeteria.model.Order;
import com.marin.cafeteria.model.OrderProduct;
import com.marin.cafeteria.model.Product;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

@Service
public class PDFSlipGenerator {


    public byte[] generateOrderPDF(Order order) throws DocumentException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A5);
        PdfWriter.getInstance(document, outputStream);

        document.open();

        // Add header
        Font headerFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
        Paragraph header = new Paragraph("Cafe Receipt", headerFont);
        header.setAlignment(Element.ALIGN_CENTER);
        document.add(header);
        document.add(new Paragraph("\n"));

        // Add order details
        Font normalFont = new Font(Font.FontFamily.HELVETICA, 12);
        document.add(new Paragraph("Order ID: " + order.getId(), normalFont));
        document.add(new Paragraph("Date: " + order.getOrderTime(), normalFont));
        document.add(new Paragraph("Server: " + order.getServer().getUsername(), normalFont));
        document.add(new Paragraph("\n"));

        // Create items table
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{2, 1, 1, 1});

        // Add table headers
        // Add headers
        table.addCell(new PdfPCell(new Phrase("Item", normalFont)));
        table.addCell(new PdfPCell(new Phrase("Price", normalFont)));
        table.addCell(new PdfPCell(new Phrase("Quantity", normalFont)));  // Added quantity column
        table.addCell(new PdfPCell(new Phrase("Total", normalFont)));

        for (OrderProduct item : order.getOrderProducts()) {
            // Create cells for each column
            PdfPCell nameCell = new PdfPCell(new Phrase(item.getProduct().getName(), normalFont));
            PdfPCell priceCell = new PdfPCell(new Phrase(String.format("$%.2f", item.getProduct().getPrice()), normalFont));
            PdfPCell quantityCell = new PdfPCell(new Phrase(String.valueOf(item.getQuantity()), normalFont));

            // Calculate and add total for this item
            BigDecimal itemTotal = item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            PdfPCell totalCell = new PdfPCell(new Phrase(String.format("$%.2f", itemTotal), normalFont));

            // Add cells to table
            table.addCell(nameCell);
            table.addCell(priceCell);
            table.addCell(quantityCell);
            table.addCell(totalCell);
        }

        document.add(table);
        document.add(new Paragraph("\n"));

        // Add total
        Paragraph total = new Paragraph(new Phrase(String.format("Total : $%.2f", order.getTotalPrice()), new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD)));
        total.setAlignment(Element.ALIGN_RIGHT);
        document.add(total);

        // Add footer
        document.add(new Paragraph("\n"));
        Paragraph footer = new Paragraph("Thank you for your order!",
                new Font(Font.FontFamily.HELVETICA, 12, Font.ITALIC));
        footer.setAlignment(Element.ALIGN_CENTER);
        document.add(footer);

        document.close();

        return outputStream.toByteArray();
    }

}
