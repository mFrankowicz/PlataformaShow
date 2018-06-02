package plataforma;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.extern.java.Log;
import org.bson.Document;
import org.fxmisc.richtext.StyleClassedTextArea;
import org.fxmisc.richtext.model.StyledDocument;
import org.javatuples.Triplet;


import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

@Log
public class Codec {

    public static Document encode(StyleClassedTextArea textArea){

        AtomicInteger start = new AtomicInteger(0);
        AtomicInteger end = new AtomicInteger(0);

        ObservableList<Triplet<Integer,Integer,Collection<String>>> formatIndex = FXCollections.observableArrayList();

        StyledDocument<Collection<String>, String, Collection<String>> doc = textArea.getDocument();
        doc.getStyleSpans(0,doc.length()).forEach((p) -> {
            Triplet<Integer, Integer, Collection<String>> i =
                    Triplet.with(
                            start.addAndGet(p.getLength())-p.getLength(),
                            end.addAndGet(p.getLength()),
                            p.getStyle());


            formatIndex.add(i);
        });

        ObservableList<Document> styles = FXCollections.observableArrayList();
        formatIndex.forEach((t) -> {
            Document style = new Document();
            style.append("start", t.getValue0())
                    .append("end", t.getValue1())
                    .append("style", t.getValue2());
            styles.add(style);
        });

        Document formatDocument = new Document();
        formatDocument
                .append("length", doc.length())
                .append("styles",styles);

        return formatDocument;
    }

    public void decode(){

    }
}
