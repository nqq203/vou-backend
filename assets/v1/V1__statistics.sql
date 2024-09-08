CREATE TABLE Quiz_Game_Stats (
    id_quiz_game_stat SERIAL PRIMARY KEY ,
    id_event INT,
    id_game INT,
    number_of_participants INT
);

CREATE TABLE Quiz_Winner
(
    id_quiz_winner SERIAL PRIMARY KEY ,
    id_event  INT,
    id_game   INT,
    winner_id INT,
    rank      INT
);

CREATE TABLE Quiz_Question_Stats (
    id_quiz_question INT PRIMARY KEY,
    id_game INT,
    id_quiz INT,
    correct_count INT,
    incorrect_count INT
);