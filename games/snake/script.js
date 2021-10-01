let canvas = document.getElementById("snake"); //criar elemento que irá rodar o jogo
let context = canvas.getContext("2d"); //....
let box = 20;
const width = 30
const height = 30

const DIRECTIONS = {
    UP: "UP",
    DOWN: "DOWN",
    LEFT: "LEFT",
    RIGHT: "RIGHT",
}

let direction = DIRECTIONS.RIGHT;
let foods = [];
let snakes = [];

function drawBackground() {
    context.fillStyle = "lightgreen";
    context.fillRect(0, 0, width * box, height * box);
}

function drawSnakes() {
    for (let snake of snakes) {
        for (let snakeBody of snake.body) {
            context.fillStyle = "green";
            context.fillRect(snakeBody.x * box, snakeBody.y * box, box, box);
        }
    }
}

function drawFoods() {
    foods.forEach(item => {
        context.fillStyle = "red";
        context.fillRect(item.x, item.y, box, box);
    });
}

//quando um evento acontece, detecta e chama uma função
document.addEventListener('keydown', update);

function update(event) {
    const originDirection = direction;
    if (event.keyCode === 37 && direction !== DIRECTIONS.RIGHT) direction = DIRECTIONS.LEFT;
    if (event.keyCode === 38 && direction !== DIRECTIONS.DOWN) direction = DIRECTIONS.UP;
    if (event.keyCode === 39 && direction !== DIRECTIONS.LEFT) direction = DIRECTIONS.RIGHT;
    if (event.keyCode === 40 && direction !== DIRECTIONS.UP) direction = DIRECTIONS.DOWN

    if (direction !== originDirection) {
        fetch('http://localhost:8080/polling/send', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({direction: direction})
        })
    }
}

function playGame() {

    // if (snake[0].x > 15 * box && direction === "right") snake[0].x = 0;
    // if (snake[0].x < 0 && direction === 'left') snake[0].x = 16 * box;
    // if (snake[0].y > 15 * box && direction === "down") snake[0].y = 0;
    // if (snake[0].y < 0 && direction === 'up') snake[0].y = 16 * box;

    // for (i = 1; i < snake.length; i++) {
    //     if (snake[0].x == snake[i].x && snake[0].y == snake[i].y) {
    //         clearInterval(jogo);
    //         alert('Game Over :(');
    //     }
    // }



}


async function poll() {
    while (true) {

        const result = await fetch('http://localhost:8080/polling/poll', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({tick: 0})
        })
            .then(response => response.json());

        snakes = Object.values(result.game.snakeList);
        foods = result.game.foodList;

        drawBackground();
        drawSnakes();
        drawFoods();

    }
}

poll()

let jogo = setInterval(playGame, 500);