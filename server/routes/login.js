var authuser = function (req, res) {
  console.log("routes모듈에서 authuser 호출함");
  var database = req.app.get("database");
  var id = req.body.id || req.query.id;
  var password = req.body.password || req.query.password;

  if (database.db) {
    database.MemberModel.find(
      { id: id, password: password },
      function (err, results) {
        if (err) {
          console.log("데이터베이스에서 사용자 정보를 확인하는 중 오류 발생");
        }
        if (results.length > 0) {
          res.writeHead("200", {
            "Content-Type": "application/json;charset=utf8",
          });
          var message = { success: true };
          res.write(JSON.stringify(message));
          res.end();
        } else {
          res.writeHead("200", {
            "Content-Type": "application/json;charset=utf8",
          });
          var message = { success: false };
          res.write(JSON.stringify(message));
          res.end();
        }
      }
    );
  } else {
    console.log("데이터베이스가 연결되지 않았습니다");
  }
};

var register = function (req, res) {
  console.log("login모듈에 register함수 호출함");
  var id = req.body.id || req.query.id;
  var password = req.body.password || req.query.password;
  var name = req.body.name || req.query.name;
  var database = req.app.get("database");
  var user = new database.MemberModel({
    id: id,
    password: password,
    name: name,
  });
  user.save(function (err) {
    if (err) {
      res.writeHead("200", { "Content-Type": "application/json;charset=utf8" });
      var message = { success: false };
      res.write(JSON.stringify(message));
      res.end();
      return;
    }
    console.log("사용자 데이터 추가함");
    console.dir(user);
    res.writeHead("200", { "Content-Type": "application/json;charset=utf8" });
    var message = { success: true };
    res.write(JSON.stringify(message));
    res.end();
  });
};

var validate = function (req, res) {
  console.log("login모듈에 validate함수 호출함");
  var id = req.body.id || req.query.id;
  var database = req.app.get("database");
  if (database.db) {
    database.MemberModel.findById(id, function (err, results) {
      if (err) {
        console.log("데이터베이스에서 사용자 확인하는 과정중 오류 발생함");
        return;
      }
      if (results.length > 0) {
        console.log("이미 존재하는 아이디임");
        res.writeHead("200", {
          "Content-Type": "application/json;charset=utf8",
        });
        var message = { success: false };
        res.write(JSON.stringify(message));
        res.end();
      } else {
        console.log("아직 사용되지 않은 아이디임");
        res.writeHead("200", {
          "Content-Type": "application/json;charset=utf8",
        });
        var message = { success: true };
        res.write(JSON.stringify(message));
        res.end();
      }
    });
  } else {
    console.log("데이터베이스가 연결되어 있지않음");
  }
};

module.exports.authuser = authuser;
module.exports.register = register;
module.exports.validate = validate;
