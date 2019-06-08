var md5 = require('js-md5');

module.exports = (io, con) => {
	io.on('connection', (socket) => {
		console.log('connectid');
	});
}