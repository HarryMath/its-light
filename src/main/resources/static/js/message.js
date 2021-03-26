let message
let messageInterval
const msg_window = document.createElement('div')
msg_window.className = 'msg_window'
msg_window.innerHTML = `<div class="msg"></div><div class="msg_closer" onclick="hideMessage()">×</div>`
document.querySelector('html').appendChild(msg_window)
try {
	message = document.getElementById('message').textContent
}
catch(e) {
	message = null
}
if(message) {
	setTimeout(showMessage, 300, message)
}

function showMessage(message, status=null) {
	msg_window.querySelector('.msg').innerHTML = message
	if ( status === -1) {
		try {
			window.navigator.vibrate(30)
		} catch (e) {
			console.log(e)
		}
	}
	msg_window.classList.add('msg_visible')
	messageInterval = window.setInterval(function () {
		hideMessage()
	}, 6000);
}
function hideMessage() {
	clearInterval(messageInterval)
	msg_window.classList.remove('msg_visible')
}

function showLoader(element) {
	const loaderElement = document.createElement('div')
	loaderElement.className = 'loader-overlay'
	element.prepend(loaderElement)
}

function hideLoader(element) {
	try {
		element.querySelector('.loader-overlay').remove()
	} catch (e) {
		console.warn(e)
	}
}