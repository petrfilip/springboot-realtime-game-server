console.log("Snake game loaded");
let canvas = document.getElementById("gameBoard");
let context = canvas.getContext("2d");
let box = 20;
const width = 30
const height = 30

canvas.width = (box * width) + box;
canvas.height = (box * height) + box;

const DIRECTIONS = {
    UP: "UP",
    DOWN: "DOWN",
    LEFT: "LEFT",
    RIGHT: "RIGHT",
}

let direction = null;
let foods = [];
let snakes = [];
const playerColors = [];

const getPlayerColors = (index) => {
    if (playerColors[index] === undefined) {
        playerColors[index] = "#" + Math.floor(Math.random() * (16777215)).toString(16);
    }
    return playerColors[index];
}

function drawBackground() {
    context.fillStyle = "lightgreen";
    context.fillRect(0, 0, (width * box) + box, (height * box) + box);
}

function drawSnakes() {
    for (let i = 0; i < snakes.length; i++) {
        const snake = snakes[i]
        context.fillStyle = getPlayerColors(i);
        for (let snakeBody of snake.body) {
            context.fillRect(snakeBody.x * box, snakeBody.y * box, box, box);
        }
    }
}

function drawFoods() {
    foods.forEach(item => {
        context.fillStyle = "red";
        context.fillRect(item.x * box, item.y * box, box, box);
    });
}

document.addEventListener('keydown', update);

function update(event) {
    const originDirection = direction;
    if (event.keyCode === 37 && direction !== DIRECTIONS.RIGHT) direction = DIRECTIONS.LEFT;
    if (event.keyCode === 38 && direction !== DIRECTIONS.DOWN) direction = DIRECTIONS.UP;
    if (event.keyCode === 39 && direction !== DIRECTIONS.LEFT) direction = DIRECTIONS.RIGHT;
    if (event.keyCode === 40 && direction !== DIRECTIONS.UP) direction = DIRECTIONS.DOWN

    if (direction !== originDirection) {
        serverConnection.sendMove({direction: direction})
    }
}


serverConnection.onGameChange((data) => {
    console.log(data)
    snakes = Object.values(data.game.snakeList);
    foods = data.game.foodList;

    drawBackground();
    drawSnakes();
    drawFoods();
})

