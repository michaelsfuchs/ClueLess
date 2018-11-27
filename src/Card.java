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
	
	public boolean equals(Object other)
	{
		if(other == null)
		{
			return false;
		}
		if(other instanceof Card)
		{
			Card otherCard = (Card)other;
			return (this.type == otherCard.type && this.cardID == otherCard.cardID);
		}
		return false;
	}
	
	public int hashCode() {
        int hash = 7;
        Integer id = (Integer)(this.cardID);
        hash = 17 * hash + (id != null ? id.hashCode() : 0);
        return hash;
    }
};
