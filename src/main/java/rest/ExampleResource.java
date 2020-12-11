package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.*;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import dto.StudentDTO;
import fetchers.ExampleFetcher;
import fetchers.StudentFetcher;

@Path("example")
public class ExampleResource {

    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static ExecutorService es = Executors.newCachedThreadPool();


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"user", "admin"})
    public String getData() throws IOException, InterruptedException, ExecutionException, TimeoutException {

        String data = ExampleFetcher.fetchData(es,gson);

        return data;
    }

    @GET
    @Path("/students")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"user", "admin"})
    public String getStudents() throws InterruptedException, ExecutionException, TimeoutException, IOException {

        List<StudentDTO> students = StudentFetcher.fetchData(es,gson);

        return gson.toJson(students);
    }

}
