package main;

import controllers.NoteAPI;
import models.Item;
import models.Note;
import utils.ScannerInput;
import utils.Utilities;

public class Driver {
    private NoteAPI noteAPI = new NoteAPI();

    public Driver(){
        this.runMenu();
    }

    public static void main(String[] args) {
        new Driver();
    }

    private int mainMenu(){
        return ScannerInput.readNextInt("""
                ------------------------------------------------------------------                           
                                             Shop Menu                            
                ------------------------------------------------------------------  
                 NOTE MENU                 
                    1) Add a Note                                          
                    2) List all notes (all, active, archived)                                        
                    3) Update a note                                          
                    4) Delete a note
                    5) Archive a note                                          
                 ------------------------------------------------------------------
                 ITEM MENU   
                    6) Add an item to a note                                 
                    7) Update item description on a note                         
                    8) Delete an item from a note                               
                    9) Mark item as complete/todo  
                 ------------------------------------------------------------------
                 REPORT MENU FOR NOTES
                    10) All notes and their items (active & archived)                            
                    11) Archive notes whose items are all complete                          
                    12) All notes within a selected Category
                    13) All notes within a selected Priority
                    14) Search for all notes (by note Title)                                                     
                 ------------------------------------------------------------------
                 REPORT MENU FOR ITEMS
                    15) All items that are todo (with note Title)
                    16) Overall number of items todo/complete
                    17) Todo/complete items by specific Category
                    18) Search for all items (by item description)
                 ------------------------------------------------------------------
                 SETTINGS MENU
                    20) Save
                    21) Load
                    0) Exit
                 ------------------------------------------------------------------
                 ==>>""");
    }

    private void runMenu(){
        for(int option = this.mainMenu(); option != 0; option = this.mainMenu()) {
            switch(option) {
                case 1: this.addNote();
                break;
                case 2: this.viewNote();
                break;
                case 3: this.updateNote();
                break;
                case 4: this.deleteNote();
                break;
                case 5: this.archiveNote();
                break;
                case 6: this.addItemToNote();
                break;
                case 7: this.updateItemDescInNote();
                break;
                case 8: this.deleteItemFromNote();
                break;
                case 9: this.markCompletionOfItem();
                break;
                case 10: this.printActiveAndArchivedReport();
                break;
                case 11: this.archiveNotesWithAllItemsComplete();
                break;
                case 12: this.printNotesBySelectedCategory();
                break;
                case 13: this.printNotesByPriority();
                break;
                case 14: this.searchNotesByTitle();
                break;
                case 15: this.printAllTodoItems();
                break;
                case 16: this.printOverallItemsTodoComplete();
                break;
                case 17: this.printItemCompletionStatusByCategory();
                break;
                case 18: this.searchItemsByDescription();
                break;
                case 20: this.save();
                break;
                case 21: this.load();
                break;
                default:
                    System.out.println("Invalid option entered: " + option);
            }

            ScannerInput.readNextLine("\nPress enter key to continue...");
        }

        System.out.println("Exiting...bye");
        System.exit(0);
    }

    private void addNote(){
        String noteTitle = ScannerInput.readNextLine("Enter the Note Title:  ");
        int notePriority = ScannerInput.readNextInt("Enter the Note Priority:  ");
        String noteCategory = ScannerInput.readNextLine("Enter the Note Category:  ");

        boolean isAdded = this.noteAPI.add(new Note(noteTitle, notePriority, noteCategory));
        if (isAdded) {
            System.out.println("Note Added Successfully");
        } else {
            System.out.println("No Note Added");
        }
    }

    private int listMenu(){
        return ScannerInput.readNextInt("""
                 ------------------------
                | 1) View ALL notes      |     
                | 2) View ACTIVE notes   |
                | 3) View ARCHIVED notes |
                 ------------------------
                 ===> """);
    }

    private void viewNote() {
        if(noteAPI.numberOfNotes() !=0) {
            for (int option = this.listMenu(); option != 0; this.listMenu()) {
                switch (option) {
                    case 1:
                        this.printAllNotes();
                        break;
                    case 2:
                        this.printActiveNotes();
                        break;
                    case 3:
                        this.printArchivedNotes();
                    default:
                        System.out.println("Invalid option entered: " + option);
                }
                ScannerInput.readNextLine("\nPress enter key to continue...");
                runMenu();
            }
        }
        else {
            System.out.println("No notes");
        }
    }

