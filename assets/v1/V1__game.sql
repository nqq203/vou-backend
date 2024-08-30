-- Creating the Game table
CREATE TABLE Game (
    id_game SERIAL PRIMARY KEY,
    name VARCHAR(100),
    image_url VARCHAR(255) DEFAULT NULL,
    type VARCHAR(10) CHECK (type IN ('shake-game', 'quiz-game')),
    instruction TEXT,
    started_at TIMESTAMP,
    event_id INT DEFAUlT NULL
);

-- Creating the QuizGame table
CREATE TABLE QuizGame (
    id_game INT PRIMARY KEY,
    aim_score INT DEFAULT 0,
    FOREIGN KEY (id_game) REFERENCES Game(id_game)
);

-- Creating the ShakeGame table
CREATE TABLE ShakeGame (
    id_game INT PRIMARY KEY,
    FOREIGN KEY (id_game) REFERENCES Game(id_game)
);

-- Creating the Question table
CREATE TABLE Quiz (
    id SERIAL PRIMARY KEY,
    id_game INT,
    question VARCHAR(500),
    ans1 TEXT,
    ans2 TEXT,
    ans3 TEXT,
    correct_answer_index INT,

    FOREIGN KEY (id_game) REFERENCES Game(id_game)
);

-- -- Creating the Result table
-- CREATE TABLE Result (
--     id_result SERIAL PRIMARY KEY,
--     result_content TEXT
-- );

-- Creating the PlaySession table

CREATE TABLE PlaySession (
    id_playsession SERIAL PRIMARY KEY,
    id_player INT,
    id_game INT,
    score INT DEFAULT 0,
    turns INT DEFAULT 0
);
