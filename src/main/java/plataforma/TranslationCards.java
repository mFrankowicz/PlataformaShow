package plataforma;

import com.mongodb.client.MongoCollection;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import org.bson.Document;
import org.fxmisc.richtext.StyleClassedTextArea;
import java.util.ArrayList;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.exists;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;

public class TranslationCards {
    private Pane pane;
    private AnchorPane anchorPane;
    private StyleClassedTextArea textArea;

    private ContextMenu contextMenu;

    private Font commonMenuFont = new Font("Roboto", 10);

    private ToggleButton translationNoteToggle;
    private Button saveButton;
    private ButtonBar styleButtonBar;

    public static int WIDTH = 220;
    public static int HEIGHT = 285;

    public int columnNumber;
    public int rowNumber;

    MongoCollection collection;

    public boolean saved = true;

    public TranslationCards(int _columnNumber, int _rowNumber, NoteController noteController, MongoCollection _collection) {

        columnNumber = _columnNumber;
        rowNumber = _rowNumber;

        collection = _collection;

        pane = new Pane();
        anchorPane = new AnchorPane();

        contextMenu = new ContextMenu();

        MenuItem translationNote = new MenuItem();
        translationNote.setText("adicionar nota");

        MenuItem showOnlyThisCardNote = new MenuItem();
        showOnlyThisCardNote.setText("mostrar notas");

        MenuItem hideOnlyThisCardNote = new MenuItem();
        hideOnlyThisCardNote.setText("esconder notas");

        MenuItem showAllNotes = new MenuItem();
        showAllNotes.setText("mostrar todas");

        MenuItem hideAllNotes = new MenuItem();
        hideAllNotes.setText("esconder todas");


        contextMenu.getItems().add(0, translationNote);
        contextMenu.getItems().add(1, showOnlyThisCardNote);
        contextMenu.getItems().add(2, hideOnlyThisCardNote);
        contextMenu.getItems().add(3, showAllNotes);
        contextMenu.getItems().add(4, hideAllNotes);


        textArea = new StyleClassedTextArea();
        textArea.setContextMenu(contextMenu);
        textArea.setStyle("-fx-font-family: Roboto; -fx-font-size: 14");

        pane.setMinSize(WIDTH, HEIGHT);
        pane.setPrefSize(WIDTH, HEIGHT);
        anchorPane.setMinSize(WIDTH, HEIGHT);
        anchorPane.setPrefSize(WIDTH, HEIGHT);

        pane.getChildren().add(anchorPane);
        anchorPane.getChildren().add(textArea);

        textArea.setWrapText(true);

        AnchorPane.setTopAnchor(textArea, 1.0);
        AnchorPane.setBottomAnchor(textArea, 20.0);
        AnchorPane.setLeftAnchor(textArea, 1.0);
        AnchorPane.setRightAnchor(textArea, 1.0);

        translationNoteToggle = new ToggleButton();
        translationNoteToggle.setText("N.t");
        translationNoteToggle.setFont(commonMenuFont);
        anchorPane.getChildren().add(translationNoteToggle);
        AnchorPane.setRightAnchor(translationNoteToggle, 2.0);
        AnchorPane.setBottomAnchor(translationNoteToggle, 2.0);
        translationNoteToggle.setDisable(true);

        saveButton = new Button();
        saveButton.setText("s");
        saveButton.setFont(commonMenuFont);
        anchorPane.getChildren().add(saveButton);
        AnchorPane.setLeftAnchor(saveButton, 2.0);
        AnchorPane.setBottomAnchor(saveButton, 0.0);
        saveButton.setDisable(true);

        styleButtonBar = new ButtonBar();
        styleButtonBar.setButtonMinWidth(5);
        styleButtonBar.setPadding(new Insets(0,2,0,0));
        styleButtonBar.setPrefHeight(18);
        styleButtonBar.setPrefWidth(150);
        anchorPane.getChildren().add(styleButtonBar);
        AnchorPane.setLeftAnchor(styleButtonBar, 15.0);
        AnchorPane.setBottomAnchor(styleButtonBar, 0.0);

        Button italicButton = new Button();
        italicButton.setMinWidth(10);
        italicButton.setText("i");
        italicButton.setFont(commonMenuFont);
        //italicButton.setStyle("-fx-font-style: italic;");
        //styleButtonBar.getButtons().add(italicButton);

        Button boldButton = new Button();
        boldButton.setText("b");
        boldButton.setFont(commonMenuFont);
        //boldButton.setStyle("-fx-font-weight: bold;");
        //styleButtonBar.getButtons().add(boldButton);

        Button italicBoldButton = new Button();
        italicBoldButton.setText("i.b");
        italicBoldButton.setFont(commonMenuFont);
        // italicBoldButton.setStyle("-fx-font-style: italic; -fx-font-weight: bold;");
        //styleButtonBar.getButtons().add(italicBoldButton);

        Button normalButton = new Button();
        normalButton.setText("n");
        normalButton.setFont(commonMenuFont);
        //styleButtonBar.getButtons().add(normalButton);

        italicButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                IndexRange selection = textArea.getSelection();
                if(selection.getLength() != 0) {
                    textArea.setStyleClass(selection.getStart(), selection.getEnd(), ("italic-text"));
                    //System.out.println(selection.getStart() + " - " + selection.getEnd());
                    //System.out.println(textArea.getDocument().getStyleSpans(selection.getStart(), selection.getEnd()));
                }
            }
        });

        boldButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                IndexRange selection = textArea.getSelection();
                if(selection.getLength() != 0) {
                    textArea.setStyleClass(selection.getStart(), selection.getEnd(), ("bold-text"));
                    //System.out.println(selection.getStart() + " - " + selection.getEnd());
                }
            }
        });

        italicBoldButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                IndexRange selection = textArea.getSelection();
                if(selection.getLength() != 0) {
                    textArea.setStyleClass(selection.getStart(), selection.getEnd(), ("italic-bold-text"));
                    //System.out.println(selection.getStart() + " - " + selection.getEnd());
                }
            }
        });

        normalButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                IndexRange selection = textArea.getSelection();
                if(selection.getLength() != 0) {
                    textArea.setStyleClass(selection.getStart(), selection.getEnd(), ("normal-text"));
                    //System.out.println(selection.getStart() + " - " + selection.getEnd());
                }
            }
        });

        translationNote.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                noteController.addNote(columnNumber, rowNumber);
                noteController.updateNotes();
                setNoteToggler();
            }
        });

        saveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                addNoteAsync();
                System.out.println("pressed");
            }
        });

        translationNoteToggle.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                System.out.println(newValue + " new - - old " + oldValue);
                System.out.println(observable + " observable");

                if (newValue == true) {
                    noteController.getNoteCards().forEach((noteCards) -> {
                        if (columnNumber == noteCards.columnReference
                                && rowNumber == noteCards.rowReference) {
                            noteCards.getvBoxPaneAnchorPane().setVisible(true);
                            noteCards.getvBoxPaneAnchorPane().managedProperty()
                                    .bind(noteCards.getvBoxPaneAnchorPane().visibleProperty());
                        }
                    });
                } else {
                    noteController.getNoteCards().forEach((noteCards) -> {
                        if (columnNumber == noteCards.columnReference
                                && rowNumber == noteCards.rowReference) {
                            noteCards.getvBoxPaneAnchorPane().setVisible(false);
                            noteCards.getvBoxPaneAnchorPane().managedProperty()
                                    .bind(noteCards.getvBoxPaneAnchorPane().visibleProperty());
                        }
                    });
                }
            }
        });

        showOnlyThisCardNote.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                noteController.getNoteCards().forEach((p) -> {
                    if (columnNumber == p.columnReference && rowNumber == p.rowReference) {
                        p.getvBoxPaneAnchorPane().setVisible(true);
                        p.getvBoxPaneAnchorPane().managedProperty()
                                .bind(p.getvBoxPaneAnchorPane().visibleProperty());
                    }
                });
            }
        });

        hideOnlyThisCardNote.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                noteController.getNoteCards().forEach((p) -> {
                    if (columnNumber == p.columnReference && rowNumber == p.rowReference) {
                        p.getvBoxPaneAnchorPane().setVisible(false);
                        p.getvBoxPaneAnchorPane().managedProperty()
                                .bind(p.getvBoxPaneAnchorPane().visibleProperty());
                    }
                });
            }
        });

        showAllNotes.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                noteController.getNoteCards().forEach((p) -> {
                    p.getvBoxPaneAnchorPane().setVisible(true);
                    p.getvBoxPaneAnchorPane().managedProperty()
                            .bind(p.getvBoxPaneAnchorPane().visibleProperty());
                });
            }
        });

        hideAllNotes.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                noteController.getNoteCards().forEach((p) -> {
                    p.getvBoxPaneAnchorPane().setVisible(false);
                    p.getvBoxPaneAnchorPane().managedProperty()
                            .bind(p.getvBoxPaneAnchorPane().visibleProperty());
                });
            }
        });

        textArea.setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                saved = false;
                saveButton.setDisable(false);
            }
        });

        textArea.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                saved = false;
                saveButton.setDisable(false);
            }
        });
    }


    public void addNoteAsync() {
        System.out.println("Entered Async");
        Runnable task = () -> addNotTask();

        Thread backgroundThread = new Thread(task);
        backgroundThread.setDaemon(true);
        backgroundThread.start();

    }

    public void applyFormatation(Document formatList){
        if(formatList != null){
            ArrayList<Document> style = (ArrayList<Document>) formatList.get("styles");
            style.forEach((d) ->{
                int start = d.getInteger("start");
                System.out.println(start);
                int end = d.getInteger("end");
                System.out.println(end);
                ArrayList styleName = (ArrayList) d.get("style");
                if(styleName.size() != 0) {
                    textArea.setStyleClass(start, end, styleName.get(0).toString());
                }
            });
        }
    }

    private void addNotTask(){

        int column = columnNumber;
        switch (column) {

            case 1 :
                    collection
                    .updateOne(combine(eq("line", rowNumber), exists("column_one")),
                            combine(set("column_one", textArea.getText()), set("column_one_format", Codec.encode(textArea))));
                    System.out.println("column_one" + " " + rowNumber);
                    System.out.println(Codec.encode(textArea));
                break;

            case 2 : collection
                    .updateOne(combine(eq("line", rowNumber), exists("column_two")),
                            combine(set("column_two", textArea.getText()), set("column_two_format", Codec.encode(textArea))));
                    System.out.println("column_two" + " " + rowNumber);
                    System.out.println(Codec.encode(textArea));
                break;

            case 3 : collection
                    .updateOne(combine(eq("line", rowNumber), exists("column_three")),
                            combine(set("column_three", textArea.getText()), set("column_three_format", Codec.encode(textArea))));
                    System.out.println("column_three" + " " + rowNumber);
                    System.out.println(Codec.encode(textArea));
                break;

            case 4 : collection
                    .updateOne(combine(eq("line", rowNumber), exists("column_four")),
                            combine(set("column_four", textArea.getText()), set("column_four_format", Codec.encode(textArea))));
                    System.out.println("column_four" + " " + rowNumber);
                    System.out.println(Codec.encode(textArea));
                break;

        }
        saved = true;
        saveButton.setDisable(true);
    }

    public void setNoteToggler() {
        translationNoteToggle.setDisable(false);
        translationNoteToggle.disarm();
    }

    //get and setters
    public Pane getPane() {
        return pane;
    }

    public void setPane(Pane pane) {
        this.pane = pane;
    }

    public StyleClassedTextArea getTextArea() {
        return textArea;
    }

    public void setTextArea(StyleClassedTextArea textArea) {
        this.textArea = textArea;
    }

    public AnchorPane getAnchorPane() {
        return anchorPane;
    }

    public void setAnchorPane(AnchorPane anchorPane) {
        this.anchorPane = anchorPane;
    }

    public ToggleButton getTranslationNoteToggle() {
        return translationNoteToggle;
    }

    public void setTranslationNoteToggle(ToggleButton translationNoteToggle) {
        this.translationNoteToggle = translationNoteToggle;
    }

    public Button getSaveButton() {
        return saveButton;
    }

    public void setSaveButton(Button saveButton) {
        this.saveButton = saveButton;
    }
}
