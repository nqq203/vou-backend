CREATE TABLE Quiz_Game_Stats (
    id_quiz_game_stat SERIAL PRIMARY KEY ,
    id_event INT,
    number_of_participants INT
);

CREATE TABLE Player_Result
(
    id_player_result SERIAL PRIMARY KEY ,
    id_event  INT,
    player_username TEXT,
    rank      INT
);

CREATE TABLE Quiz_Question_Stats (
    id_quiz_question INT PRIMARY KEY,
    id_quiz INT,
    correct_count INT,
    incorrect_count INT
);