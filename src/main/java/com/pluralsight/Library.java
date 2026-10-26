package com.pluralsight;
import java.util.*;

public class Library {
    public static void main(String[] args) {
        //Define library inventory
        Book[] inventory = populateInventory();

        //Menu enum
        enum MENU {
            MAIN,
            AVAIL,
            NOTAVAIL,
            CHECKOUT,
            CHECKIN
        };

        //Menu strings
        String mainMenu = """
                Welcome to the library! Please select an option:
                
                1 - Show available books
                2 - Show checked out books
                3 - Check out a book
                4 - Check in a book
                0 - EXIT
                """;
        String availableMenu = """
                Enter the corresponding index to check out that available book.
                Enter 0 to return to the Main Menu.
                """;
        String booksOutMenu = """
                Enter the corresponding index to check in the corresponding book.
                Enter 0 to return to the Main Menu.
                """;
        String checkOutMenu = """
                Enter a title or an ISBN number to search for a book to check out.
                Enter 0 to return to the Main Menu.
                """;
        String checkInMenu = """
                Enter the name of the person checking in a book. All books checked out to that name will be displayed.
                Enter 0 to return to the Main Menu.
                """;

        //Menu control variables
        Scanner s = new Scanner(System.in);
        MENU menuState = MENU.MAIN;
        boolean exitFlag = false;
        boolean firstRun = true;
        int command = 0;
        String userInput = "";

        //Program loop
        while (!exitFlag) {
            switch (menuState) {
                case MAIN -> {
                    userInput = getInputFromConsole(mainMenu, s, !firstRun);
                    command = !userInput.isEmpty() ? Integer.parseInt(userInput) : 0;
                    firstRun = false;
                    switch (command) {
                        case 1 -> menuState = MENU.AVAIL;
                        case 2 -> menuState = MENU.NOTAVAIL;
                        case 3 -> menuState = MENU.CHECKOUT;
                        case 4 -> menuState = MENU.CHECKIN;
                        default -> exitFlag = true;
                    }
                }
                case AVAIL -> {
                    List<Book> availableBooks = listBooksByStatus(inventory, false);
                    displayBookList(availableBooks);
                    userInput = getInputFromConsole(availableMenu, s, true);
                    command = !userInput.isEmpty() ? Integer.parseInt(userInput) : 0;
                    if (command > 0 && command <= availableBooks.size()) {
                        checkOutBook(availableBooks.get(command - 1), s);
                    } else {
                        System.out.println("Returning to main menu.");
                    }
                    menuState = MENU.MAIN;
                }
                case NOTAVAIL -> {
                    List<Book> checkedOutBooks = listBooksByStatus(inventory, true);
                    displayBookList(checkedOutBooks);
                    userInput = getInputFromConsole(booksOutMenu, s, true);
                    command = !userInput.isEmpty() ? Integer.parseInt(userInput) : 0;
                    if (command > 0 && command <= checkedOutBooks.size()) {
                        checkInBook(checkedOutBooks.get(command - 1));
                    } else {
                        System.out.println("Returning to main menu.");
                    }
                    menuState = MENU.MAIN;
                }
                case CHECKOUT -> {
                    userInput = getInputFromConsole(checkOutMenu, s, true);
                    Book searchResult = null;
                    if (Character.isDigit(userInput.charAt(0)) && (userInput.charAt(3) == '-')) {   //Parse for ISBN
                        for (Book book : inventory) {
                            if (book.getIsbn().equals(userInput))
                                searchResult = book;
                        }
                    } else {
                        for (Book book : inventory) {
                            if (book.getTitle().equalsIgnoreCase(userInput))
                                searchResult = book;
                        }
                    }
                    if (searchResult != null) {
                        if (!searchResult.isCheckedOut) {
                            checkOutBook(searchResult, s);
                        } else {
                            System.out.println("This book is already checked out. Returning to main menu.");
                        }
                    } else {
                        System.out.println("Unknown error. Returning to main menu.");
                    }
                    menuState = MENU.MAIN;
                }
                case CHECKIN -> {
                    userInput = getInputFromConsole(checkInMenu, s, true);
                    List <Book> checkedOutBooks = listBooksByStatus(inventory, userInput);
                    displayBookList(checkedOutBooks);
                    userInput = getInputFromConsole(booksOutMenu, s, true);
                    command = !userInput.isEmpty() ? Integer.parseInt(userInput) : 0;
                    if (command > 0 && command <= checkedOutBooks.size()) {
                        checkInBook(checkedOutBooks.get(command - 1));
                    } else {
                        System.out.println("Returning to main menu.");
                    }
                    menuState = MENU.MAIN;
                }
            }
            if (exitFlag) {
                System.out.println("Exiting library interface.");
                break;
            }

        }
        s.close();
    }

