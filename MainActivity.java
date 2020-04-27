package com.example.userr.rts_lab32_android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView resultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText deadlineEditText = (EditText) findViewById(R.id.deadLineEditText);
        final EditText iterationsCountEditText = (EditText) findViewById(R.id.iterationsCountEditText);
        final EditText learningSpeedTextEdit = (EditText) findViewById(R.id.learningSpeedTextEdit);
        Button calcButton = (Button) findViewById(R.id.calculateButton1);
        resultTextView = (TextView) findViewById(R.id.resultTextView);

        calcButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int iterationsNum = Integer.parseInt(iterationsCountEditText.getText().toString());
                    double learningSpeed = Double.parseDouble(learningSpeedTextEdit.getText().toString());
                    long timeDeadline = (long) (Double.parseDouble(deadlineEditText.getText().toString())*1000000000);
                    train(learningSpeed, iterationsNum, timeDeadline);

                } catch (NumberFormatException e) {
                    resultTextView.setText("Введені неправильні дані!");
                }
            }
        });
    }

    private void train(double learningSpeed, int iterationsNum, long deadline) {
        int[][] points =  {{0, 6}, {1, 5}, {3, 3}, {2, 4}};
        double w1=0.0, w2=0.0, P=4.0,  y, delta;
        int iterations = 0 ;
        boolean done = false;
        long start = System.nanoTime();

        int index = 0;
        while (iterations++ < iterationsNum && (System.nanoTime() - start) < deadline) {

            index %= 4;
            y = points[index][0] * w1 + points[index][1] * w2;

            if (P < points[0][0] * w1 + points[0][1] * w2
                    && P < points[1][0] * w1 + points[1][1] * w2
                    && P > points[2][0] * w1 + points[2][1] * w2
                    && P > points[3][0] * w1 + points[3][1] * w2) {
                done = true;
                break;
            }

            delta = P - y;
            w1 += delta * points[index][0] * learningSpeed;
            w2 += delta * points[index][1] * learningSpeed;
            index++;
        }

        if (done) {
            long execTimeMcs = (System.nanoTime() - start) / 1000;
            resultTextView.setText(
                    String.format(
                            "Навчання завершено успішно!\n" +
                                    "w1 = %6.4f w2 = %6.4f\n" +
                                    "Час виконання: %d мкс\n"+
                                    "к-сть ітерацій: %d", w1, w2, execTimeMcs, iterations));

        } else {
            String reason = "Помилка!";
            if (iterations >= iterationsNum) {
                reason += "\nНе достатньо ітрацій!";
            } else {
                reason += "\nНе достатньо часу!";
            }
            resultTextView.setText(reason);
        }
    }
}