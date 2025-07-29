CREATE TABLE lotto_draws (
    id SERIAL PRIMARY KEY,
    draw_date DATE NOT NULL UNIQUE
);

CREATE TABLE lotto_draw_numbers (
    draw_id INTEGER NOT NULL,
    number INTEGER NOT NULL,
    FOREIGN KEY (draw_id) REFERENCES lotto_draws(id)
)

CREATE TABLE lotto_number_stats (
    number INTEGER PRIMARY KEY,
    occurrences INTEGER NOT NULL,
    last_drawn DATE NOT NULL,
    skip_count INTEGER NOT NULL
);
