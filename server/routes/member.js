var _ = require("lodash");

var findmemberlist = function (req, res) {
  console.log("member모듈에 findmemberlist 함수 호출함");
  var aJsonArray1 = new Array();
  var aJsonArray2 = new Array();
  var nameArray = new Array();

  var startDay = req.body.startDay || req.query.startDay;
  var endDay = req.body.endDay || req.query.endDay;
  var category = req.body.category || req.query.category;
  var num = req.body.num || req.query.num;
  var num2 = parseInt(num);
  var start = new Date(startDay);
  var end = new Date(endDay);

  for (var key in req.body) {
    if (
      key != "startDay" &&
      key != "endDay" &&
      key != "category" &&
      key != "num"
    ) {
      str = req.body[key];
      nameArray.push(str);
    }
  }

  var database = req.app.get("database");
  for (var i = 0; i < nameArray.length; i++) {
    var ids;

    var value = nameArray[i];

    database.ItemListModel.find(
      { id: value, category: category, wholeday: { $gte: start, $lte: end } },
      function (err, results) {
        var answer = 0;
        if (results.length > 0) {
          ids = results[0]._doc.id;
          for (var i = 0; i < results.length; i++) {
            answer += parseInt(results[i]._doc.price);
          }
          var message = { id: ids, money: answer };
        } else {
          var message = { id: "---", money: answer };
        }

        aJsonArray1.push(message);
        if (aJsonArray1.length == num2) {

          for (var j = 0; j < nameArray.length; j++) {
            var value2 = nameArray[j];

            database.MemberModel.find({ id: value2 }, function (err, results2) {
              if (results2.length > 0) {
                aJsonArray2.push(results2);
              }
              if (aJsonArray2.length == num2) {
                aJsonArray1.push(aJsonArray2);
                res.writeHead("200", {
                  "Content-Type": "application/json;charset=utf8",
                });

                res.write(JSON.stringify(aJsonArray1));
                res.end();
              }
            });
          }
        }
      }
    );
  }

};

module.exports.findmemberlist = findmemberlist;
