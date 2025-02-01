package com.example.test.service;

import org.apache.commons.io.input.ReversedLinesFileReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Component
public class FileUpdateListener {

    private RandomAccessFile randomAccessFile = null;
    private long offset;
    public Queue<String> fileLinesQueue = new LinkedList<>();

    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    @PostConstruct
    public void FileUpdateListenerPre() throws Exception {
        randomAccessFile = new RandomAccessFile("logFile", "r");
//        offset = setInitialOffset();
        readFileFromEnd();
        offset = setInitialOffset2();
    }

    private long setInitialOffset1() throws IOException {
        String line;
        while ((line = randomAccessFile.readLine()) != null) {
            addToQueueMaintainingSize(line);
        }
        return randomAccessFile.getFilePointer();
    }

    private long setInitialOffset2() throws IOException {
        return randomAccessFile.length();
    }

    private void readFileFromEnd() throws Exception{
        File file = new File("logFile");
        try (ReversedLinesFileReader rlfReader = new ReversedLinesFileReader(file, StandardCharsets.UTF_8)) {
            List<String> lastLines = new ArrayList<>();

            String line = "";
            while ((line = rlfReader.readLine()) != null && lastLines.size() < 10) {
                lastLines.add(line);
            }

            Collections.reverse(lastLines);
            for(String s : lastLines){
                addToQueueMaintainingSize(s);
            }
        }
    }

    private void addToQueueMaintainingSize(String line) {
        fileLinesQueue.add(line);
        if(fileLinesQueue.size() > 10) {
            fileLinesQueue.remove();
        }
    }

    @Scheduled(fixedDelay = 1000, initialDelay = 5000)
    public void checkForFileUpdates() throws Exception{
        long fileLength = randomAccessFile.length();
        randomAccessFile.seek(offset);

        while (randomAccessFile.getFilePointer() < fileLength) {
            String latestFileData = randomAccessFile.readLine();
            addToQueueMaintainingSize(latestFileData);
            simpMessagingTemplate.convertAndSend("/topic/fileUpdates", latestFileData);
        }
        offset = fileLength;
    }
}
