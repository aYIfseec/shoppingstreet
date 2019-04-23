package com.hyk.shoppingstreet.common.utils;

import com.google.common.collect.Lists;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class FileReadUtil {


    public static String getStringFromFile(String fileName) {
        StringBuilder res = new StringBuilder();
        try (
            FileReader reader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(reader)
        ) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                res.append(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res.toString();
    }


    public static List<String> getLineListFromFile(String fileName) {
        List<String> res = Lists.newArrayList();
        try (
            FileReader reader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(reader)
        ) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                res.add(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }
}
