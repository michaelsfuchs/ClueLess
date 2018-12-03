package game;
import java.util.ArrayList;

public class Location
{
	int locId;
	Type type;
	int occupancy;
	ArrayList<Location> connections;
	
	Location(int aLocId, Type aType)
	{
		locId = aLocId;
		type = aType;
		occupancy = 0;
		this.connections = new ArrayList<Location>();
	}
	
	enum Type
	{
		HALLWAY,
		ROOM,
	}
};
