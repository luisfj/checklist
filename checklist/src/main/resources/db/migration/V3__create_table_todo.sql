do $$
begin
    create table todo (
        id uuid primary key,
        title varchar(255) not null,
        description varchar(2000),
        project_id bigint not null references project(id),
        assigned_worker_id bigint references worker(id),
        created_at timestamp without time zone not null,
        updated_at timestamp without time zone,
        checked_at timestamp without time zone,
        checked_worker_id bigint references worker(id)
    );
end $$;
