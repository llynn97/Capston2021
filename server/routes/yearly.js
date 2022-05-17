var _ = require("lodash");

var monthcheck = function (req, res) {
  var id = req.body.id || req.query.id;
  var num = req.body.num || req.query.num;
  var profit = req.body.profit || req.query.profit;
  var arrlen = parseInt(num);
  var aJsonArray = new Array();
  var startYear;
  var endYear;
  var yearArray = new Array();
  var count = 0;

  var startday = /^s/;
  for (var key in req.body) {
    if (key != "num" && key != "id" && startday.exec(key) == "s") {
      // 시작연도에 1을 더해 끝연도를 구하기 위해 형변환 시행
      req.body[key] *= 1;
      startYear = req.body[key];
      endYear = req.body[key] + 1;
      yearArray[count] = startYear;
      count = count + 1;
    }
  }
  yearArray.sort(function (a, b) {
    return a - b;
  });
  var database = req.app.get("database");
  var i = 0;
  while (i < yearArray.length) {
    var year;
    var yearnow = yearArray[i];
    database.ItemListModel.find(
      { id: id, profit: profit, year: yearnow },
      function (err, results) {
        var answer = 0;
        if (results.length > 0) {
          year = results[0]._doc.year;

          for (var j = 0; j < results.length; j++) {
            
            answer += parseInt(results[j]._doc.price);
          }
          var message = { year: year, money: answer };
        } else {
          var message = { year: "---", money: answer };
        }

        aJsonArray.push(message);

        if (aJsonArray.length === arrlen) {
          res.writeHead("200", {
            "Content-Type": "application/json;charset=utf8",
          });

          res.write(JSON.stringify(aJsonArray));
          res.end();
        }
      }
    );
    i += 1;
  }
};

module.exports.monthcheck = monthcheck;
