var _ = require("lodash");

var additemlist = function (req, res) {
  var title = req.body.title || req.query.title;
  var itemname = req.body.itemname || req.query.itemname;
  var price = req.body.price || req.query.price;
  var category = req.body.category || req.query.category;
  var paymethod = req.body.paymethod || req.query.paymethod;
  var profit = req.body.profit || req.query.profit;
  var amount = req.body.amount || req.query.amount;
  var year = req.body.year || req.query.year;
  var month = req.body.month || req.query.month;
  var date = req.body.date || req.query.date;
  var id = req.body.id || req.query.id;
  var wholeDay = req.body.wholeDay || req.query.wholeDay;
  var database = req.app.get("database");
  var itemlist = new database.ItemListModel({
    title: title,
    itemname: itemname,
    price: price,
    category: category,
    paymethod: paymethod,
    profit: profit,
    amount: amount,
    year: year,
    month: month,
    date: date,
    id: id,
    wholeDay: wholeDay,
  });
  itemlist.save(function (err) {
    if (err) {
      res.writeHead("200", { "Content-Type": "application/json;charset=utf8" });
      var message = { success: false, error: err.message };
      res.write(JSON.stringify(message));
      res.end();
      return;
    }
    res.writeHead("200", { "Content-Type": "application/json;charset=utf8" });
    var message = { success: true };
    res.write(JSON.stringify(message));
    res.end();
  });
};

var deleteitem = function (req, res) {
  var _id = req.body._id || req.query._id;
  var num = _id;
  var database = req.app.get("database");
  database.ItemListModel.remove({ _id: _id }, function (err) {
    if (err) {
      res.writeHead("200", { "Content-Type": "application/json;charset=utf8" });
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

var deleteitemlist = function (req, res) {
  var id = req.body.id || req.query.id;
  var title = req.body.title || req.query.title;
  var year = req.body.year || req.query.year;
  var month = req.body.month || req.query.month;
  var date = req.body.date || req.query.date;
  var database = req.app.get("database");
  database.ItemListModel.remove(
    { id: id, title: title, year: year, month: month, date: date },
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

var showitemlist = function (req, res) {
  var id = req.params.id;
  var year = req.params.year;
  var month = req.params.month;
  var date = req.params.date;
  var database = req.app.get("database");
  database.ItemListModel.find(
    { id: id, year: year, month: month, date: date },
    function (err, results) {
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
    }
  );
};

var modifyitemlist = function (req, res) {
  var title = req.body.title || req.query.title;
  var itemname = req.body.itemname || req.query.itemname;
  var price = req.body.price || req.query.price;
  var category = req.body.category || req.query.category;
  var paymethod = req.body.paymethod || req.query.paymethod;
  var profit = req.body.profit || req.query.profit;
  var amount = req.body.amount || req.query.amount;
  var year = req.body.year || req.query.year;
  var month = req.body.month || req.query.month;
  var date = req.body.date || req.query.date;
  var _id = req.body._id || req.query._id;
  var id = req.body.id || req.qurey.id;
  var wholeDay = req.body.wholeDay || req.query.wholeDay;
  var num = _id;
  var database = req.app.get("database");
  if (num == "1") {
    var itemlist = new database.ItemListModel({
      title: title,
      itemname: itemname,
      price: price,
      category: category,
      paymethod: paymethod,
      profit: profit,
      amount: amount,
      year: year,
      month: month,
      date: date,
      id: id,
      wholeDay: wholeDay,
    });
    itemlist.save(function (err) {
      if (err) {
        res.writeHead("200", {
          "Content-Type": "application/json;charset=utf8",
        });
        var message = { success: false, error: err.message };
        res.write(JSON.stringify(message));
        res.end();
        return;
      }
      console.dir(itemlist);
      res.writeHead("200", { "Content-Type": "application/json;charset=utf8" });
      var message = { success: true };
      res.write(JSON.stringify(message));
      res.end();
    });
  } else {
    database.ItemListModel.update(
      { _id: _id },
      {
        $set: _(
          {
            title,
            itemname,
            price,
            category,
            paymethod,
            profit,
            amount,
            year,
            month,
            date,
            wholeDay,
          },
          _.identity
        ),
      },
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
        res.writeHead("200", {
          "Content-Type": "application/json;charset=utf8",
        });
        var message = { success: true };
        res.write(JSON.stringify(message));
        res.end();
        return;
      }
    );
  }
};

module.exports.additemlist = additemlist;
module.exports.deleteitem = deleteitem;
module.exports.deleteitemlist = deleteitemlist;
module.exports.showitemlist = showitemlist;
module.exports.modifyitemlist = modifyitemlist;
