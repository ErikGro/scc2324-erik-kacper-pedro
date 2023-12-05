package scc.persistence.db;

public interface DBLayer {
    public UserContainer getUserContainer();

    public HouseContainer getHouseContainer();

    public RentalContainer getRentalContainer();

    public QuestionsContainer getQuestionsContainer();
}
