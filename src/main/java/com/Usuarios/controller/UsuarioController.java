/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.Usuarios.controller;

import com.Usuarios.model.Usuario;
import com.Usuarios.service.UsuarioService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.servlet.http.HttpServletResponse;

// importaciones pdf
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;

// importaciones excel
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.util.List;

/**
 *
 * @author casti
 */
@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService usuarioService) {
        this.service = usuarioService;
    }

    @GetMapping
    public String listarUsuarios(Model model) {
        model.addAttribute("usuarios", this.service.listarTodos());
        return "usuarios";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioCrear(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "formulario";
    }

    @PostMapping
    public String guardarUsuario(@ModelAttribute Usuario usuario) {
        this.service.guardar(usuario);
        return "redirect:/usuarios";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        model.addAttribute("usuario", this.service.buscarPorId(id).orElseThrow(() -> new IllegalArgumentException("ID inválido: " + id)));
        return "formulario";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarUsuario(@PathVariable Long id) {
        service.eliminar(id);
        return "redirect:/usuarios";
    }

    @GetMapping("/reporte/pdf")
    public void generarPdf(HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=usuarios_reporte.pdf");

        PdfWriter writer = new PdfWriter(response.getOutputStream());
        Document document = new Document(new com.itextpdf.kernel.pdf.PdfDocument(writer));

        document.add(new Paragraph("Reporte de Usuarios").setBold().setFontSize(18));

        Table table = new Table(6);
        table.addCell("ID");
        table.addCell("Nombre");
        table.addCell("Apellido");
        table.addCell("Número Celular");
        table.addCell("Edad");
        table.addCell("Rol");

        List<Usuario> usuarios = this.service.listarTodos();
        for (Usuario usuario : usuarios) {
            table.addCell(usuario.getId().toString());
            table.addCell(usuario.getNombre());
            table.addCell(usuario.getApellido());
            table.addCell(usuario.getNumeroCelular());
            table.addCell(String.valueOf(usuario.getEdad()));
            table.addCell(usuario.getRol());
        }

        document.add(table);
        document.close();
    }

    @GetMapping("/reporte/excel")
    public void generarExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=usuarios_reporte.xlsx");

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Usuarios");

        Row headerRow = sheet.createRow(0);
        String[] columnHeaders = {"ID", "Nombre", "Apellido", "Número Celular", "Edad", "Rol"};
        for (int i = 0; i < columnHeaders.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columnHeaders[i]);
            CellStyle style = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setBold(true);
            style.setFont(font);
            cell.setCellStyle(style);
        }

        List<Usuario> usuarios = this.service.listarTodos();
        int rowIndex = 1;
        for (Usuario usuario : usuarios) {
            Row row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(usuario.getId());
            row.createCell(1).setCellValue(usuario.getNombre());
            row.createCell(2).setCellValue(usuario.getApellido());
            row.createCell(3).setCellValue(usuario.getNumeroCelular());
            row.createCell(4).setCellValue(usuario.getEdad());
            row.createCell(5).setCellValue(usuario.getRol());
        }

        for (int i = 0; i < columnHeaders.length; i++) {
            sheet.autoSizeColumn(i);
        }

        workbook.write(response.getOutputStream());
        workbook.close();
    }
}
