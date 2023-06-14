package oop.lesson_06;

import java.math.BigDecimal;
import java.math.RoundingMode;

final class Model {

    private final static int MAX_INPUT_DIGITS = 12;

    private final static int MAX_RESULT_DECIMALS = 5;

    private static double doTheMath(char op, double v1, double v2)
            throws ArithmeticException {
        double result = 0.0;

        switch (op) {
            case '+':
                result = v1 + v2;
                break;
            case '-':
                result = v1 - v2;
                break;
            case '×':
                result = v1 * v2;
                break;
            case '÷':
                if (v2 == 0.0) {
                    throw new ArithmeticException("Division by 0");
                }
                result = v1 / v2;
                break;
        }

        return round(result, MAX_RESULT_DECIMALS);
    }

    private static double round(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }

        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    private String resultDisplay;
    private String operationDisplay;
    private double tempValue;
    private boolean inErrorMode;
    private boolean firstDigit;

    public Model() {
        reset();
    }

    public String getResultDisplay() {
        return resultDisplay;
    }

    public String getOperationDisplay() {
        return operationDisplay;
    }

    public void insertNumber(int n) {
        if (inErrorMode) {
            return;
        }

        if (firstDigit) {
            resultDisplay = String.valueOf(n);
            firstDigit = false;
            return;
        }

        if (resultDisplay.length() >= MAX_INPUT_DIGITS) {
            return;
        }

        if (resultDisplay.equals("0")) {
            resultDisplay = String.valueOf(n);
            return;
        }
        if (resultDisplay.equals("-0")) {
            resultDisplay = "-" + n;
            return;
        }

        resultDisplay += n;
    }

    public void insertDot() {
        if (inErrorMode) {
            return;
        }

        if (firstDigit) {
            resultDisplay = "0.";
            firstDigit = false;
            return;
        }
        if (resultDisplay.contains("-")) {
            resultDisplay = "-0.";
            return;
        }

        if (resultDisplay.contains(".")) {
            return;
        }

        resultDisplay += ".";
    }

    public void switchSign() {
        if (inErrorMode) {
            return;
        }

        if (firstDigit && !operationDisplay.isEmpty()) {
            resultDisplay = "-0";
            firstDigit = false;
            return;
        }

        if (resultDisplay.charAt(0) == '-') {
            resultDisplay = resultDisplay.substring(1);
        } else {
            resultDisplay = "-" + resultDisplay;
        }

        if (firstDigit) {
            firstDigit = false;
        }
    }

    public void setOperation(char op) {
        calculate();

        if (inErrorMode) {
            return;
        }

        try {
            tempValue = Double.valueOf(resultDisplay);

            operationDisplay = String.valueOf(op);

            firstDigit = true;
        } catch (Exception e) {
            enterErrorMode();
        }
    }

    public void calculate() {
        if (inErrorMode) {
            return;
        }

        if (operationDisplay.isEmpty()) {
            return;
        }

        try {
            char op = operationDisplay.charAt(0);
            Double valueIndisplay = Double.valueOf(resultDisplay);

            Double result = doTheMath(op, tempValue, valueIndisplay);

            resultDisplay = result.toString();
            operationDisplay = "";

            firstDigit = true;
        } catch (NumberFormatException | ArithmeticException e) {
            enterErrorMode();
        }
    }

    public void clean() {
        if (inErrorMode) {
            return;
        }

        resultDisplay = "0";
        firstDigit = true;
    }

    public void reset() {
        tempValue = 0.0;

        resultDisplay = "0";
        firstDigit = true;
        inErrorMode = false;

        operationDisplay = "";
    }

    private void enterErrorMode() {
        inErrorMode = true;
        resultDisplay = "Error";
        operationDisplay = "";
    }
}
