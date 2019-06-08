var express = require('express')
	, mainApp = express()
	, fs = require('fs')
	, http = require('http').Server(mainApp)
	, session = require('express-session')
	, MySQLStore = require('express-mysql-session')(session)
	, io = require('socket.io').listen(http)
	, bodyParser = require('body-parser')
	;

let con = require('mysql').createConnection({user: "hackathon", password: "hackathon", database: "hackathon", charset: "utf8mb4"});
con.on('error', (err) => {console.warn(err)});
con.connect((err) => {if (err) return console.error('error connecting: ' + err.stack); console.log('mysql for MAIN as id ' + con.threadId);});
require('mysql-utilities').upgrade(con);
require('mysql-utilities').introspection(con);

con.promise = require('/root/hackathon/modules/promise');
con.request = require('request');

session = session({secret: "493yty984ygr98", store: new MySQLStore({expiration: 604800000}, con), resave: false, saveUninitialized: false, name: 'sid', cookie: {maxAge: 604800000, domain: '.server-discord.com'}});

io.use((socket, next) => {session(socket.request, socket.request.res, next)});
try {require('./modules/io')(io, con)} catch (e) {console.warn(e)}

mainApp.use(bodyParser.urlencoded({ extended: true })).use(bodyParser.json()).use(require('cookie-parser')()).use(session);

mainApp.use(express.static('/root/hackathon/public'));


let gets = fs.readdirSync("/root/hackathon/get/");

gets.forEach(file => {
	if (!file.endsWith(".js")) return;

	try{
	  const get = require(`./get/${file}`);
	  mainApp.get(get.path, get.func.bind(null, con));
	  console.log(`GET `+`${get.path}`+` загржен.`);
	  delete require.cache[require.resolve(`./get/${file}`)];
	} catch (e) {console.warn(e)}

});


let posts = fs.readdirSync("/root/hackathon/post/");

posts.forEach(file => {
  if (!file.endsWith(".js")) return;

  try {
    const post = require(`./post/${file}`);
    mainApp.post(post.path, post.func.bind(null, con));
    console.log(`POST `+`${post.path}`+` загржен.`);
    delete require.cache[require.resolve(`./post/${file}`)];
  } catch (e) {console.warn(e)}

});

// Handle 404 AND 500
mainApp.use((req, res) => {res.status(404).send('404: Not Found <a href="//test-discord.ga">Главная</a>');})
.use((error, req, res, next) => {console.log(error); res.status(500).send('500: Internal Server Error <a href="//test-discord.ga">Главная</a>');})

http.listen(80, () => console.log('Work on port :80'))