    private void updateNote(){
        this.printAllNotes();
        if (this.noteAPI.numberOfNotes() > 0) {
            int indexToUpdate = ScannerInput.readNextInt("Enter the index of the note to update ==> ");
            if (this.noteAPI.isValidIndex(indexToUpdate)) {
                String noteTitle = ScannerInput.readNextLine("Enter the Note Title:  ");
                int notePriority = ScannerInput.readNextInt("Enter the Note Priority:  ");
                String noteCategory = ScannerInput.readNextLine("Enter the Note Category:  ");

                if (this.noteAPI.updateNote(indexToUpdate, noteTitle, notePriority, noteCategory)) {
                    System.out.println("Update Successful");
                } else {
                    System.out.println("Update NOT Successful");
                }
            } else {
                System.out.println("There are no products for this index number");
            }
        }
    }

    private void deleteNote(){
        this.printAllNotes();
        if (this.noteAPI.numberOfNotes() > 0) {
            int indexToDelete = ScannerInput.readNextInt("Enter the index of the note to delete ==> ");
            Note noteToDelete = this.noteAPI.deleteNote(indexToDelete);
            if (noteToDelete != null) {
                System.out.println("Delete Successful! Deleted note: " + noteToDelete.getNoteTitle());
            } else {
                System.out.println("Delete NOT Successful");
            }
        }
    }

    private void archiveNote(){
        this.printAllNotes();
        if (this.noteAPI.numberOfNotes() > 0) {
            int indexToArchive = ScannerInput.readNextInt("Enter the index of the note to archive ==> ");
            if(noteAPI.isValidIndex(indexToArchive)) {
                this.noteAPI.archiveNote(indexToArchive);

                if (!this.noteAPI.archiveNote(indexToArchive)) {
                    System.out.println("Archive Successful! Archived note: " + noteAPI.findNote(indexToArchive).getNoteTitle());
                } else {
                    System.out.println("Archive NOT Successful");
                }
            }
            else{
                System.out.println("No note matching index");
            }
        }
    }

    private void addItemToNote(){
        this.printAllNotes();
        if (this.noteAPI.numberOfNotes() > 0) {
            int indexToAddTo = ScannerInput.readNextInt("Enter the index of the note to add item ==> ");

            if(noteAPI.isValidIndex(indexToAddTo)) {
                String itemDescription = ScannerInput.readNextLine("Enter the Item Description:  ");
                char notePriority = ScannerInput.readNextChar("Is item Completed? (y/n):  ");
                boolean itemCompletion = Utilities.YNtoBoolean(notePriority);
                boolean isAdded = this.noteAPI.findNote(indexToAddTo).addItem(new Item(itemDescription, itemCompletion));


                if (isAdded) {
                    System.out.println("Add Successful! Added to note: " + noteAPI.findNote(indexToAddTo).getNoteTitle());
                } else {
                    System.out.println("Add NOT Successful");
                }
            }
            else{
                System.out.println("No note matching index");
            }
        }
    }

    private void updateItemDescInNote() {
        this.printAllNotes();
        if (this.noteAPI.numberOfNotes() > 0) {
            int indexOfNote = ScannerInput.readNextInt("Enter the index of the note to update item description ==> ");
            if (this.noteAPI.numberOfItems() > 0) {
                System.out.println(this.noteAPI.findNote(indexOfNote).listItems());
                int indexOfItem = ScannerInput.readNextInt("Enter the index of the item to update description ==> ");
                String itemDesc = ScannerInput.readNextLine("Enter new Item Description ==> ");
                this.noteAPI.findNote(indexOfNote).findItem(indexOfItem).setItemDescription(itemDesc);
            }
            if(this.noteAPI.findNote(indexOfNote).numberOfItems() == 0){
                System.out.println("No items in Note");
            }
        }
    }

    private void deleteItemFromNote(){
        this.printAllNotes();
        if (this.noteAPI.numberOfNotes() > 0) {
            int indexOfNote = ScannerInput.readNextInt("Enter the index of the note to delete item ==> ");
            if (this.noteAPI.numberOfItems() > 0) {
                System.out.println(this.noteAPI.findNote(indexOfNote).listItems());
                int indexOfItem = ScannerInput.readNextInt("Enter the index of the item to delete ==> ");
                Item itemDeleted = this.noteAPI.findNote(indexOfNote).deleteItem(indexOfItem);
                if(itemDeleted != null){
                    System.out.println("Delete Successful! Deleted item: " + itemDeleted.getItemDescription());
                } else {
                    System.out.println("Delete NOT Successful");
                }
            }
            if(this.noteAPI.findNote(indexOfNote).numberOfItems() == 0){
                System.out.println("No items in Note");
            }
        }
    }

