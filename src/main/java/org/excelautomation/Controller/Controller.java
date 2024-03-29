package org.excelautomation.Controller;

import org.apache.poi.ss.usermodel.Row;
import org.excelautomation.Model.ErrorType;
import org.excelautomation.Model.MessageType;
import org.excelautomation.Model.Model;
import org.excelautomation.View.View;

import java.util.concurrent.TimeUnit;

public class Controller {
    private final View display;
    private final Model model;
    private final ExceptionHandler exceptionHandler;

    public Controller(View view, String path) {
        this.display = view;
        this.model = new Model(path, this);
        exceptionHandler = new ExceptionHandler(this.display);
    }

    public void error(ErrorType ep, Exception e) {
        exceptionHandler.handleError(ep, e);
    }

    public void error(ErrorType ep, int rows) {
        exceptionHandler.handleError(ep, rows);
    }

    public void error(ErrorType ep, String fileName, Row row) {
        exceptionHandler.handleError(ep, fileName, row.getRowNum() + 1);
    }

    public void error(ErrorType ep) {
        exceptionHandler.handleError(ep);
    }

    public void init() {
        model.readFolder();
    }

    public void sendMessage(MessageType mp, String fileName, int count) {
        display.println("№" + count + ": " + fileName, mp);
    }

    public void sendMessage(MessageType mp, long start, long end) {
        switch (mp) {
            case READING_TIME -> display.println("ЧТЕНИЕ ЗАНЯЛО: " + getTime(start, end), MessageType.INFO);
            case WRITING_TIME -> display.println("ЗАПИСЬ ЗАНЯЛА: " + getTime(start, end), MessageType.INFO);
        }
    }

    private String getTime(long start, long end) {
        return String.format(": %02d:%02d:%02d%n",
                TimeUnit.MILLISECONDS.toHours(end - start),
                TimeUnit.MILLISECONDS.toMinutes(end - start) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(end - start)),
                TimeUnit.MILLISECONDS.toSeconds(end - start) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(end - start)));
    }
}
