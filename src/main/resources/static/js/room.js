class Room {
    id
    name
    country
    creationDate
    ipOfCreator
    lightOn

    constructor() {
        this.id = ""
        this.name = ""
        this.country = ""
        this.creationDate = ""
        this.ipOfCreator = ""
        this.lightOn = false
    }

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
        const element = this.createTemplateElement()
        const shareButton = element.querySelector('.room-share-button')
        const joinButton = element.querySelector('.room-join-button')
        const deleteButton = element.querySelector('.room-delete-button')
        if(window.userIP === this.ipOfCreator) {
            deleteButton.setAttribute('onclick', `deleteRoom('${this.id}')`)
        } else {
            deleteButton.classList.add('disabled')
        }
        if(window.userCountry.toLowerCase() === this.country.toLowerCase()) {
            joinButton.setAttribute('onclick', `joinRoom('${this.id}')`)
            element.classList.add('room-available')
        } else {
            joinButton.classList.add('disabled')
        }
        shareButton.setAttribute('onclick', `shareRoom('${this.id}')`)
        return element
    }

    createTemplateElement() {
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
                <div class="room-share-button">
                    share <i class="fas fa-share-alt"></i>
                </div>        
                <div class="room-delete-button">
                    delete <i class="fas fa-trash"></i>
                </div>        
            </div>`
        return element
    }

    create() {
        return request('/api/rooms/new', 'POST', this)
    }
}