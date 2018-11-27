import java.util.ArrayList;

public class Location
{
	int locId;
	Type type;
	int occupancy;
	ArrayList<Location> connections;
	
	Location()
	{
		this.connections = new ArrayList<Location>();
	}
	
	enum Type
	{
		HALLWAY,
		ROOM,
	}
};
