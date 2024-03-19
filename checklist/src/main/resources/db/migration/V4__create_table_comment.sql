do $$
begin
    create table comment (
        id uuid primary key,
        comment varchar(4000) not null,
        todo_id uuid not null references todo(id),
        created_worker_id bigint not null references worker(id),
        created_at timestamp without time zone not null,
        updated_worker_id bigint references worker(id),
        updated_at timestamp without time zone,
        deleted_at timestamp without time zone,
        deleted_worker_id bigint references worker(id)
    );
end $$;