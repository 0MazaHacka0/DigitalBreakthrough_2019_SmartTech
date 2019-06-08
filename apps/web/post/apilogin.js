let md5 = require('js-md5');

module.exports.func = async (con, req, res) => {
	let data = await con.promise(con, con.queryValue, 'SELECT userid FROM users WHERE login = ? AND password = ?', [req.body.login, md5(req.body.password)]);
	data = data.res;
	if (!data) return res.json({err: "WrongEmailOrPass"});
	let keyg = md5('' + data + (+new Date()));
	con.upsert('users', {userid: data, keyg: keyg}, () => {});
	console.log('login ok', req.body.login);
	res.json({key: keyg});
};

module.exports.path = '/api/login';