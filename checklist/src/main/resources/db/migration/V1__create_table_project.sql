do $$
begin
    create table project (
        id serial primary key,
        slug varchar(255) not null,
        name varchar(255) not null,
        description varchar(2000)
    );
end $$;