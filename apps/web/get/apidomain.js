module.exports.func = (con, req, res) => {
	console.log(req.query);
	con.queryValue('SELECT id FROM companies WHERE domain =  ?', [req.query.domain], (err, val) => {if (val) res.json({icon: 'https://test-discord.ga/img/companies/' + val + '.jpg'}); else res.json({err: "NotHave"})});
};

module.exports.path = '/api/domain';