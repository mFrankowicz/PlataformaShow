package plataforma;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;

public class NoteCards {

    private AnchorPane vBoxPaneAnchorPane;
    private TextArea textArea;
    private Label header;

    public int columnReference;
    public int rowReference;

    public boolean isInDatabase;

    public NoteCards(int column, int row){

        columnReference = column;
        rowReference = row;

        vBoxPaneAnchorPane = new AnchorPane();
        textArea = new TextArea();
        textArea.setWrapText(true);

        header = new Label();

        vBoxPaneAnchorPane.setPrefSize(265, 250);
        vBoxPaneAnchorPane.setMinSize(265,30);
        vBoxPaneAnchorPane.setMaxWidth(290);

        header.setText("");
        vBoxPaneAnchorPane.getChildren().add(textArea);
        vBoxPaneAnchorPane.getChildren().add(header);

        AnchorPane.setTopAnchor(textArea, 30.0);
        AnchorPane.setLeftAnchor(textArea, 3.0);
        AnchorPane.setRightAnchor(textArea, 3.0);
        AnchorPane.setBottomAnchor(textArea, 3.0);

        AnchorPane.setTopAnchor(header, 3.0);
        AnchorPane.setLeftAnchor(header, 3.0);
        AnchorPane.setRightAnchor(header, 3.0);
        AnchorPane.setBottomAnchor(header, 225.0);

    }


    public AnchorPane getvBoxPaneAnchorPane() {
        return vBoxPaneAnchorPane;
    }

    public void setvBoxPaneAnchorPane(AnchorPane vBoxPaneAnchorPane) {
        this.vBoxPaneAnchorPane = vBoxPaneAnchorPane;
    }

    public TextArea getTextArea() {
        return textArea;
    }

    public void setTextArea(TextArea textArea) {
        this.textArea = textArea;
    }

    public Label getHeader() {
        return header;
    }

    public void setHeader(Label header) {
        this.header = header;
    }
}
