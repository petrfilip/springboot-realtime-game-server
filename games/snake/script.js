let canvas = document.getElementById("snake");
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

function drawBackground() {
    context.fillStyle = "lightgreen";
    context.fillRect(0, 0, (width * box) + box, (height * box) + box);
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
        fetch('http://localhost:8080/polling/send', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({direction: direction})
        })
    }
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