    private void markCompletionOfItem(){
        this.printAllNotes();
        if (this.noteAPI.numberOfNotes() > 0) {
            int indexOfNote = ScannerInput.readNextInt("Enter the index of the note to update item completion status ==> ");
            if (this.noteAPI.numberOfItems() > 0) {
                System.out.println(this.noteAPI.findNote(indexOfNote).listItems());
                int indexOfItem = ScannerInput.readNextInt("Enter the index of the item to update completion status ==> ");
                char notePriority = ScannerInput.readNextChar("Is item Completed? (y/n):  ");
                boolean itemCompletion = Utilities.YNtoBoolean(notePriority);
                Item item = this.noteAPI.findNote(indexOfNote).findItem(indexOfItem);
                this.noteAPI.findNote(indexOfNote).findItem(indexOfItem).setItemCompleted(itemCompletion);
                System.out.println("Update Successful! Updated item: " + item + '\n');
            }
            if(this.noteAPI.findNote(indexOfNote).numberOfItems() == 0){
                System.out.println("No items in Note");
            }
        }
    }

    private void printAllNotes(){
        System.out.println(noteAPI.numberOfNotes() + " active and archived note(s):" + '\n');
        System.out.println(this.noteAPI.listAllNotes());
    }

    private void printArchivedNotes(){
        System.out.println(noteAPI.numberOfArchivedNotes() + " archived note(s):" + '\n');
        System.out.println(this.noteAPI.listArchivedNotes());
    }

    private void printActiveNotes(){
        System.out.println(noteAPI.numberOfActiveNotes() + " active note(s):" + '\n');
        System.out.println(this.noteAPI.listActiveNotes());
    }

    private void printActiveAndArchivedReport(){
        printActiveNotes();
        System.out.println('\n' + "------------------------------------------------------" + '\n');
        printArchivedNotes();
        System.out.println('\n' + "------------------------------------------------------" + '\n');
    }

    private void archiveNotesWithAllItemsComplete(){
        noteAPI.archiveNotesWithAllItemsComplete();
    }

    private void printNotesBySelectedCategory(){
        if(noteAPI.numberOfNotes() == 0){
            System.out.println("No Notes");
        }
        if(noteAPI.numberOfNotes() > 0){
            String category = ScannerInput.readNextLine("Enter Category ==> ");
            System.out.println(noteAPI.listNotesBySelectedCategory(category));

        }
    }

    private void printNotesByPriority(){
        if(noteAPI.numberOfNotes() == 0){
            System.out.println("No Notes");
        }
        if(noteAPI.numberOfNotes() > 0){
            int priority = ScannerInput.readNextInt("Enter Priority ==> ");
            System.out.println(noteAPI.listNotesBySelectedPriority(priority));
        }
    }

    private void searchNotesByTitle(){
        if(noteAPI.numberOfNotes() == 0){
            System.out.println("No Notes");
        }
        if(noteAPI.numberOfNotes() > 0){
            String title = ScannerInput.readNextLine("Enter Title ==> ");
            System.out.println(noteAPI.searchNotesByTitle(title));
        }
    }

    private void printAllTodoItems(){
        System.out.println(noteAPI.numberOfTodoItems() + " TODO item(s):" + '\n');
        System.out.println(noteAPI.listTodoItems());
    }

    private void printOverallItemsTodoComplete(){
        if(noteAPI.numberOfItems() == 0){
            System.out.println("No Items Stored");
        }
        if(noteAPI.numberOfItems() > 0){
            System.out.println( "Completed item(s):" + noteAPI.numberOfCompleteItems());
            System.out.println( "------------------------------------------------------");
            System.out.println( "TODO item(s): " + noteAPI.numberOfTodoItems() );
        }
    }

    private void printItemCompletionStatusByCategory(){
        if(noteAPI.numberOfNotes() == 0){
            System.out.println("No Notes");
        }
        if(noteAPI.numberOfNotes() > 0){
            String category = ScannerInput.readNextLine("Enter Category ==> ");
            System.out.println(noteAPI.listItemStatusByCategory(category));

        }
    }

    private void searchItemsByDescription(){

    }

    private void save(){
        try {
            this.noteAPI.save();
        } catch (Exception var2) {
            System.err.println("Error writing to file: " + var2);
        }
    }

    private void load(){
        try {
            this.noteAPI.load();
        } catch (Exception var2) {
            System.err.println("Error reading from file: " + var2);
        }
    }
}