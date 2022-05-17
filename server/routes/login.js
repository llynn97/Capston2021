var authuser = function (req, res) {
  var database = req.app.get("database");
  var id = req.body.id || req.query.id;
  var password = req.body.password || req.query.password;

  if (database.db) {
    database.MemberModel.find(
      { id: id, password: password },
      function (err, results) {
        if (err) {
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
  }
};

var register = function (req, res) {
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
    console.dir(user);
    res.writeHead("200", { "Content-Type": "application/json;charset=utf8" });
    var message = { success: true };
    res.write(JSON.stringify(message));
    res.end();
  });
};

var validate = function (req, res) {
  var id = req.body.id || req.query.id;
  var database = req.app.get("database");
  if (database.db) {
    database.MemberModel.findById(id, function (err, results) {
      if (err) {
        return;
      }
      if (results.length > 0) {
        res.writeHead("200", {
          "Content-Type": "application/json;charset=utf8",
        });
        var message = { success: false };
        res.write(JSON.stringify(message));
        res.end();
      } else {
        res.writeHead("200", {
          "Content-Type": "application/json;charset=utf8",
        });
        var message = { success: true };
        res.write(JSON.stringify(message));
        res.end();
      }
    });
  } else {
  }
};

module.exports.authuser = authuser;
module.exports.register = register;
module.exports.validate = validate;
