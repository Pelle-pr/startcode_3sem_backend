package fetchers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import dto.*;
import utils.HttpUtils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.*;

public class StudentFetcher {


    private static String studenURL = "http://hp-api.herokuapp.com/api/characters/students";


    public static List<StudentDTO> fetchData(ExecutorService threadPool, Gson gson) throws InterruptedException, ExecutionException, TimeoutException, IOException {

        String students = HttpUtils.fetchData(studenURL);

        Type listType = new TypeToken<List<StudentDTO>>() {}.getType();

        List<StudentDTO> studentDTOS = gson.fromJson(students, listType);


        return studentDTOS;

    }
}
