class Room {
    id
    name
    country
    creationDate
    ipOfCreator
    lightOn

    constructor() {}

    initWithNameAndCountry(name, country) {
        this.name = name
        this.country = country
        this.creationDate = new Date().toDateString()
    }

    initWithDTO(data) {
        this.id = data.id
        this.name = data.name
        this.country = data.country
        this.creationDate = data.creationDate
        this.ipOfCreator = data.ipOfCreator
    }

    initWithFullData(data) {
        this.id = data.id
        this.name = data.name
        this.country = data.country
        this.creationDate = data.creationDate
        this.ipOfCreator = data.ipOfCreator
        this.lightOn = data.lightOn
    }

    createHtmlElement() {
        const element = document.createElement('div')
        element.className = 'room'
        element.setAttribute('id', 'room' + this.id)
        element.innerHTML =
            `<div class="room-content">
               <div class="room-name">${this.name}</div>
               <div class="room-country">${this.country}</div>
               <div class="room-creation">${this.creationDate}</div>
            </div>
            <div class="room-footer">
                <div class="room-join-button">
                    join <i class="fas fa-sign-in-alt"></i>
                </div>        
                <div class="room-share-button" onclick="shareRoom('${this.id}')">
                    share <i class="fas fa-share-alt"></i>
                </div>        
                <div class="room-delete-button">
                    delete <i class="fas fa-trash"></i>
                </div>        
            </div>`
        const joinButton = element.querySelector('.room-join-button')
        const deleteButton = element.querySelector('.room-delete-button')
        if(window.userIP === this.ipOfCreator) {
            deleteButton.setAttribute('onclick', `deleteRoom('${this.id}')`)
        } else {
            deleteButton.classList.add('disabled')
        }
        if(window.userCountry.toLowerCase() === this.country.toLowerCase()) {
            joinButton.setAttribute('onclick', `joinRoom('${this.id}')`)
        } else {
            joinButton.classList.add('disabled')
        }
        return element
    }

    create() {
        return request('/api/rooms/new', 'POST', this)
    }

    delete() {
        request('/api/rooms/delete?id=' + this.id)
            .then(response => {
                if(response == ResponseCodes.SUCCESS) {
                    showMessage('room deleted successfully')
                    try {
                        document.getElementById('room' + this.id).remove()
                    } catch (e) {
                        console.warn(e)
                    }
                } else {
                    showMessage('you can\'t delete this room', -1)
                }
            })
    }
}