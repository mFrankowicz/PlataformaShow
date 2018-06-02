package plataforma;

import javafx.scene.text.Text;

public class NoteLink {

    public final int columnNumberReference;
    public final int rowNumberReference;

    Text linkText;

    public NoteLink(int column, int row){
        columnNumberReference = column;
        rowNumberReference = row;
        linkText = new Text();
    }

    public void setLink(TranslationController translationController){

    }


    // getters and setters
    public Text getLinkText() {
        return linkText;
    }

    public void setLinkText(Text linkText) {
        this.linkText = linkText;
    }

}
