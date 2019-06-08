module.exports.func = async (con, req, res) => {

	if (req.headers['authorization'].split(' ')[0] == 'Bearer') return res.json({err: "unauthorized"});

	let data = await con.promise(con, con.queryHash, 'SELECT address, lat, lng FROM addressCompanies WHERE id = (SELECT id FROM users WHERE keyg = ?)', [req.headers['authorization'].split(' ')[0]]);
	data = data.res;

	if (!data) return res.json({err: "NoAddress"});
	console.log(data);
	res.json(data);

};

module.exports.path = '/api/address';