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
        post( "/delete", ZadanieCars::delete );
        post( "/update", ZadanieCars::updateFunction );
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

    private static String delete( Request req, Response res )
    {
        Gson gson = new Gson();
        String toDel = gson.fromJson( req.body(), String.class );
        System.out.println( toDel );
        res.type( "application/json" );
        for( int i = 0; i < cars.size(); i++ )
        {
            if( Objects.equals( cars.get( i ).uuid, toDel ) )
            {
                cars.remove( i );
                return gson.toJson( true, boolean.class );
            }
        }
        return gson.toJson( false, boolean.class );
    }

    private static String updateFunction( Request req, Response res )
    {
        Gson gson = new Gson();
        Car car = gson.fromJson( req.body(), Car.class );
        for( int i = 0; i < cars.size(); i++ )
        {
            if( Objects.equals( cars.get( i ).uuid, car.uuid ) )
            {
                cars.get( i ).model = car.model;
                cars.get( i ).year = car.year;
                return gson.toJson( true, boolean.class );
            }
        }
        return gson.toJson( false, boolean.class );
    }


    private static int newId()
    {
        return cars.size() > 0 ? Collections.max( cars ).id + 1 : 1;
    }
}
