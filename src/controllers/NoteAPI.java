package controllers;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import models.Note;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;


public class NoteAPI {
    private ArrayList<Note> notes = new ArrayList();

    public boolean add(Note note) {
        return this.notes.add(note);
    }

    public boolean updateNote(int indexToUpdate, String noteTitle, int notePriority, String noteCategory) {
        Note foundNote = this.findNote(indexToUpdate);
        if (foundNote != null) {
            foundNote.setNoteTitle(noteTitle);
            foundNote.setNotePriority(notePriority);
            foundNote.setNoteCategory(noteCategory);
            return true;
        } else {
            return false;
        }
    }

    public Note deleteNote(int indexToDelete) {
        if (!isValidIndex(indexToDelete)) {
            return null;
        }
        return this.isValidIndex(indexToDelete) ? (Note) this.notes.remove(indexToDelete) : findNote(indexToDelete);
    }

    public boolean archiveNote(int indexToArchive) {
        if (notes.isEmpty()) {
            return false;
        }
        if (!isValidIndex(indexToArchive)) {
            return false;
        }

        if (notes.get(indexToArchive).isNoteArchived()) {
            return false;
        }

        if (notes.get(indexToArchive).numberOfTODOItem() == 0) {
            notes.get(indexToArchive).setNoteArchived(true);
            return true;
        }
        return false;
    }

    public String archiveNotesWithAllItemsComplete() {
        if (notes.isEmpty()) {
            return "No active notes stored";
        }
        if (numberOfActiveNotes() == 0) {
            return "No active notes stored";
        }
        if (numberOfActiveNotes() != 0) {
            for (int i = 0; i < notes.size(); i++) {
                if (notes.get(i).numberOfTODOItem() == 0) {
                    archiveNote(i);
                }
            }
        }
        return "Archived";
    }

    public int numberOfNotes() {
        return this.notes.size();
    }

    public int numberOfArchivedNotes() {
        int archived = 0;
        for (int i = 0; i < notes.size(); ++i) {
            if (notes.get(i).isNoteArchived()) {
                archived++;
            }
        }
        return archived;
    }

    public int numberOfActiveNotes() {
        int active = 0;
        for (int i = 0; i < notes.size(); ++i) {
            if (!notes.get(i).isNoteArchived()) {
                active++;
            }
        }
        return active;
    }

    public int numberOfNotesByCategory(String category) {
        int byCategory = 0;
        for (int i = 0; i < notes.size(); i++) {
            if (notes.get(i).getNoteCategory().equalsIgnoreCase(category)) {
                byCategory++;
            }
        }
        return byCategory;
    }

    public int numberOfNotesByTitle(String title) {
        int byTitle = 0;
        for (int i = 0; i < notes.size(); i++) {
            if (notes.get(i).getNoteTitle().contains(title)) {
                byTitle++;
            }
        }
        return byTitle;
    }

    public int numberOfNotesByPriority(int priority) {
        int byPriority = 0;
        for (Note note : notes) {
            if (priority == note.getNotePriority()) {
                byPriority++;
            }
        }
        return byPriority;
    }

    public int numberOfItems() {
        return numberOfTodoItems() + numberOfCompleteItems();
    }

    public int numberOfCompleteItems() {
        int numComplete = 0;
        for (Note note : notes) {
            numComplete += note.numberOfCompletedItems();
        }
        return numComplete;
    }

    public int numberOfTodoItems() {
        int numTodo = 0;
        for (Note note : notes) {
            numTodo += note.numberOfTODOItem();
        }
        return numTodo;
    }

    public String listAllNotes() {
        if (this.notes.isEmpty()) {
            return "no notes";
        } else {
            String listOfNotes = "";

            for (int i = 0; i < this.notes.size(); ++i) {
                listOfNotes = listOfNotes + i + ": " + this.notes.get(i) + "\n";
            }

            return listOfNotes;
        }
    }

    public String listActiveNotes() {
        if (this.notes.isEmpty()) {
            return "No Active Notes";
        }
        if (numberOfActiveNotes() == 0) {
            return "No Active Notes";
        } else {
            String listOfActive = "";

            for (int i = 0; i < notes.size(); i++) {
                if (!notes.get(i).isNoteArchived())
                    listOfActive = listOfActive + this.notes.get(i) + "\n";
            }

            return listOfActive;
        }
    }

    public String listArchivedNotes() {
        if (this.notes.isEmpty()) {
            return "No Archived Notes";
        }
        if (numberOfActiveNotes() == 0) {
            return "No Archived Notes";
        } else {
            String listOfArchived = "";

            for (int i = 0; i < notes.size(); i++) {
                if (notes.get(i).isNoteArchived())
                    listOfArchived = listOfArchived + this.notes.get(i) + "\n";
            }

            return listOfArchived;
        }
    }

