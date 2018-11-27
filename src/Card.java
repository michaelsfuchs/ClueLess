enum CardType
{
	SUSPECT,
	ROOM,
	WEAPON,
}

public class Card
{
	CardType type;
	int cardID;
	
	Card(CardType aType, int aCardID)
	{
		type = aType;
		cardID = aCardID;
	}
	
	public boolean equals(Card other)
	{
		return (this.type == other.type && this.cardID == other.cardID);
	}
	
};
