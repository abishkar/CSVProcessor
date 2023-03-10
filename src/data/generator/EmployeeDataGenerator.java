package data.generator;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;
import java.util.stream.IntStream;

public class EmployeeDataGenerator {

    static Logger logger = Logger.getLogger(EmployeeDataGenerator.class.getName());



    public static void executeDataEmployeeGeneration() throws IOException {
      Files.deleteIfExists(Paths.get("employee.csv"));
      Files.deleteIfExists(Paths.get("report.csv"));




        List<String> departments = generateDeparmentData();

        List<Employee> employeesList=new ArrayList<>();

        List<List<String>> employeeRows=new ArrayList<List<String>>();

        //The dataset should be ~1 million records
        IntStream.range(0,1100000).parallel().forEach(
                salary -> {
                    //The salary range should be randomly distributed from $ 10k to $500k
                    int salaryDATA=generateSalary(10000,500000);

                    String salaryData=String.valueOf(salaryDATA);
                    int employeeID=new SecureRandom().nextInt(1000000-1);
                    String employeeId=String.valueOf(employeeID);


                    String firstName=generateFirstName();


                    String lastName=generateLastName();


                    Random rand = new Random();
                    String department = departments.get(rand.nextInt(departments.size()));

                    Employee employee=new Employee();
                    employee.setEmployeeID(employeeId);
                    employee.setDepartment(department);
                    employee.setFirstName(firstName);
                    employee.setLastName(lastName);
                    employee.setSalary(salaryData);
                    employeesList.add(employee);

                    String employeeData= employee.toString();
                    String[]    employeeArray = employeeData.split(" ");
                    List rowList= Arrays.asList(employeeArray);
                    employeeRows.add(rowList);
                }
        );

        logger.info("Dats of Size "+ employeeRows.size() + " Successfully Generated");
        generateCSVData(employeeRows);
    }



    private static void generateCSVData(List<List<String>> rows) throws IOException {
        FileWriter csvWriter = new FileWriter("employee.csv");
        csvWriter.append("employeeID");
        csvWriter.append(",");
        csvWriter.append("firstName");
        csvWriter.append(",");
        csvWriter.append("lastName");
        csvWriter.append(",");
        csvWriter.append("department");
        csvWriter.append(",");
        csvWriter.append("Salary");
        csvWriter.append("\n");

        for (List<String> rowData : rows) {
            csvWriter.append(String.join(",", rowData));
            csvWriter.append("\n");
        }

        csvWriter.flush();
        csvWriter.close();

        logger.info("Dats of Size "+ rows.size() + " Successfully Data written to CSV file");
    }

    private static List<String> generateDeparmentData() {
        List<String> departments=new ArrayList<>();
        //The department should be ~ 1000 different types
        IntStream.range(0,999).forEach(
                department -> {
                    departments.add(generateDepartment());

                }
        );
        return departments;
    }


    public static int generateSalary(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    static String generateDepartment(){

        int leftLimit = 97;
        int rightLimit = 122;
        int targetStringLength = 10;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        return generatedString;
    }

    static String generateFirstName(){
        int leftLimit = 97;
        int rightLimit = 122;
        int targetStringLength = 10;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        return generatedString;

    }


    static String generateLastName(){
        int leftLimit = 97;
        int rightLimit = 122;
        int targetStringLength = 10;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        return generatedString;

    }


}