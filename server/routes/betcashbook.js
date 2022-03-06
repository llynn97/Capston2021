var _ = require("lodash");

var addbetcashbook = function (req, res) {
  console.log("betcashbook모듈에 addbetcashbook 함수 호출함");

  var groupname = req.body.groupname || req.query.groupname;
  var goal = req.body.goal || req.query.goal;
  var goalprice = req.body.goalprice || req.query.goalprice;
  var reward = req.body.reward || req.query.reward;
  var penalty = req.body.penalty || req.query.penalty;
  var endDay = req.body.endDay || req.query.endDay;
  var startDay = req.body.startDay || req.query.startDay;
  var inviteCode = req.body.inviteCode || req.query.inviteCode;
  //var month = req.body.m//onth || re//q.query.month;
  //var date = req.body.date// || req.quer//y.date;
  var id = req.body.id || req.query.id;
  var category = req.body.category || req.query.category;
  var database = req.app.get("database");
  var betcashbook = new database.BetCashBookModel({
    groupname: groupname,
    goal: goal,
    goalprice: goalprice,
    reward: reward,
    penalty: penalty,
    endDay: endDay,
    startDay: startDay,
    inviteCode: inviteCode,
    //   // month: month,
    //    d//ate: date,
    id: id,
    category: category,
  });
  betcashbook.save(function (err) {
    if (err) {
      res.writeHead("200", { "Content-Type": "application/json;charset=utf8" });
      var message = { success: false, error: err.message };
      res.write(JSON.stringify(message));
      res.end();
      return;
    }
    console.log(id + "에 itemlist추가함");
    console.dir(betcashbook);
    res.writeHead("200", { "Content-Type": "application/json;charset=utf8" });
    var message = { success: true };
    res.write(JSON.stringify(message));
    res.end();
  });
};

var randomcodecheck = function (req, res) {
  console.log("betcashbook모듈에 randomcodecheck 함수 호출함");
  var inviteCode = req.body.inviteCode || req.query.inviteCode;
  console.log(inviteCode);
  var database = req.app.get("database");
  database.BetCashBookModel.find(
    { inviteCode: inviteCode },
    function (err, results) {
      if (err) {
        console.log(err);
        return res.end(err);
      }
      if (results.length > 0) {
        console.log(results);
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
        console.log("데베에 없음");
      }
    }
  );
};

var showbetcashbook = function (req, res) {
  console.log("itemlist모듈에 showbetcashbook함수 호출함");
  var id = req.params.id;

  var database = req.app.get("database");
  database.BetCashBookModel.find({ id: id }, function (err, results) {
    if (err) {
      console.log(err);
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

var showdetailbetcashbook = function (req, res) {
  console.log("itemlist모듈에 showdetailbetcashbook함수 호출함");
  var inviteCode = req.params.inviteCode;

  var database = req.app.get("database");
  database.BetCashBookModel.find(
    { inviteCode: inviteCode },
    function (err, results) {
      if (err) {
        console.log(err);
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
    }
  );
};

var deletebetcashbook = function (req, res) {
  // 제목에 해당하는거 다 삭제
  console.log("itemlist모듈에 deletebetcashbook 함수 호출함");
  var inviteCode = req.body.inviteCode || req.query.inviteCode;
  var id = req.body.id || req.body.id;
  var database = req.app.get("database");
  database.BetCashBookModel.remove(
    { id: id, inviteCode: inviteCode },
    function (err) {
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
    }
  );
};
var invitebetcashbook = function (req, res) {
  console.log("betcashbook모듈에 invitebetcashbook함수 호출함");
  var inviteCode = req.body.inviteCode || req.query.inviteCode;

  var id = req.body.id || req.query.id;
  var database = req.app.get("database");
  var database = req.app.get("database");
  database.BetCashBookModel.update(
    { inviteCode: inviteCode },
    { $push: { id: id } },
    function (err, results) {
      console.log(inviteCode);
      console.log(id);
      if (err) {
        console.log(err);
        return res.end(err);
      }
      if (results.length > 0) {
        console.log("들어옴");

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
    }
  );
};

module.exports.randomcodecheck = randomcodecheck;
module.exports.addbetcashbook = addbetcashbook;
module.exports.showbetcashbook = showbetcashbook;
module.exports.showdetailbetcashbook = showdetailbetcashbook;
module.exports.deletebetcashbook = deletebetcashbook;
module.exports.invitebetcashbook = invitebetcashbook;
