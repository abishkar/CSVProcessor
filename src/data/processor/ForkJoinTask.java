package data.processor;

import data.generator.Employee;

import java.util.concurrent.RecursiveTask;

public  class ForkJoinTask extends RecursiveTask<Employee> {
    private String csvLine;
    public ForkJoinTask(String csvLine){
        this.csvLine=csvLine;
    }


    @Override
    protected Employee compute() {
        Employee employee = new Employee();

        String[]     arr = csvLine.split(",");
            if(!arr[0].contains("employeeID")) {
                String id = arr[0];
                String firstName = arr[1];
                String lastName = arr[2];
                String department = arr[3];
                String Salary = arr[4];
                employee.setSalary(Salary);
                employee.setFirstName(firstName);
                employee.setLastName(lastName);
                employee.setEmployeeID(id);
                employee.setDepartment(department);
                return employee;

            }
            return null;
}
}