    public String listNotesBySelectedCategory(String category) {
        if (this.notes.isEmpty()) {
            return "No Notes";
        }
        if (numberOfNotesByCategory(category) == 0) {
            return category + "No Notes";
        } else {
            String listByCategory = "";
            listByCategory = listByCategory + "" + '\n';
            listByCategory = listByCategory + numberOfNotesByCategory(category) + " Note" + "\n";
            for (int i = 0; i < this.notes.size(); ++i) {
                if (category.equalsIgnoreCase(notes.get(i).getNoteCategory())) {
                    listByCategory = listByCategory + this.notes.get(i);
                }
            }
            return listByCategory;
        }
    }

    public String listNotesBySelectedPriority(int priority) {
        if (this.notes.isEmpty()) {
            return "No Notes";
        }
        if (numberOfNotesByPriority(priority) == 0) {
            return "no notes" + priority;
        } else {
            String listByPriority = "";
            listByPriority = listByPriority + this.numberOfNotesByPriority(priority) + " note" + "\n";
            listByPriority = listByPriority + "priority " + priority + "\n";
            for (int i = 0; i < this.notes.size(); ++i) {
                if (priority == notes.get(i).getNotePriority()) {
                    listByPriority = listByPriority + this.notes.get(i) + "\n";
                }
            }

            return listByPriority;
        }
    }

    public String listTodoItems() {
        if (this.notes.isEmpty()) {
            return "No notes stored";
        } else {
            String listTODO = "";
            for (int i = 0; i < notes.size(); i++) {
                if (notes.get(i).numberOfTODOItem() != 0) {
                    listTODO = listTODO + notes.get(i).getNoteTitle() + '\n' + notes.get(i).listTODO() + "\n";
                }
            }


            return listTODO;
        }
    }


    public String listItemStatusByCategory(String category) {
        String categoryStatus = "";
        int numberComplete = 0;
        int numberTODO = 0;

        if (this.notes.isEmpty()) {
            return "No Notes";
        }

        if (numberOfNotesByCategory(category) != 0) {
            for (int i = 0; i < numberOfNotes(); i++) {
                if (notes.get(i).getNoteCategory().equals(category)) {
                    numberComplete = numberComplete + notes.get(i).numberOfCompletedItems();
                    numberTODO = numberTODO + notes.get(i).numberOfTODOItem();
                    categoryStatus = categoryStatus + "number completed: " + numberComplete + '\n';
                    categoryStatus = categoryStatus + notes.get(i).listComplete() + '\n';
                    categoryStatus = categoryStatus + "number todo: " + numberTODO + '\n';
                    categoryStatus = categoryStatus + notes.get(i).listTODO() + '\n';
                }
            }
        } else {
            categoryStatus = categoryStatus + "number completed: " + numberComplete + '\n';
            categoryStatus = categoryStatus + "number todo: " + numberComplete + '\n';
        }
        return categoryStatus;
    }

    public Note findNote(int index) {
        return this.isValidIndex(index) ? (Note) this.notes.get(index) : null;
    }

    public String searchNotesByTitle(String searchString) {
        String listByTitle = "";
        if (this.notes.isEmpty()) {
            return "No Notes";
        }


        if (numberOfNotesByTitle(searchString) == 0) {
            listByTitle = listByTitle + "No items found for: " + searchString + '\n';
        }

        if (numberOfNotesByTitle(searchString) != 0) {
            for (int i = 0; i < notes.size(); i++) {
                if (notes.get(i).getNoteTitle().contains(searchString)) {
                    listByTitle = listByTitle + i + ": " + notes.get(i);
                }
            }
        }
        return listByTitle;
    }


    public String searchItemByDescription(String searchString) {
        if (this.notes.isEmpty()) {
            return "No Notes";
        }
        String listByDescription = "";

        for (int i = 0; i < notes.size(); i++) {
            for (int j = 0; j < notes.get(i).getItems().size(); j++) {
                if (notes.get(i).findItem(j).getItemDescription().contains(searchString)) {
                    listByDescription = listByDescription + i + ": " + notes.get(i).findItem(j) + '\n';
                }
            }
        }

        if (listByDescription == "") {
            listByDescription = listByDescription + "No items found for: " + searchString;
        }

        return listByDescription;
    }


    public boolean isValidIndex(int index) {
        return index >= 0 && index < this.notes.size();
    }

    public void load() throws Exception {
        Class<?>[] classes = new Class[]{Note.class};
        XStream xstream = new XStream(new DomDriver());
        XStream.setupDefaultSecurity(xstream);
        xstream.allowTypes(classes);
        ObjectInputStream is = xstream.createObjectInputStream(new FileReader("notes.xml"));
        this.notes = (ArrayList) is.readObject();
        is.close();
    }


    public void save() throws Exception {
        XStream xstream = new XStream(new DomDriver());
        ObjectOutputStream out = xstream.createObjectOutputStream(new FileWriter("notes.xml"));
        out.writeObject(this.notes);
        out.close();
    }
}