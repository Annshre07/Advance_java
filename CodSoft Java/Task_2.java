import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
// Interface for basic operations
interface MarksCalculator {
    int calculateTotalMarks(List<Integer> marks);
    double calculateAveragePercentage(int totalMarks, int subjectCount);
}
// Interface for grading system
interface GradeCalculator {
    String calculateGrade(double averagePercentage);
}
// Class for calculating marks
class MarksCalculationService implements MarksCalculator {
    @Override
     public int calculateTotalMarks(List<Integer> marks) {
        int total=0;
        // Summing  up all marks
        for(int i=0;i<marks.size();i++)
        {
            total=total+marks.get(i);
        }
        return(total);

    }
    @Override
    public double calculateAveragePercentage(int totalMarks, int subjectCount) {
        // Calculate average percentage
        return(totalMarks/subjectCount);
    }
}
// Class for calculating grade
class GradeCalculationService implements GradeCalculator {
    @Override
    public String calculateGrade(double averagePercentage) {
        // Assign grades based on percentage
        if(averagePercentage>=90.0)
            return"Outstanding";
            else if (averagePercentage>=80.00)
                return"Very good";
        
                else if (averagePercentage >= 60.0) 
                    return "Good";
                        else if (averagePercentage >= 40.0) 
                            return "Average";
                                        else 
                                            return "Fail";
    }
}

// Class to display results
class DisplayService {
    void displayResults(int totalMarks, double averagePercentage, String grade) {
        // Display results to the user
        System.out.println("Your total Marks is "+totalMarks+" Your Percentage is "+averagePercentage+" and Grade is "+grade);
    }
}
// Main class to orchestrate the flow
public class Task_2{
    public static void main(String[] args) {
        // Input marks from the user
           ArrayList<Integer> marks = new ArrayList<>();
           Scanner sc=new Scanner(System.in);
           System.out.println("Enter the marks: Enter -1 to stop entering:");
         while(true)
         {
            int mark=sc.nextInt();
            if(mark==-1)
             break;
            marks.add(mark);
         }
            int size=marks.size();
        // Call MarksCalculationService for total and average 
        MarksCalculationService calculate =new MarksCalculationService();
        int total =calculate.calculateTotalMarks(marks);
        double average=calculate.calculateAveragePercentage(total,size);
        // Call GradeCalculationService for grade
        GradeCalculationService grade =new GradeCalculationService();
        String gr=grade.calculateGrade(average);
        // Call DisplayService to show output
        DisplayService display=new DisplayService();
        display.displayResults(total,average,gr);
    }
}
