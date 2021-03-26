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
		showLoader(element)
		room.initWithNameAndCountry(
			roomName.textContent,
			roomCountry.textContent
		)
		room.create().then(response => {
			hideLoader(element)
			if(response == ResponseCodes.NAME_TAKEN) {
				showMessage('this name is already taken' , -1)
				roomName.textContent = ''
				roomName.focus()
			} else if(response == ResponseCodes.COUNTRY_NOT_EXISTS) {
				showMessage('this country doesn\'t  exist', -1)
				roomCountry.textContent = ''
				roomCountry.focus()
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

function deleteRoom(roomId) {
	try {
		let element = document.getElementById('room' + roomId)
		showLoader(element)
		request('/api/rooms/delete?id=' + roomId)
			.then(response => {
				if(response == ResponseCodes.SUCCESS) {
					element.remove()
					showMessage('room deleted successfully')
				} else {
					hideLoader(element)
					showMessage('you can\'t delete this room', -1)
				}
			})
	} catch (e) {
		console.warn(e)
	}
}

function shareRoom(roomId) {
	const element = document.getElementById('room' + roomId)
		.querySelector('.room-name')
	let range, selection
	if (document.body.createTextRange) {
		range = document.body.createTextRange();
		range.moveToElementText(element);
		range.select();
	} else if (window.getSelection) {
		selection = window.getSelection();
		range = document.createRange();
		range.selectNodeContents(element);
		selection.removeAllRanges();
		selection.addRange(range);
	} else {
		showMessage('unavailable to copy link')
		return
	}
	try {
		document.execCommand('copy');
		showMessage('link copied to clipboard')
	} catch (err) {
		showMessage('unavailable to copy link')
	}
}

function joinRoom(roomId) {
	document.location.href = `/rooms/${roomId}`
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
	hideLoader(document.body)
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