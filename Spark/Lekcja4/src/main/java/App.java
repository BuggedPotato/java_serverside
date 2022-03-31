import com.google.gson.Gson;
import spark.Request;
import spark.Response;

import java.util.ArrayList;

import static spark.Spark.*;

public class App {
    public static void main( String[] args )
    {
        get( "/test", ( req, res ) -> testFunction( req, res ) );
        get( "/info", ( req, res ) -> infoFunction( req, res ) );
        get( "/gson", ( req, res ) -> gsonFunction( req, res ) );
    }

    public static String testFunction( Request req, Response res )
    {
        String test = "response from server";
        res.type( "application/json" );
        return test;
    }

    public static String infoFunction( Request req, Response res )
    {
        System.out.println(res.status());
        System.out.println(req.requestMethod());
        System.out.println(req.attributes());
        System.out.println(req.cookies());
        System.out.println(req.params()); // potrzebne dziś
        System.out.println(req.uri());
        System.out.println(req.url());
        System.out.println(req.queryParams()); // potrzebne dziś
        System.out.println(req.queryParams("x")); // potrzebne dziś
        System.out.println(req.pathInfo());
        System.out.println(req.contentLength());
        System.out.println(req.contentType());
        System.out.println(req.protocol());
        System.out.println(req.headers());
        return "dadad";
    }

    public static String gsonFunction( Request req, Response res )
    {
        ArrayList<String> list = new ArrayList<>(){
            {
                add( "Reshala" );
                add( "Killa" );
                add( "Tagilla" );
                add( "Glukhar" );
                add( "Shturman" );
                add( "Sanitar" );
            }
        };
        Gson gson = new Gson();
        return gson.toJson( list, ArrayList.class );

//          dla zaawansowanych typów
//        Type listType = new TypeToken<ArrayList<String>>() {}.getType();
//        Gson gson = new Gson();
//        return gson.toJson(list, listType );
    }
}
