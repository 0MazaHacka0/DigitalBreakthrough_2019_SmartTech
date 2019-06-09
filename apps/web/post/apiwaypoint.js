module.exports.func = async (con, req, res) => {

	if (req.headers['authorization'].split(' ')[0] != 'Bearer') return res.json({err: "unauthorized"});

	let data = await con.promise(con, con.query, 'SELECT userid FROM users WHERE keyg = ?', [req.headers['authorization'].split(' ')[0]]);
	if (!data) return res.json({err: "NoUser"});

	con.query('INSERT INTO routes (userid, lat, lng, time, day, free) VALUES (?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE lat = ?, lng = ?, time = ?, free = ?', 
										[data, req.body.lat, req.body.lng, req.body.time, req.body.day, req.body.free, req.body.lat, req.body.lng, req.body.time, req.body.free], () => {});
	res.json({status: "OK"});

};

module.exports.path = '/api/waypoint';