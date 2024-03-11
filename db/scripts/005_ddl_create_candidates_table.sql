CREATE table PUBLIC.candidates
(
    id            serial primary key,
    name         varchar not null,
    description   varchar not null,
    creation_date timestamp,
    city_id       int references public.cities(id),
    file_id       int references public.files(id)
);

