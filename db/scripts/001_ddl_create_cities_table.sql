create table public.cities
(
    id   serial primary key,
    name varchar not null unique
);