package org.chocoserver;

import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {
    public static HashMap<String, Integer> commandMap = new HashMap<>();

    public static void main(String[] args) {
        File baseDir;
        if(args.length == 0 || !(baseDir  = new File(args[0])).exists()){
            System.out.println("폴더 경로가 입력되지 않았거나 잘못된 경로입니다.");
            return;
        }

        for (File logFile : baseDir.listFiles()) {
            if(!logFile.getName().contains(".log")) continue;

            try {
                FileReader fileReader = new FileReader(logFile);
                BufferedReader bufferedReader = new BufferedReader(fileReader);

                String line;
                while ((line = bufferedReader.readLine()) != null){
                    if(!line.contains("[로그]")) continue;
                    String command = "";

                    try {
                        command = line.split("§3: /")[1];
                    }catch (ArrayIndexOutOfBoundsException e){
                        System.out.println("Could not process : \" + " + line + '"');
                    }

                    commandMap.put(command, commandMap.getOrDefault(command, 0) + 1);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        List<String> keyList = new ArrayList<>(commandMap.keySet());
        keyList.sort((o1, o2) -> commandMap.get(o2).compareTo(commandMap.get(o1)));

        StringBuilder stringBuilder = new StringBuilder();
        for (String command : keyList) {
            int count = commandMap.get(command);
            if(count == 1) continue;

            stringBuilder.append('[').append(count).append(']').append(command).append('\n');
        }

        try {
            FileUtils.writeStringToFile(new File(baseDir.getAbsolutePath() + File.separator + "result.txt"), stringBuilder.toString(), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
