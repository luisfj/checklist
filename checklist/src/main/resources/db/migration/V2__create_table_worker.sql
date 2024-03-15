do $$
begin
    create table worker (
        id serial primary key,
        slug varchar(255) not null,
        name varchar(255) not null,
        project_id bigint not null references project(id)
    );
end $$;