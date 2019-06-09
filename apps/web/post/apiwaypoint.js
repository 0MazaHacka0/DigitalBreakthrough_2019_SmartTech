module.exports.func = async (con, req, res) => {

	if (req.headers['authorization'].split(' ')[0] != 'Bearer') return res.json({err: "unauthorized"});

	let data = await con.promise(con, con.queryValue, 'SELECT userid FROM users WHERE keyg = ?', [req.headers['authorization'].split(' ')[1]]);
	data = data.res
	if (!data) return res.json({err: "NoUser"});

	console.log(req.body);

	let count = await con.promise(con, con.count, 'routes', {userid: data, day: req.body.day, direction: req.body.direction});
	count = count.res;

	if (count) con.query('UPDATE routes SET lat = ?, lng = ?, time = ?, free = ? WHERE userid = ? AND day = ? AND direction = ?', [req.body.lat, req.body.lng, req.body.time, req.body.free, data, req.body.day, req.body.direction], () => {});
	else con.insert('routes', {userid: data, lat: req.body.lat, lng: req.body.lng, time: req.body.time, day: req.body.day, free: req.body.free, direction: req.body.direction}, () => {});

	res.json({status: "OK"});

};

module.exports.path = '/api/waypoint';