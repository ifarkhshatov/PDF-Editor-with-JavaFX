package sample;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ResourceBundle;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.fxml.FXML;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.control.Button;
import javafx.scene.web.HTMLEditor;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebHistory;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.fit.pdfdom.PDFDomTree;
import org.w3c.dom.Document;

import javax.print.PrintService;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class Controller {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;


    @FXML
    private WebView pdfView;

    @FXML
    private Button actionBtn;

    @FXML
    private Button resetBtn;

    @FXML
    private Button chooserBtn;


    public static void generateHTMLFromPDF(PDDocument pdf) throws IOException, ParserConfigurationException {
        Writer output = new PrintWriter("./src/output/backup.html", StandardCharsets.UTF_8);
        new PDFDomTree().writeText(pdf, output);
        output.close();
        pdf.close();
    }

    @FXML
    void initialize() throws IOException, ParserConfigurationException {

        WebEngine render = pdfView.getEngine();
        chooserBtn.setOnAction(actionEvent -> {
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PDF", "*.pdf","*.PDF");
            fileChooser.getExtensionFilters().add(extFilter);
            File file = fileChooser.showOpenDialog(chooserBtn.getScene().getWindow());

            if (file != null) {
            PDDocument pdf = null;


            try {
                pdf = PDDocument.load(file);
                generateHTMLFromPDF(pdf);
                HtmlEditor htmlEditor = new HtmlEditor("./src/output/backup.html");
                render.loadContent(htmlEditor.EditHTML(),"text/html");
            } catch (IOException | ParserConfigurationException e) {
                e.printStackTrace();
            }

        }
        });

        actionBtn.setOnAction(actionEvent -> {
            System.out.println("Stored file");

            PrinterJob job = PrinterJob.createPrinterJob();
            job.showPrintDialog(actionBtn.getScene().getWindow());
            render.print(job);
            job.endJob();
        });

        resetBtn.setOnAction(actionEvent -> {
            File tempFile = new File("./src/output/backup.html");
            boolean exists = tempFile.exists();
            if (exists) {
                HtmlEditor htmlEditor = new HtmlEditor("./src/output/backup.html");

                try {
                    pdfView.getEngine().loadContent(htmlEditor.EditHTML(), "text/html");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
