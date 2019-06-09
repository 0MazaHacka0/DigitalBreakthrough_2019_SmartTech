module.exports.func = async (con, req, res) => {

	if (req.headers['authorization'].split(' ')[0] != 'Bearer') return res.json({err: "unauthorized"});

	let data = await con.promise(con, con.queryValue, 'SELECT userid FROM users WHERE keyg = ?', [req.headers['authorization'].split(' ')[1]]);
	data = data.res
	if (!data) return res.json({err: "NoUser"});

	console.log(req.body);

	con.query('UPDATE users SET routeid = ? WHERE userid = ?', [req.body.routeid, data]);
	con.query('UPDATE routes SET broned = broned + 1 WHERE routeid = ?', [req.body.routeid]);

	res.json({status: "OK"});

};

module.exports.path = '/api/reserve';