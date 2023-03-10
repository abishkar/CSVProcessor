package data.processor;

import data.generator.Employee;
import data.generator.EmployeeDataGenerator;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class CSVParser {
    static Logger logger = Logger.getLogger(EmployeeDataGenerator.class.getName());


    public static void main(String[] args) throws IOException {
        EmployeeDataGenerator.executeDataEmployeeGeneration();
        logger.info("Data Processing started Concurrently");

        List<Employee> employeeArrayList = extractData();

        logger.info("Count Employee By SalaryGreaterThan 50000");
        long totalEmployeeCountWithSalaryGreaterThan=  countEmployeeBySalaryGreaterThan(50000,employeeArrayList);

        logger.info("Employee By SalaryGreaterThan 50000 is " + totalEmployeeCountWithSalaryGreaterThan);

        logger.info(" find Average Salary By Department ");

        Map<String,Double> salaryMap= findAverageSalaryByDepartment(employeeArrayList);

        logger.info(" Average Salary By Department is " + salaryMap);


        generateSalaryReport(salaryMap);
        logger.info("Salary Report By Department Successfully Generated");



    }

    private static List<Employee> extractData() throws IOException {
        List<String> csvFileLines = Files.lines(Paths.get("employee.csv")).collect(Collectors.toList());

        List<ForkJoinTask> actions = csvFileLines.stream().map(lines -> {
            ForkJoinTask action = new ForkJoinTask(lines);
            return action;

        }).collect(Collectors.toList());


        List<Employee> employeeArrayList = ForkJoinTask.invokeAll(actions)
                .stream().map(ForkJoinTask::join)
                .filter(emp -> Objects.nonNull(emp))
                .collect(Collectors.toList());
        return employeeArrayList;
    }


    static long countEmployeeBySalaryGreaterThan(Integer salary,List<Employee> employeeArrayList) throws IOException {
        Long totalEmployees = employeeArrayList.stream().filter(d -> {
            int employeeSalary = Integer.parseInt(d.getSalary());
            return employeeSalary > salary;
        }).collect(Collectors.counting());

        return totalEmployees;
    }

    static Map<String,Double> findAverageSalaryByDepartment(List<Employee> employeeArrayList){
        Map<String,Double>  salaryByDepartment =  employeeArrayList.stream().collect
                (Collectors.groupingBy(Employee::getDepartment,Collectors.summingDouble(d->{
                    int employeeSalary = Integer.parseInt(d.getSalary());
                   return employeeSalary;
                })));

        return salaryByDepartment;

    }


    static void generateSalaryReport(Map<String, Double> salaryMap) throws IOException {

        List<List<String>> rows=new ArrayList<List<String>>();


        salaryMap.forEach((k,v)->{

            String salaryData=String.valueOf(v);

            List list= Arrays.asList(k,salaryData);
            rows.add(list);
        });


        FileWriter csvWriter = new FileWriter("report.csv");
        csvWriter.append("Department");
        csvWriter.append(",");
        csvWriter.append("Salary");
        csvWriter.append("\n");

        for (List<String> rowData : rows) {
            csvWriter.append(String.join(",", rowData));
            csvWriter.append("\n");
        }

        csvWriter.flush();
        csvWriter.close();


    }
}