    public static String getInputFromConsole(String display, Scanner s, boolean clearBuffer) {
        if (clearBuffer) s.nextLine();
        System.out.println(display);
        return s.nextLine().trim();
    }

    public static List<Book> listBooksByStatus(Book[] inventory, boolean checkedOut) {
        List<Book> b = new ArrayList<Book>();
        for (Book book : inventory) {
            if (book.isCheckedOut == checkedOut) {
                b.add(book);
            }
        }
        return b;
    }

    public static List<Book> listBooksByStatus(Book[] inventory, String checkedOutTo) {
        List<Book> b = new ArrayList<Book>();
        for (Book book : inventory) {
            if (book.getCheckedOutTo().equalsIgnoreCase(checkedOutTo)) {
                b.add(book);
            }
        }
        return b;
    }

    public static void displayBookList(List<Book> bookList) {
        for (int i = 0; i < bookList.size(); i++) {
            System.out.println(Integer.toString(i+1) + ":\n" + bookList.get(i).retrieveInfo() + "\n");
        }
    }

    public static void checkOutBook(Book goingOut, Scanner s) {
        String userInput = getInputFromConsole("Enter the name of the person checking out this book.", s, true);
        goingOut.checkOut(userInput);
        System.out.println(goingOut.getTitle() + " checked out to " + goingOut.getCheckedOutTo() + ".");
    }

    public static void checkInBook(Book goingIn) {
        goingIn.checkIn();
        System.out.println(goingIn.getTitle() + " has been checked back in.");
    }

    public static Book[] populateInventory() {
        Book[] inventory = new Book[20];
        Book book0 = new Book(1, "978-0451524935", "1984");
        inventory[0] = book0;
        Book book1 = new Book(2, "978-0743273565", "The Great Gatsby");
        book1.checkOut("Alice");
        inventory[1] = book1;
        Book book2 = new Book(3, "978-0061120084", "To Kill a Mockingbird");
        inventory[2] = book2;
        Book book3 = new Book(4, "978-0451524942", "Animal Farm");
        inventory[3] = book3;
        Book book4 = new Book(5, "978-0141439518", "Pride and Prejudice");
        book4.checkOut("Bob");
        inventory[4] = book4;
        Book book5 = new Book(6, "978-0316769488", "The Catcher in the Rye");
        inventory[5] = book5;
        Book book6 = new Book(7, "978-0743673576", "A Tale of Two Cities");
        inventory[6] = book6;
        Book book7 = new Book(8, "978-0060935467", "The Lord of the Rings");
        book7.checkOut("Charlie");
        inventory[7] = book7;
        Book book8 = new Book(9, "978-0451584959", "The Hobbit");
        inventory[8] = book8;
        Book book9 = new Book(10, "978-0141439525", "Fahrenheit 451");
        inventory[9] = book9;
        Book book10 = new Book(11, "978-0316769495", "Jane Eyre");
        book10.checkOut("Diana");
        inventory[10] = book10;
        Book book11 = new Book(12, "978-0743273589", "Wuthering Heights");
        inventory[11] = book11;
        Book book12 = new Book(13, "978-0060935474", "Moby Dick");
        inventory[12] = book12;
        Book book13 = new Book(14, "978-0451524966", "War and Peace");
        book13.checkOut("Edward");
        inventory[13] = book13;
        Book book14 = new Book(15, "978-0141439532", "Hamlet");
        inventory[14] = book14;
        Book book15 = new Book(16, "978-0316769501", "The Odyssey");
        inventory[15] = book15;
        Book book16 = new Book(17, "978-0743167359", "King Lear");
        book16.checkOut("Fiona");
        inventory[16] = book16;
        Book book17 = new Book(18, "978-0060935481", "Macbeth");
        inventory[17] = book17;
        Book book18 = new Book(19, "978-0451518497", "Othello");
        inventory[18] = book18;
        Book book19 = new Book(20, "978-0141439549", "Romeo and Juliet");
        book19.checkOut("George");
        inventory[19] = book19;
        return inventory;
    }
}
