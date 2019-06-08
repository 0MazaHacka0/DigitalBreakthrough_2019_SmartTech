let md5 = require('js-md5');

module.exports.func = async (con, req, res) => {
	let data = await con.promise(con, con.queryValue, 'SELECT id WHERE login = ? AND password = ?', [req.query.login, md5(req.query.password)]);
	data = data.res;
	if (!data) return res.json({err: "WrongEmailOrPass"});
	let keyg = md5(data + +new Date());
	con.update('server', {id: data, key: keyg}, () => {});
};

module.exports.path = '/api/login';