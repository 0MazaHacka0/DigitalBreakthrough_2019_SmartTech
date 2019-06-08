module.exports.func = (con, req, res) => {
	con.queryValue('SELECT id FROM companies WHERE domain =  ?', [req.query.domain], (err, val) => {if (val) res.json('/img/companies' + val + '.png'); else res.json({err: "NotHave"})});
};

module.exports.path = '/api/domain';