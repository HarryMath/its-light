const roomAdder = document.getElementById('roomAdder')
window.rooms = []
defineUser()
	.then( () => loadRooms())


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