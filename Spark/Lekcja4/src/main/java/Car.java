public class Car implements Comparable<Car> {
    public int id;
    public String model;
    public boolean damaged;
    public int doors;
    public String country;

    public Car( int id, String model, boolean damaged, int doors, String country )
    {
        this.id = id;
        this.model = model;
        this.damaged = damaged;
        this.doors = doors;
        this.country = country;
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
