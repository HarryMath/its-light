const lamp = document.getElementById('lamp')
defineRoomAndConnect()
let stompClient
let connected = false

lamp.addEventListener('click', switchLight)


function connect() {
    let socket = new SockJS('/its-light');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        connected = true;
        stompClient.subscribe(
            '/rooms/' + window.currentRoom.id + '/light',
            handleMessage
        );
    });
    hideLoader(document.body)
}

function switchLight() {
    let on = lamp.classList.contains('on')
    on = !on
    stompClient.send("/app/switch", {}, JSON.stringify({
        'on': on,
        'roomId': window.currentRoom.id
    }))
}

function handleMessage(data) {
    let light = JSON.parse(data.body)
    lamp.className = light.on ? 'on' : ''
}

function defineRoomAndConnect() {
    const hrefParts = document.location.href.split('/')
    const roomId = hrefParts[hrefParts.length - 1].split('?')[0]
    request(`/api/rooms/${roomId}`)
        .then(response => {
            if(response === 'null' || !response) {
                showMessage('you have no access to this room')
                document.location.href = '/'
            } else {
                window.currentRoom = response;
                if(response.lightOn) lamp.className = 'on'
                connect()
            }
        })
}