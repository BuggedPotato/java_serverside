import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.RandomBasedGenerator;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import spark.Request;
import spark.Response;

import javax.print.Doc;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Stream;

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

        post( "/generate", ZadanieCars::generateCars );
        post( "/invoice", ZadanieCars::generateInvoice );
        get( "/invoices", ZadanieCars::getInvoice );
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

    private static String generateCars( Request req, Response res )
    {
        Random rand = new Random();
        final String[] models = {
                "Opel",
                "Fiat",
                "Audi",
                "Dacia",
                "Honda"
        };

        Gson gson = new Gson();
        for( int i = 0; i < 10; i++ )
        {
            Car car = gson.fromJson( req.body(), Car.class );
            car.uuid = uuid.generate().toString();
            car.id = newId();
            car.model = models[ rand.nextInt( models.length ) ];
            car.colour = randomColour();
            car.year = 1990 + rand.nextInt( 30 );
            for( Airbag a : car.airbags ){
                a.value = rand.nextBoolean();
            }
            cars.add( car );
        }
        return gson.toJson( true, boolean.class );
    }

    private static String generateInvoice( Request req, Response res )
    {
        Gson gson = new Gson();
        String toFind = gson.fromJson( req.body(), Car.class ).uuid;
        for( Car c : cars )
        {
            if (Objects.equals(c.uuid, toFind)) {
                c.invoiceGenerated = true;
                createInvoicePDF( c );
            }
        }

        return gson.toJson( true, boolean.class );
    }

    private static void createInvoicePDF( Car car )
    {
        Document document = new Document();
        String path = "invoices/" + car.uuid + ".pdf";
        try {
            PdfWriter.getInstance( document, new FileOutputStream( path ));
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        document.open();

        Font headerFont = FontFactory.getFont( FontFactory.COURIER_BOLD, 16, BaseColor.BLACK );
        Chunk chunk = new Chunk( "FAKTURA DLA: " + car.uuid, headerFont );

        try {
            document.add( chunk );
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        document.close();
    }

    private static String getInvoice( Request req, Response res )
    {
        return "d";
    }

    private static String randomColour()
    {
        Random rand = new Random();
        return "#" + Integer.toHexString( Math.round(16777216 * rand.nextFloat()) );
    }

    private static int newId()
    {
        return cars.size() > 0 ? Collections.max( cars ).id + 1 : 1;
    }
}
