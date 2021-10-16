class ServerHelper {
    constructor(serverHost) {
        this.serverHost = serverHost;
    }

    getPlayer = () => {
        return {playerId: this.playerId, roomId: this.roomId, gameName: this.gameName}
    }

    setPlayerId = (playerId) => {
        console.log("PlayerId: " + playerId)
        this.playerId = playerId;
    }

    setRoomId = (roomId) => {
        console.log("Room changed to " + roomId)
        this.roomId = roomId;
    }

    getRoomList = () => {
        return fetch(this.serverHost + '/room/list', {
            method: 'GET',
        }).then(res => res.json())
    }

    joinToRoom = (data) => {
        this.gameState = "RUNNING"
        this.playerId = data.playerId;
        this.roomId = data.roomId;
        return fetch(this.serverHost + '/room/join', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(data)
        }).then(res => {
            console.log("Player " + data.playerId + " joined to room " + data.roomId)
            return res.json()
        })
    }

    createRoom = async (data, callback) => {

        const result = await fetch(this.serverHost + '/room/create', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(data)
        }).then(res => {
            console.log("Room created")
            return res.json()
        }).then(json => {
            return json
        });
        this.gameName = data.gameName; //todo toto zlepšit -> použije se pro načtení zdrojáků
        this.playerId = result.roomOwner.playerId;
        this.roomId = result.roomId;
        callback()
    }

    startGame = () => {
        this.gameState = "RUNNING"
        fetch(this.serverHost + '/room/startGame', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({roomId: this.roomId, playerId: this.playerId})
        })
    }


    onGameChange = async (callback) => {

        while (this.gameState === "RUNNING") {
            console.log("new pooling")
            const rr = await fetch('http://localhost:8080/polling/poll', {
                method: 'POST',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify({tick: 0, roomId: this.roomId, playerId: this.playerId})
            }).then(response => response.json())
                .then(json => {
                    console.log("Game changed")
                    try {
                        this.gameState = json.state;
                        callback(json)
                    } catch (e) {
                    }
                })
                .catch(e => {
                    console.log(e)
                })
        }

    }

    sendMove = (data) => {
        fetch(this.serverHost + '/polling/send', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({...data, tick: 0, roomId: this.roomId, playerId: this.playerId})
        })
    }


}