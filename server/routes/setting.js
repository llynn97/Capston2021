var showuser = function (req, res) {
  var id = req.body.id || req.query.id;
  var database = req.app.get("database");
  database.MemberModel.find({ id: id }, function (err, results) {
    if (err) {
      return res.end(err);
    }
    if (results.length > 0) {
      res.writeHead("200", {
        "Content-Type": "application/json;charset=utf8",
      });

      res.write(JSON.stringify(results));
      res.end();
    } else {
      res.write("[]");
      res.end();
    }
  });
};

var deleteuser = function (req, res) {
  // 제목에 해당하는거 다 삭제
  var id = req.body.id || req.query.id;

  var database = req.app.get("database");
  database.BetCashBookModel.updateMany(
    { id: id },
    { $pull: { id: id } },
    function (err, results) {
      if (err) {
        return res.end(err);
      }
    }
  );
  database.ItemListModel.remove({ id: id }, function (err) {
    if (err) {
      res.writeHead("200", { "Content-Type": "application/json;charset=utf8" });
      var message = { success: false };
      res.write(JSON.stringify(message));
      res.end();
      return;
    }
  });

  database.MemberModel.remove({ id: id }, function (err) {
    if (err) {
      res.writeHead("200", {
        "Content-Type": "application/json;charset=utf8",
      });
      var message = { success: false };
      res.write(JSON.stringify(message));
      res.end();
      return;
    }
    res.writeHead("200", { "Content-Type": "application/json;charset=utf8" });
    var message = { success: true };
    res.write(JSON.stringify(message));
    res.end();
    return;
  });
};

var setbudget = function (req, res) {
  var budget = req.body.budget || req.query.budget;

  var id = req.body.id || req.query.id;
  var database = req.app.get("database");
  database.MemberModel.findOneAndUpdate(
    { id: id },
    { $set: { budget: budget } },
    function (err, results) {
      if (err) {
        return res.end(err);
      }
      if (Object.keys(results).length > 0) {

        res.writeHead("200", {
          "Content-Type": "application/json;charset=utf8",
        });
        res.write(JSON.stringify(results));
        res.end();
      } else {
        res.write("0");
        res.end();
      }
    }
  );
};

var setphoto = function (req, res) {
  var photoname = req.body.photoname || req.query.photoname;
  var name = req.body.name || req.query.name;

  var id = req.body.id || req.query.id;
  var database = req.app.get("database");
  database.MemberModel.findOneAndUpdate(
    { id: id },
    { photoname: photoname, name: name },
    function (err, results) {

      if (err) {
        return res.end(err);
      }
      if (Object.keys(results).length > 0) {

        res.writeHead("200", {
          "Content-Type": "application/json;charset=utf8",
        });
        res.write(JSON.stringify(results));
        res.end();
      } else {
        res.write("0");
        res.end();
      }
    }
  );
};
module.exports.showuser = showuser;
module.exports.deleteuser = deleteuser;
module.exports.setbudget = setbudget;
module.exports.setphoto = setphoto;
