package scc.persistence.db;

public interface DBLayer {
    public UserDB getUserDB();

    public HouseDB getHouseDB();

    public RentalDB getRentalDB();

    public QuestionsDB getQuestionsDB();
}
