package NotableDeveloper.rank.test.data;

import NotableDeveloper.rank.domain.dto.EvaluationDto;
import NotableDeveloper.rank.domain.enums.Semester;
import lombok.Getter;
import lombok.Setter;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class SampleCsvExtract {
    String filePath;


    public SampleCsvExtract(){
        filePath = System.getenv("CSV_PATH");
    }

    public ArrayList<EvaluationDto> extractEvaluation(){
        ArrayList<EvaluationDto> evaluations = new ArrayList<>();
        String line;

        int[] targetColumns = {2, 3, 5, 6, 7, 8, 9};

        try {
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream(filePath), StandardCharsets.UTF_8)
            );

            for(int i = 0; i < 100; i++){
                line = br.readLine();
                String[] columns = line.split(",");

                String code = columns[targetColumns[0]];
                String title = columns[targetColumns[1]];
                String professorName = columns[targetColumns[2]];
                String college = columns[targetColumns[3]];
                String department = columns[targetColumns[4]];
                String position = columns[targetColumns[5]];
                float rating = Float.parseFloat(columns[targetColumns[6]]);

                evaluations.add(new EvaluationDto(
                    2023,
                        Semester.FIRST,
                        code,
                        title,
                        professorName,
                        college,
                        department,
                        position,
                        rating
                ));
            }

        } catch (Exception e){
            e.printStackTrace();
        }

        return evaluations;
    }
}
