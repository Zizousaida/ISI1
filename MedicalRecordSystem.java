import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

class MedicalRecord implements Serializable {
    private static final long serialVersionUID = 1L;
    private String patientName;
    private Date dateOfBirth;
    private String diagnosis;

    public MedicalRecord(String patientName, Date dateOfBirth, String diagnosis) {
        this.patientName = patientName;
        this.dateOfBirth = dateOfBirth;
        this.diagnosis = diagnosis;
    }

    public String getPatientName() {
        return patientName;
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return "Patient: " + patientName + 
               ", Date of Birth: " + sdf.format(dateOfBirth) + 
               ", Diagnosis: " + diagnosis;
    }
}

public class MedicalRecordSystem {
    private static ArrayList<MedicalRecord> records = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    public static void main(String[] args) {
        boolean running = true;

        while (running) {
            try {
                displayMenu();
                int choice = getIntInput("Enter your choice: ");
                
                switch (choice) {
                    case 1:
                        addMedicalRecord();
                        break;
                    case 2:
                        deleteMedicalRecord();
                        break;
                    case 3:
                        displayAllRecords();
                        break;
                    case 4:
                        saveRecordsToFile();
                        break;
                    case 5:
                        loadRecordsFromFile();
                        break;
                    case 6:
                        System.out.println("Exiting the program...");
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
            }
            
            // Add pause before redisplaying menu
            if (running) {
                System.out.println("\nPress Enter to continue...");
                scanner.nextLine();
            }
        }
        scanner.close();
    }

    private static void displayMenu() {
        System.out.println("\n===== MEDICAL RECORDS MANAGEMENT SYSTEM =====");
        System.out.println("1. Add a new medical record");
        System.out.println("2. Delete a medical record");
        System.out.println("3. Display all medical records");
        System.out.println("4. Save records to file");
        System.out.println("5. Load records from file");
        System.out.println("6. Exit");
    }

    private static void addMedicalRecord() {
        try {
            System.out.println("\n--- Add New Medical Record ---");
            String name = getStringInput("Enter patient name: ");
            
            Date dob = null;
            boolean validDate = false;
            while (!validDate) {
                try {
                    String dobString = getStringInput("Enter date of birth (dd/MM/yyyy): ");
                    dob = dateFormat.parse(dobString);
                    validDate = true;
                } catch (ParseException e) {
                    System.out.println("Invalid date format. Please use dd/MM/yyyy format.");
                }
            }
            
            String diagnosis = getStringInput("Enter diagnosis: ");
            
            records.add(new MedicalRecord(name, dob, diagnosis));
            System.out.println("Medical record added successfully!");
        } catch (Exception e) {
            System.out.println("Error adding record: " + e.getMessage());
        }
    }

    private static void deleteMedicalRecord() {
        if (records.isEmpty()) {
            System.out.println("No records to delete.");
            return;
        }
        
        System.out.println("\n--- Delete Medical Record ---");
        String name = getStringInput("Enter patient name to delete: ");
        
        boolean found = false;
        for (int i = 0; i < records.size(); i++) {
            if (records.get(i).getPatientName().equalsIgnoreCase(name)) {
                records.remove(i);
                System.out.println("Record deleted successfully!");
                found = true;
                break;
            }
        }
        
        if (!found) {
            System.out.println("No record found with that name.");
        }
    }

    private static void displayAllRecords() {
        if (records.isEmpty()) {
            System.out.println("No records to display.");
            return;
        }
        
        System.out.println("\n--- All Medical Records ---");
        for (int i = 0; i < records.size(); i++) {
            System.out.println((i + 1) + ". " + records.get(i));
        }
    }

    private static void saveRecordsToFile() {
        try {
            String filename = getStringInput("Enter filename to save: ");
            if (!filename.endsWith(".dat")) {
                filename += ".dat";
            }
            
            FileOutputStream fileOut = new FileOutputStream(filename);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(records);
            out.close();
            fileOut.close();
            System.out.println("Records saved successfully to " + filename);
        } catch (IOException e) {
            System.out.println("Error saving to file: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private static void loadRecordsFromFile() {
        try {
            String filename = getStringInput("Enter filename to load: ");
            if (!filename.endsWith(".dat")) {
                filename += ".dat";
            }
            
            FileInputStream fileIn = new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            records = (ArrayList<MedicalRecord>) in.readObject();
            in.close();
            fileIn.close();
            System.out.println("Records loaded successfully from " + filename);
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("Class not found: " + e.getMessage());
        }
    }

    private static String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private static int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
}
