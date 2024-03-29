package repository.connection;

import org.jdbi.v3.core.Jdbi;

public class DbConnection {


    private static final DbConnection dbInstance = new DbConnection();

    public static DbConnection getInstance() {
        return dbInstance;
    }

    private Jdbi jdbi;

    private DbConnection() {
        connect();
        createTables();
    }

    private void connect() {
        jdbi = Jdbi.create("jdbc:mysql://localhost:3306/cinema_tickets?dateStrings=true&useUnicode=true&useJDBCCompliantTimezoneShift=false" +
                "&useLegacyDatetimeCode=false&serverTimezone=Europe/Warsaw&useSSL=false", "root", "root");
    }

    private void createTables() {
        jdbi.useHandle(handle -> handle.execute("create table if not exists movies ( " +
                "id integer primary key auto_increment, " +
                "title varchar(50) not null, " +
                "genre varchar(50) not null," +
                "price decimal default 0," +
                "duration integer default 0," +
                "release_date date not null" +
                ");"
        ));

        jdbi.useHandle(handle -> handle.execute("create table if not exists loyalty_cards (" +
                "id integer primary key auto_increment, " +
                "expiration_date date not null," +
                "discount decimal(4,2) not null," +
                "current_movies_quantity integer not null," +
                "movies_quantity integer not null" +
                ");"
        ));

        jdbi.useHandle(handle -> handle.execute("create table if not exists customers (" +
                "id integer primary key auto_increment, " +
                "name varchar (50) not null," +
                "surname varchar (50) not null," +
                "age integer default 18," +
                "email varchar (50) not null," +
                "loyalty_card_id integer," +
                "foreign key (loyalty_card_id) references loyalty_cards(id) on delete cascade on update cascade" +
                ");"
        ));

        jdbi.useHandle(handle -> handle.execute("create table if not exists sales_stands (" +
                "id integer primary key auto_increment," +
                "customer_id integer," +
                "movie_id integer," +
                "start_date_time TimeStamp," +
                "price_with_discount decimal (4,2) not null default 0, "+
                "foreign key (customer_id) references customers(id) on delete cascade on update cascade," +
                "foreign key (movie_id) references movies(id) on delete cascade on update cascade" +
                ");"
        ));
    }

    public Jdbi getJdbi() {
        return jdbi;
    }
}
