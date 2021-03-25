const roomAdder = document.getElementById('roomAdder')
window.rooms = []
defineUser()
	.then( () => loadRooms())


async function createNewRoom(){
	let room = new Room()
	let element = room.createTemplateElement()
	element.querySelector('.room-footer').style.opacity = '0'
	const roomName = element.querySelector('.room-name')
	const roomCountry = element.querySelector('.room-country')
	roomName.setAttribute('contentEditable', 'true');
	roomCountry.setAttribute('contentEditable', 'true');
	setPlaceholder(roomName, 'name')
	setPlaceholder(roomCountry, 'country')
	roomName.addEventListener('keydown', function (event) {
		if(event.keyCode === 13) {
			event.preventDefault()
			roomName.blur()
		}
	})
	roomName.addEventListener('blur', () => {
		roomCountry.focus()
	})
	roomCountry.addEventListener('keydown', function (event) {
		if(event.keyCode === 13) {
			event.preventDefault()
			roomCountry.blur()
		}
	})
	roomCountry.addEventListener('blur', () => {
		room.initWithNameAndCountry(
			roomName.textContent,
			roomCountry.textContent
		)
		room.create().then(response => {
			if(response == ResponseCodes.NAME_TAKEN) {
				showMessage('this name is already taken' , -1)
				roomName.innerHTML = ''
				roomName.focus()
			} else {
				room.ipOfCreator = window.userIP
				room.id = response
				window.rooms.push(room)
				const newElement = room.createHtmlElement()
				element.setAttribute('id', newElement.getAttribute('id'))
				element.innerHTML = newElement.innerHTML
				showMessage('room created successfully')
			}
		})
	})
	roomAdder.after(element)
	roomName.focus()
}

async function loadRooms() {
	const response = await request('/api/rooms')
	response.forEach(roomData => {
		let room = new Room()
		room.initWithDTO(roomData)
		const roomElement = room.createHtmlElement()
		roomAdder.after(roomElement)
		window.rooms.push(room)
	})
}

async function defineUser() {
	const response = await request('/api/users/current')
	window.userIP = response.ipAddress
	window.userCountry = response.country
}

function setPlaceholder(element, placeholder) {
	(element.innerHTML === '') && (element.innerHTML = placeholder);
	element.addEventListener('focus', () => {
		const value = element.innerHTML;
		value === placeholder && (element.innerHTML = '');
	});
	element.addEventListener('blur', () => {
		if(element.textContent.trim() === '') {
			element.parentElement.parentElement.remove()
		}
	});
}