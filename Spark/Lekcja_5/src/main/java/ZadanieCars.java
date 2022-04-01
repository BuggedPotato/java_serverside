import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.RandomBasedGenerator;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import spark.Request;
import spark.Response;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.UUID;

import static spark.Spark.*;

public class ZadanieCars {
    public static ArrayList<Car> cars = new ArrayList<>();
    private static RandomBasedGenerator uuid = Generators.randomBasedGenerator();
    public static void main( String[] args )
    {
        staticFiles.location( "/public" );
        post( "/add", ZadanieCars::addFunction );
        post( "/json", ZadanieCars::jsonFunction );
        get( "/delete/:id", ZadanieCars::delete );
        get( "/update/:id", ZadanieCars::update );
    }

    private static String addFunction( Request req, Response res )
    {
        Gson gson = new Gson();
        Car car = gson.fromJson( req.body(), Car.class );
        car.id = newId();
        car.uuid = uuid.generate().toString();
        cars.add( car );

        return gson.toJson( car, Car.class );
    }

    private static String jsonFunction( Request req, Response res )
    {
        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<Car>>(){}.getType();
        res.type( "application/json" );
        return gson.toJson( cars, listType );
    }

    private static String htmlFunction( Request req, Response res )
    {
        String foo = """
                <table>
                    <thead>
                        <tr>
                            <td>id</id>
                            <td>model</id>
                            <td>damaged</id>
                            <td>doors</id>
                            <td>country</id>
                        </tr>
                    </thead>
                    <tbody>
                    """;
//        for( Car car : cars )
//        {
//            String row = "<tr>";
//            row += "<td>" + car.id + "</td>";
//            row += "<td>" + car.model + "</td>";
//            row += "<td>" + car.damaged + "</td>";
//            row += "<td>" + car.doors + "</td>";
//            row += "<td>" + car.country + "</td>";
//            row += "</tr>";
//            foo += row;
//        }
//        foo += "</tbody></table>";
//        res.type( "text/html" );
        return foo;
    }

    private static boolean deleteAll( Request req, Response res )
    {
        cars.clear();
        return true;
    }

    private static boolean delete( Request req, Response res )
    {
        for( int i = 0; i < cars.size(); i++ )
        {
            if( cars.get(i).id == Integer.parseInt( req.params( "id" ) ) )
            {
                cars.remove( i );
                return true;
            }
        }
        return false;
    }

    private static String update( Request req, Response res )
    {

        return "not found";
    }


    private static int newId()
    {
        return cars.size() > 0 ? Collections.max( cars ).id + 1 : 1;
    }
}
