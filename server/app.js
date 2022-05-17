var express = require("express"),
  http = require("http"),
  path = require("path");

// Express의 미들웨어 불러오기
var bodyParser = require("body-parser"),
  cookieParser = require("cookie-parser"),
  static = require("serve-static"),
  errorHandler = require("errorhandler");

// 에러 핸들러 모듈 사용
var expressErrorHandler = require("express-error-handler");
// Session 미들웨어 불러오기
var expressSession = require("express-session");

// Passport 사용
var passport = require("passport");
var flash = require("connect-flash");

// 모듈로 분리한 설정 파일 불러오기
var config = require("./config/config");

// 모듈로 분리한 데이터베이스 파일 불러오기
var database = require("./database/database");

// 모듈로 분리한 라우팅 파일 불러오기
var route_loader = require("./routes/route_loader");

var app = express();

console.log("config.server_port : %d", config.server_port);
app.set("port", process.env.PORT || 3000);

app.use(bodyParser.urlencoded({ extended: false }));
app.use(bodyParser.json());

app.use("/public", static(path.join(__dirname, "public")));

app.use(cookieParser());

// 세션 설정
app.use(
  expressSession({
    secret: "my key",
    resave: true,
    saveUninitialized: true,
  })
);

// 라우팅 정보를 읽어서 라우팅 설정
var router = express.Router();
route_loader.init(app, router);

// 패스포트 설정
var configPassport = require("./config/passport");
configPassport(app, passport);

// 패스포트 라우팅 설정
var userPassport = require("./routes/user_passport");
userPassport(router, passport);

app.on("close", function () {
  console.log("Express 서버 객체가 종료됩니다.");
  if (database.db) {
    database.db.close();
  }
});

var server = http.createServer(app).listen(app.get("port"), function () {
  console.log("서버가 시작되었습니다. 포트 : " + app.get("port"));

 // 데이터베이스 초기화
  database.init(app, config);
});
