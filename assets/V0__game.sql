-- Creating the Game table
CREATE TABLE Game (
    id_game SERIAL PRIMARY KEY,
    name VARCHAR(100),
    image_url VARCHAR(255) DEFAULT NULL,
    type VARCHAR(10) CHECK (type IN ('shake', 'quiz')),
    instruction TEXT,
    started_at TIMESTAMP
);

INSERT INTO Game (name, image_url, type, instruction, started_at) VALUES
('Trivia Quiz', 'http://example.com/trivia.png', 'quiz', 'Trả lời các câu hỏi chính xác và nhanh nhất có thể', '2024-09-15 12:00:00'),
('Lắc xì Moomoo', 'http://example.com/shake.png', 'shake', 'Lắc thiết bị nhanh nhất có thể', '2024-10-10 15:00:00');

-- Creating the QuizGame table
CREATE TABLE QuizGame (
    id_game INT PRIMARY KEY,
    aim_score INT DEFAULT 0,
    FOREIGN KEY (id_game) REFERENCES Game(id_game)
);

-- Inserting mock data into the QuizGame table
INSERT INTO QuizGame (id_game, aim_score)
VALUES
(1, 100);

-- Creating the ShakeGame table
CREATE TABLE ShakeGame (
    id_game INT PRIMARY KEY,
    FOREIGN KEY (id_game) REFERENCES Game(id_game)
);

-- Inserting mock data into the ShakeGame table
INSERT INTO ShakeGame (id_game)
VALUES
(2);

-- Creating the Question table
CREATE TABLE Quiz (
    id_quiz SERIAL PRIMARY KEY,
    id_game INT,
    question_content VARCHAR(500),
    ans1 TEXT,
    ans2 TEXT,
    ans3 TEXT,
    correct_answer_index INT,

    FOREIGN KEY (id_game) REFERENCES Game(id_game)
);

-- Inserting mock data into the Quiz table
INSERT INTO Quiz (id_game, question_content, ans1, ans2, ans3, correct_answer_index)
VALUES
(1, 'Thủ đô nước Pháp là gì', 'Ha Noi', 'Ho Chi Minh', 'Paris', 3),
(1, 'Con vật có danh hiệu Đại sư huynh trong Tây du ký, là đồ đề của Đường Tăng, tên gì', 'Trư bát giới', 'Ngộ Không', 'Thiên bồng nguyên soái', 2),
(1, 'Ai là người đầu tiên bước lên mặt trăng', 'Neil Armstrong', 'Thomas Edison', 'Albert Einstein', 1);

-- Creating the Result table
CREATE TABLE Result (
    id_result SERIAL PRIMARY KEY,
    result_content TEXT
);

-- Inserting mock data into the Result table
INSERT INTO Result (result_content)
VALUES
('Win'),
('Lose');

-- Creating the PlaySession table
CREATE TABLE PlaySession (
    id_playsession SERIAL PRIMARY KEY,
    id_player INT,
    id_game INT,
    score INT DEFAULT 0,
    turns INT DEFAULT 0
);
