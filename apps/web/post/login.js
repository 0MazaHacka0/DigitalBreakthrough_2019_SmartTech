let md5 = require('js-md5');

module.exports.func = async (con, req, res) => {
	let data = await con.promise(con, con.queryValue, 'SELECT userid FROM users WHERE login = ? AND password = ?', [req.body.login, md5(req.body.password)]);
	data = data.res;
	if (!data) return res.send('Неправильный логин или пароль <a href="//test-discord.ga">Главная</a>');

	req.session.userid = data;

	console.log('login ok', req.body.login);
	res.redirect('/menu');
};

module.exports.path = '/login';