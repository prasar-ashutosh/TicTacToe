var ws;

var connect_section;
var start_join_section;
var player_name_section;
var join_game_section;
var player_message_section;
var game_message_section;

var tiles;
var board = ['', '', '', '', '', '', '', '', ''];
var playerName;
var marker;
var isActive = false;
var gameId;
var opponent;
var gameType;

function connect() {
    // Initialize the html sections
    connect_section = document.getElementById("connect");
    start_join_section = document.getElementById("start-join");
    player_name_section = document.getElementById("player-name");
    join_game_section = document.getElementById("join-game");
    player_message_section = document.getElementById("player-message");
    game_message_section = document.getElementById("game-message");

    tiles = Array.from(document.querySelectorAll('.tile'));

    // Connect with Websocket end point
    var username = document.getElementById("username").value;
    playerName = username.trim();
    player_name_section.innerHTML = `<h2>${playerName}</h2>`;
    var host = document.location.host;
    var pathname = document.location.pathname;

    if (playerName) {
        ws = new WebSocket("ws://" +host  + pathname + "tic-tac-toe/" + username);
        ws.onmessage = handleServerMessage;
        // Initialize the Listeners
        tiles.forEach( (tile, index) => {
            tile.addEventListener('click', () => userAction(tile, index));
        });
    }
}

function handleServerMessage(event) {
    console.log(event.data);
    var message = JSON.parse(event.data);
    gameId = message.gameId;
    isActive = message.isActive;
    opponent = message.opponentName;
    let playerMessage;
    let gameMessage;

    switch(message.messageType) {
        case 'CONNECTED':
            playerMessage = 'Welcome!! Start/Join a Game Or Play with a Bot';
            connect_section.classList.add('hide');
            player_message_section.classList.remove('hide');
            start_join_section.classList.remove('hide');
            break;
        case 'GAME_LAUNCHED':
            playerMessage = `Game Launched!! Game Id: ${gameId} <br/>Share the game id with your friend and Wait for them to join ...`;
            player_message_section.classList.remove('hide');
            start_join_section.classList.add('hide');
            break;
        case 'GAME_STARTED':
            if (isActive) {
                playerMessage = `Awesome, Your opponent has joined!!<br/> Your marker is <span class="PlayerX">X</span>, you are playing against ${opponent}`;
                gameMessage = "<span class='fade-in'>It's Your turn, go for it!!<span/>";
            } else {
                playerMessage = `Awesome, you have joined the Game!!<br/> Your marker is <span class="PlayerO">O</span>, you are playing against ${opponent}`;
                gameMessage = "<span class='fade-in'>Wait for your turn!!<span/>";
            }
            player_message_section.classList.remove('hide');
            game_message_section.classList.add(`player${marker}`);
            game_message_section.classList.remove('hide');
            start_join_section.classList.add('hide');
            join_game_section.classList.add('hide');
            break;
        case 'GAME_JOIN_ERROR':
            playerMessage = `Error Joining Game: ${message.message}`;
            player_message_section.classList.remove('hide');
            break;
        case 'GAME_UPDATE':
            playerMessage = `Game on!!<br/> Your marker is <span class='Player${marker}'>${marker}</span>, you are playing against ${opponent}`;
            if (isActive) {
                gameMessage = "<span class='fade-in'>It's Your turn, go for it!!<span/>";
             } else {
                gameMessage = "<span class='fade-in'>Good Move! Wait for your turn!<span/>";
             }
            board = message.board;
            updateBoard(message.currentIndex);
            break;
        case 'INVALID_MOVE':
            playerMessage = `Game on!!<br/> Your marker is <span class='Player${marker}'>${marker}</span>, you are playing against ${opponent}`;
            gameMessage = `Invalid Move: ${message.message}`;
            break;
        case 'GAME_OVER':
            playerMessage = 'Game Over!! Press Quit to exit the Game'
            gameMessage = getResultMessage(message);
            board = message.board;
            updateBoard(message.currentIndex);
    }
    player_message_section.innerHTML = playerMessage;
    game_message_section.innerHTML = gameMessage;
}

function updateBoard(currentIndex = -1) {
    let i = 0;
    tiles.forEach(tile => {
       if(board[i] !== null)
       {
           if(currentIndex == i) {
              tile.innerHTML = `<span class='fade-in'>${board[i]}<span>`;
           } else {
              tile.innerText = board[i];
           }
           tile.classList.remove('playerX');
           tile.classList.remove('playerO');
           tile.classList.add(`player${board[i]}`);
       }
       i = i + 1;
    });
}

function flashWinningLines(winningLines) {
    let i = 0;
    tiles.forEach(tile => {
       if(winningLines.includes(i))
       {
           tile.classList.add('blink');
       }
       i = i + 1;
    });
}

function startGame() {
    gameType = 'MULTI_PLAYER';
    marker = 'X';
    var json = JSON.stringify({
        "messageType":"START_GAME",
        "gameType": gameType
    });
    ws.send(json);
}

function playWithABot() {
    gameType = 'BOT';
    var markerArray = ['X', 'O'];
    marker = markerArray[(Math.random() * markerArray.length) | 0]
    console.log(marker)

    var json = JSON.stringify({
        "messageType":"START_GAME",
        "marker": marker,
        "gameType": gameType
    });
    ws.send(json);
}

function exitGame() {
    board = ['', '', '', '', '', '', '', '', ''];
    gameType = '';
    updateBoard();
    tiles.forEach(tile => {
       tile.classList.remove('blink');
       tile.classList.remove('fade-in');
    });
    isActive = false;
    game_message_section.classList.remove(`player${marker}`);
    marker = '';
    start_join_section.classList.remove('hide');
    game_message_section.classList.add('hide');
    player_message_section.classList.add('hide');
}

function join() {
    start_join_section.classList.add('hide');
    join_game_section.classList.remove('hide');
    game_message_section.innerHTML = 'Enter the Game ID to join the game';
}

function joinGame() {
    gameType = 'MULTI_PLAYER';
    var gameId = document.getElementById("game-id").value;
    marker = 'O';
    var json = JSON.stringify({
        "messageType":"JOIN_GAME",
        "gameId": gameId,
        "gameType": gameType
    });
    ws.send(json);
}

const userAction = (tile, index) => {
    if(isValidAction(tile) && isActive) {
        var json = JSON.stringify({
            "messageType":"MOVE",
            "gameId": gameId,
            "marker": marker,
            "index": index,
            "gameType": gameType
        });
        ws.send(json);
    }
}

const getResultMessage = (message) => {
    var resultMessage;
    switch(message.gameResult) {
        case 'X_WIN':
            flashWinningLines(message.winningLineIndexes);
            if(marker == 'X') {
                resultMessage = "<span class='fade-in announcement'>Congratulations, You win!!</span>"
            } else {
                resultMessage = "<span class='fade-in announcement'>Your opponent won!! Better luck next time!!</span>"
            }
            break;
        case 'O_WIN':
            flashWinningLines(message.winningLineIndexes);
            if(marker == 'O') {
                resultMessage = "<span class='fade-in announcement'>Congratulations, You win!!</span>"
            } else {
                resultMessage = "<span class='fade-in announcement'>Your opponent won!! Better luck next time!!</span>"
            }
            break;
        case 'TIE':
            resultMessage = "<span class='fade-in announcement'>It's a Tie</span>";
    }
    return resultMessage;
};

const isValidAction = (tile) => {
    if (tile.innerText === 'X' || tile.innerText === 'O'){
        return false;
    }
    return true;
};