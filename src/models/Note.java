package models;

import java.util.ArrayList;
import java.util.Objects;

import static utils.Utilities.*;
import static utils.CategoryUtility.*;

public class Note {
    private String noteTitle = "No Title";
    private int notePriority = 1 ;
    private String noteCategory = "";
    private boolean isNoteArchived = false;
    private ArrayList<Item> items = new ArrayList<>();

    public Note(String noteTitle, int notePriority, String noteCategory) {
        this.noteTitle = truncateString(noteTitle,20);
        setNotePriority(notePriority);
        setNoteCategory(noteCategory);
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        if(validateStringLength(noteTitle,20)){
            this.noteTitle = noteTitle;
        }
    }

    public int getNotePriority() {
        return notePriority;
    }

    public void setNotePriority(int notePriority) {
        if(validRange(notePriority,1,5)){
            this.notePriority = notePriority;
        }
    }

    public String getNoteCategory() {
        return noteCategory;
    }

    public void setNoteCategory(String noteCategory) {
        if(isValidCategory(noteCategory)) {
            this.noteCategory = noteCategory;
        }
    }

    public boolean isNoteArchived() {
        return isNoteArchived;
    }

    public void setNoteArchived(boolean noteArchived) {
        isNoteArchived = noteArchived;

    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    public int numberOfItems(){
        return items.size();
    }

    public boolean checkNoteCompletionStatus() {
        for (int i = 0; i < items.size(); ++i) {
            if(!items.get(i).isItemCompleted()){
                return false;
            }
        }
        return true;
    }

    public int numberOfCompletedItems(){
        int complete = 0;
        for(Item item: items) {
            if(item.isItemCompleted()){
                complete ++;
            }
        }
        return complete;
    }

    public int numberOfTODOItem(){
        int todo = 0;
        for(Item item: items) {
            if(!item.isItemCompleted()){
                todo ++;
            }
        }
        return todo;
    }

    public boolean addItem(Item item){ return this.items.add(item);}

    public String listItems(){
        if (this.items.isEmpty()) {
            return "No items added";
        } else {
            String listItems = "";

            for(int i = 0; i < this.items.size(); ++i) {
                listItems = listItems + i + ": " + items.get(i) + "\n";
            }

            return listItems;
        }
    }

    public String listTODO(){
        String listTODO = "";
        for(int i = 0; i < this.items.size(); i++){
            if(!items.get(i).isItemCompleted()) {
                listTODO = listTODO + i + ": " + items.get(i) + "\n";
            }
        }
        return listTODO;
    }

    public String listComplete(){
        String listComplete = "";
        for(int i = 0; i < this.items.size(); i++){
            if(items.get(i).isItemCompleted()) {
                listComplete = listComplete + i + ": " + items.get(i) + "\n";
            }
        }
        return listComplete;
    }

    public boolean isValidIndex(int index) {
        return index >= 0 && index < this.items.size();
    }

    public Item findItem(int index){
        return this.isValidIndex(index) ? (Item)this.items.get(index) : null;
    }

    public Item deleteItem(int indexToDelete){
        return this.isValidIndex(indexToDelete) ? (Item)this.items.remove(indexToDelete) : null;
    }

    public boolean updateItem(int index, String itemDescription, boolean itemCompleted) {
        Item foundItem = this.findItem(index);
        if (foundItem != null) {
            foundItem.setItemDescription(itemDescription);
            foundItem.setItemCompleted(itemCompleted);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Note note = (Note) o;
        return notePriority == note.notePriority
                && isNoteArchived
                == note.isNoteArchived
                && Objects.equals(noteTitle, note.noteTitle)
                && Objects.equals(noteCategory, note.noteCategory)
                && Objects.equals(items, note.items);
    }

    public String toString() {
        return noteTitle + ", Priority = " + notePriority + ", Category = " + noteCategory + ", Archived=" + booleanToYN(isNoteArchived)
                + '\n' + items + '\n' ;

    }
}
