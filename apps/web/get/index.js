module.exports.func = (con, req, res) => {

	res.render('/root/hackathon/views/index.ejs', 
		{
			title: "Main"
		});
};

module.exports.path = '/';