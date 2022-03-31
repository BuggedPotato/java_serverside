import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import spark.Request;
import spark.Response;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

import static spark.Spark.*;

public class ZadanieCars {
    public static ArrayList<Car> cars = new ArrayList<>(){
        {
            add( new Car( 1, "Opel", false, 5, "PL" ) );
            add( new Car( 2, "Ford", true, 5, "ENG" ) );
            add( new Car( 3, "Dacia", false, 5, "PL" ) );
        }
    };
    public static void main( String[] args )
    {
        staticFiles.location( "/public" );
        get( "/add", ZadanieCars::addFunction );
        get( "/json", ( req, res ) -> jsonFunction( req, res, false ) );
        get( "/text", ( req, res ) -> jsonFunction( req, res, true ) );
        get( "/html", ZadanieCars::htmlFunction );
        get( "/deleteAll", ZadanieCars::deleteAll );
        get( "/delete/:id", ZadanieCars::delete );
        get( "/update/:id", ZadanieCars::update );
    }

    private static String addFunction( Request req, Response res )
    {
        String model = req.queryParams( "model" );
        int doors = Integer.parseInt( req.queryParams( "doors" ) );
        boolean damaged = Objects.equals( req.queryParams( "damaged" ), "on" );
        String country = req.queryParams( "country" );

        Car car = new Car( newId(), model, damaged, doors, country );
        cars.add( car );
        return "Car added to list, current size = " + cars.size();
    }

    private static String jsonFunction( Request req, Response res, boolean asText )
    {
        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<Car>>(){}.getType();
        if( asText )
            res.type( "text/plain" );
        else
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
        for( Car car : cars )
        {
            String row = "<tr>";
            row += "<td>" + car.id + "</td>";
            row += "<td>" + car.model + "</td>";
            row += "<td>" + car.damaged + "</td>";
            row += "<td>" + car.doors + "</td>";
            row += "<td>" + car.country + "</td>";
            row += "</tr>";
            foo += row;
        }
        foo += "</tbody></table>";
        res.type( "text/html" );
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
        if( req.queryParams( "model" ) == null || req.queryParams( "doors" ) == null ||
                req.queryParams( "damaged" ) == null || req.queryParams( "country" ) == null )
            return "not enough new data";

        int id = Integer.parseInt( req.params( "id" ) );
        String model = req.queryParams( "model" );
        int doors = Integer.parseInt( req.queryParams( "doors" ) );
        boolean damaged = Objects.equals( req.queryParams( "damaged" ), "on" );
        String country = req.queryParams( "country" );

        for( int i = 0; i < cars.size(); i++ )
        {
            if( cars.get(i).id == id )
            {
                Car newCar = new Car( id, model, damaged, doors, country );
                cars.set( i, newCar );
                return "updated";
            }
        }
        return "not found";
    }


    private static int newId()
    {
        return Collections.max( cars ).id + 1;
    }
}
