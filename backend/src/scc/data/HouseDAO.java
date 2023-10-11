package scc.data;

public class HouseDAO {
    private String _rid;
    private String _ts;
    private String id;
    private String name;

    public HouseDAO() {}

    public HouseDAO(House house) {
        this(house.getId(), house.getName());
    }

    public HouseDAO(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String get_rid() {
        return _rid;
    }

    public void set_rid(String _rid) {
        this._rid = _rid;
    }

    public String get_ts() {
        return _ts;
    }

    public void set_ts(String _ts) {
        this._ts = _ts;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public House toHouse() {
        return new House(id, name);
    }
}
