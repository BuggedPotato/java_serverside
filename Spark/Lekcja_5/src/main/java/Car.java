import java.util.ArrayList;

public class Car implements Comparable<Car> {
    public int id;
    public String uuid;
    public String model;
    public ArrayList<Airbag> airbags;
    public int year;
    public String colour;

    public Car(int id, String uuid, String model, ArrayList<Airbag> airbags, int year, String colour)
    {
        this.id = id;
        this.uuid = uuid;
        this.model = model;
        this.airbags = airbags;
        this.year = year;
        this.colour = colour;
    }

    @Override
    public int compareTo( Car o )
    {
        if( o.id == this.id )
            return 0;
        else if( o.id < this.id )
            return 1;
        return -1;
    }


}
