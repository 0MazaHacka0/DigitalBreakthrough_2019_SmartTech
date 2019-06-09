function getDistanceFromLatLonInKm(lat1,lon1,lat2,lon2) {
  var R = 6371; // Radius of the earth in km
  var dLat = deg2rad(lat2-lat1);  // deg2rad below
  var dLon = deg2rad(lon2-lon1); 
  var a = 
    Math.sin(dLat/2) * Math.sin(dLat/2) +
    Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * 
    Math.sin(dLon/2) * Math.sin(dLon/2)
    ; 
  var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a)); 
  var d = R * c; // Distance in km
  return d;
}

function deg2rad(deg) {
  return deg * (Math.PI/180)
}

module.exports.func = async (con, req, res) => {

	if (req.headers['authorization'].split(' ')[0] != 'Bearer') return res.json({err: "unauthorized"});

	let data = await con.promise(con, con.query, 'SELECT name, routes.lat, routes.lng, time, day, avatar FROM routes LEFT JOIN users using(userid) WHERE id = (SELECT id FROM users WHERE keyg = ?) AND free > broned AND direction = ?', [req.headers['authorization'].split(' ')[1], req.query.direction]);
  data = data.res;

	if (!data) return res.json({err: "NoDrivers"});

  if (!req.query.direction) {
  	data.forEach((item, i, arr) => {
  		console.log(getDistanceFromLatLonInKm(req.query.lat, req.query.lng, item.lat, item.lng));
  		if (getDistanceFromLatLonInKm(req.query.lat, req.query.lng, item.lat, item.lng) > 1.5) {arr.splice(i, 1)}
  	});
  }

	console.log(data);
	res.json(data);

};

module.exports.path = '/api/drivers';