class ServerHelper {
    constructor(serverHost) {
        this.serverHost = serverHost;
        this.subscribeRoomChange();
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
        this._joinToRoom()
    }

    getRoomList = () => {
        return fetch(this.serverHost + '/room/list', {
            method: 'GET',
        }).then(res => res.json())
    }

    _joinToRoom = () => {
        if (this.roomId === undefined) {
            alert("Room have to be set");
            return;
        }

        if (this.playerId === undefined) {
            alert("PlayerId have to be set")
            return;

        }

        this.gameState = "RUNNING"
        return fetch(this.serverHost + '/room/join', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({roomId: this.roomId, playerId: this.playerId})
        }).then(res => {
            // console.log("Player " + data.playerId + " joined to room " + data.roomId)
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
        this.setRoomId(result.roomId);
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
            const rr = await fetch(this.serverHost + '/polling/poll', {
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
                        this.timer(3000)

                    }
                })
                .catch(e => {
                    console.log(e)
                    this.timer(3000)

                })
        }
    }

    onRoomChangeCallback = (callback) => {
        this.onRoomChangeCallback = callback
    }

    timer = ms => new Promise(res => setTimeout(res, ms))


    subscribeRoomChange = async () => {
        while (true) {
            if (this.roomId) {
                console.log("new room pooling")
                const result = await fetch(this.serverHost + '/room/poll', {
                    method: 'POST',
                    headers: {'Content-Type': 'application/json'},
                    body: JSON.stringify({tick: 0, roomId: this.roomId, playerId: this.playerId})
                }).then(response => response.json())
                    .then(json => {
                        console.log("Room state changed")
                        try {
                            this.onRoomChangeCallback(json)
                        } catch (e) {
                        }
                    })
                    .catch(e => {
                        console.log(e)
                    })
            } else {
                await this.timer(3000)
            }
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