import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Highlighter;
import javax.swing.text.JTextComponent;
import java.awt.*;


//A simple class that searches for a word in
//a document and highlights occurrences of that word

class WordSearcher {
public WordSearcher(JTextComponent comp) {
 this.comp = comp;
 this.painter = new UnderlineHighlighter.UnderlineHighlightPainter(
     Color.red);
}

// Search for a word and return the offset of the
// first occurrence. Highlights are added for all
// occurrences found.
public int search(String word) {
 int firstOffset = -1;
 Highlighter highlighter = comp.getHighlighter();

 // Remove any existing highlights for last word
 Highlighter.Highlight[] highlights = highlighter.getHighlights();
 for (int i = 0; i < highlights.length; i++) {
   Highlighter.Highlight h = highlights[i];
   if (h.getPainter() instanceof UnderlineHighlighter.UnderlineHighlightPainter) {
     highlighter.removeHighlight(h);
   }
 }

 if (word == null || word.equals("")) {
   return -1;
 }

 // Look for the word we are given - insensitive search
 String content = null;
 try {
   Document d = comp.getDocument();
   content = d.getText(0, d.getLength()).toLowerCase();
 } catch (BadLocationException e) {
   // Cannot happen
   return -1;
 }

 word = word.toLowerCase();
 int lastIndex = 0;
 int wordSize = word.length();

 while ((lastIndex = content.indexOf(word, lastIndex)) != -1) {
   int endIndex = lastIndex + wordSize;
   try {
     highlighter.addHighlight(lastIndex, endIndex, painter);
   } catch (BadLocationException e) {
     // Nothing to do
   }
   if (firstOffset == -1) {
     firstOffset = lastIndex;
   }
   lastIndex = endIndex;
 }

 return firstOffset;
}

protected JTextComponent comp;

protected Highlighter.HighlightPainter painter;

}