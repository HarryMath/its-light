const ResponseCodes = {
	SUCCESS: 0,
	NO_ACCESS: -1,
	DATABASE_ERROR: -2,
	NAME_TAKEN: -3,
	COUNTRY_NOT_EXISTS: -4
}

async function request(url, method = 'GET', data = null) {
	try {
		const headers = {}
		let body
		if(data){
			headers['Content-Type'] = 'application/json'
			body = JSON.stringify(data)
		}
		const response = await fetch(url, {
			method,
			headers,
			body
		})
		const text = await response.text();
		try {
			return JSON.parse(text)
		} catch (e) {
			console.log(e)
			return text
		}
	}
	catch(e) {
		console.log(e)
		console.log(url)
	}
}