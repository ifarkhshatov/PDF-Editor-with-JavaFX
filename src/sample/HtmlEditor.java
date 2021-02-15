package sample;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

public class HtmlEditor {
    private String source;

    public HtmlEditor(String source) {
        this.source = source;
    }

    public String EditHTML() throws IOException {
        File input = new File(this.source);

        Document doc = Jsoup.parse(input, "UTF-8");

        doc.select("body").attr("contenteditable","true");
        doc.select("head").prepend("<style type=\"text/css\">div.p{width:auto!important;}   .page{border: 1px transparent!important;}  @page {\n" +
                "        size: auto;\n" +
                "        margin: 0;\n" +
                "    }\n" +
                "    @media print {\n" +
                "        html, body {\n" +
                "            width: 210mm;\n" +
                "            height: 297mm;        \n" +
                "        }\n" +
                "        .page {\n" +
                "            margin: 0;\n" +
                "            border: initial;\n" +
                "            border-radius: initial;\n" +
                "            width: initial;\n" +
                "            min-height: initial;\n" +
                "            box-shadow: initial;\n" +
                "            background: initial;\n" +
                "            page-break-after: always;\n" +
                "        }\n" +
                "    }");

        doc.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
        return doc.html();
    }

}
