package com.tallerjava.taller.service;

import com.tallerjava.taller.model.Novedad;
import com.tallerjava.taller.model.Usuario;
import com.tallerjava.taller.model.Ficha;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class NovedadExportService {

    /**
     * Exporta lista de novedades a Excel (.xlsx)
     */
    public byte[] exportarNovedadesExcel(List<Novedad> novedades) throws IOException {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            
            Sheet sheet = workbook.createSheet("Novedades");

            // Estilo para encabezados
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.WHITE.getIndex());
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.DARK_GREEN.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);

            // Estilo para datos
            CellStyle dataStyle = workbook.createCellStyle();
            dataStyle.setWrapText(true);
            dataStyle.setVerticalAlignment(VerticalAlignment.TOP);

            // Crear encabezados
            Row headerRow = sheet.createRow(0);
            String[] columnas = {"ID", "Fecha", "Ficha", "Instructor", "Título", "Descripción", "Respuesta", "Estado", "Tipo"};
            
            for (int i = 0; i < columnas.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columnas[i]);
                cell.setCellStyle(headerStyle);
            }

            // Llenar datos
            int rowNum = 1;
            for (Novedad novedad : novedades) {
                Row row = sheet.createRow(rowNum++);

                row.createCell(0).setCellValue(novedad.getId() != null ? novedad.getId() : 0);
                row.createCell(1).setCellValue(novedad.getFecha() != null ? novedad.getFecha().toString() : "");
                
                Ficha ficha = novedad.getFicha();
                row.createCell(2).setCellValue(ficha != null ? ficha.getNumeroFicha() : "N/A");
                
                Usuario instructor = novedad.getInstructor();
                String nombreInstructor = "N/A";
                if (instructor != null) {
                    nombreInstructor = (instructor.getNombre() != null ? instructor.getNombre() : "") + 
                                     " " + (instructor.getApellido() != null ? instructor.getApellido() : "");
                    nombreInstructor = nombreInstructor.trim();
                }
                row.createCell(3).setCellValue(nombreInstructor);
                
                row.createCell(4).setCellValue(novedad.getTitulo() != null ? novedad.getTitulo() : "");
                row.createCell(5).setCellValue(novedad.getDescripcion() != null ? novedad.getDescripcion() : "");
                row.createCell(6).setCellValue(novedad.getRespuesta() != null ? novedad.getRespuesta() : "Sin respuesta");
                row.createCell(7).setCellValue(novedad.getEstado() != null ? novedad.getEstado() : "Pendiente");
                row.createCell(8).setCellValue(novedad.getTipo() != null ? novedad.getTipo() : "Comunicado");

                // Aplicar estilo a las celdas
                for (int i = 0; i < columnas.length; i++) {
                    row.getCell(i).setCellStyle(dataStyle);
                }
            }

            // Autoajustar columnas
            for (int i = 0; i < columnas.length; i++) {
                sheet.autoSizeColumn(i);
                // Limitar ancho máximo para columnas de texto largo
                if (i >= 4 && i <= 6) {
                    sheet.setColumnWidth(i, 15000); // Ancho máximo para descripción y respuesta
                }
            }

            workbook.write(out);
            return out.toByteArray();
        }
    }

    /**
     * Exporta lista de novedades a HTML imprimible como PDF
     */
    public byte[] exportarNovedadesPDF(List<Novedad> novedades) throws IOException {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html><html><head><meta charset='UTF-8'>");
        html.append("<style>");
        html.append("body { font-family: Arial, sans-serif; margin: 20px; }");
        html.append("h1 { color: #15803d; text-align: center; }");
        html.append("table { width: 100%; border-collapse: collapse; margin-top: 20px; font-size: 12px; }");
        html.append("th { background-color: #15803d; color: white; padding: 10px; text-align: left; }");
        html.append("td { border: 1px solid #ddd; padding: 8px; }");
        html.append("tr:nth-child(even) { background-color: #f9fafb; }");
        html.append(".footer { margin-top: 20px; text-align: right; color: #666; }");
        html.append("@media print { body { margin: 0; } }");
        html.append("</style>");
        html.append("</head><body>");
        html.append("<h1>Reporte de Novedades</h1>");
        html.append("<p class='footer'>Generado: " + java.time.LocalDate.now() + "</p>");
        html.append("<table>");
        html.append("<thead><tr>");
        html.append("<th>ID</th><th>Fecha</th><th>Ficha</th><th>Instructor</th>");
        html.append("<th>Título</th><th>Descripción</th><th>Estado</th><th>Tipo</th>");
        html.append("</tr></thead><tbody>");
        
        for (Novedad novedad : novedades) {
            html.append("<tr>");
            html.append("<td>").append(novedad.getId() != null ? novedad.getId() : 0).append("</td>");
            html.append("<td>").append(novedad.getFecha() != null ? novedad.getFecha().toString() : "").append("</td>");
            
            Ficha ficha = novedad.getFicha();
            html.append("<td>").append(ficha != null ? escapeHtml(ficha.getNumeroFicha()) : "N/A").append("</td>");
            
            Usuario instructor = novedad.getInstructor();
            String nombreInstructor = "N/A";
            if (instructor != null) {
                nombreInstructor = (instructor.getNombre() != null ? instructor.getNombre() : "") + 
                                 " " + (instructor.getApellido() != null ? instructor.getApellido() : "");
                nombreInstructor = nombreInstructor.trim();
            }
            html.append("<td>").append(escapeHtml(nombreInstructor)).append("</td>");
            html.append("<td>").append(escapeHtml(novedad.getTitulo() != null ? novedad.getTitulo() : "")).append("</td>");
            
            String desc = novedad.getDescripcion() != null ? novedad.getDescripcion() : "";
            if (desc.length() > 100) {
                desc = desc.substring(0, 97) + "...";
            }
            html.append("<td>").append(escapeHtml(desc)).append("</td>");
            html.append("<td>").append(escapeHtml(novedad.getEstado() != null ? novedad.getEstado() : "Pendiente")).append("</td>");
            html.append("<td>").append(escapeHtml(novedad.getTipo() != null ? novedad.getTipo() : "Comunicado")).append("</td>");
            html.append("</tr>");
        }
        
        html.append("</tbody></table>");
        html.append("<p class='footer'>Total de novedades: " + novedades.size() + "</p>");
        html.append("<script>window.print();</script>"); // Auto-imprimir al abrir
        html.append("</body></html>");
        
        return html.toString().getBytes("UTF-8");
    }

    /**
     * Exporta lista de novedades a CSV
     */
    public byte[] exportarNovedadesCSV(List<Novedad> novedades) throws IOException {
        StringBuilder csv = new StringBuilder();
        
        // BOM para UTF-8 (ayuda a Excel a detectar el encoding)
        csv.append('\ufeff');
        
        // Encabezados
        csv.append("ID,Fecha,Ficha,Instructor,Título,Descripción,Respuesta,Estado,Tipo\n");
        
        // Datos
        for (Novedad novedad : novedades) {
            csv.append(escapeCsv(novedad.getId() != null ? novedad.getId().toString() : "0")).append(",");
            csv.append(escapeCsv(novedad.getFecha() != null ? novedad.getFecha().toString() : "")).append(",");
            
            Ficha ficha = novedad.getFicha();
            csv.append(escapeCsv(ficha != null ? ficha.getNumeroFicha() : "N/A")).append(",");
            
            Usuario instructor = novedad.getInstructor();
            String nombreInstructor = "N/A";
            if (instructor != null) {
                nombreInstructor = (instructor.getNombre() != null ? instructor.getNombre() : "") + 
                                 " " + (instructor.getApellido() != null ? instructor.getApellido() : "");
                nombreInstructor = nombreInstructor.trim();
            }
            csv.append(escapeCsv(nombreInstructor)).append(",");
            
            csv.append(escapeCsv(novedad.getTitulo() != null ? novedad.getTitulo() : "")).append(",");
            csv.append(escapeCsv(novedad.getDescripcion() != null ? novedad.getDescripcion() : "")).append(",");
            csv.append(escapeCsv(novedad.getRespuesta() != null ? novedad.getRespuesta() : "Sin respuesta")).append(",");
            csv.append(escapeCsv(novedad.getEstado() != null ? novedad.getEstado() : "Pendiente")).append(",");
            csv.append(escapeCsv(novedad.getTipo() != null ? novedad.getTipo() : "Comunicado")).append("\n");
        }
        
        return csv.toString().getBytes("UTF-8");
    }

    /**
     * Escapa valores para formato CSV
     */
    private String escapeCsv(String value) {
        if (value == null) return "";
        
        // Si contiene coma, comillas o salto de línea, envolver en comillas
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            // Duplicar comillas internas
            value = value.replace("\"", "\"\"");
            return "\"" + value + "\"";
        }
        return value;
    }

    /**
     * Escapa HTML
     */
    private String escapeHtml(String value) {
        if (value == null) return "";
        return value.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;")
                   .replace("\"", "&quot;")
                   .replace("'", "&#39;");
    }
}
