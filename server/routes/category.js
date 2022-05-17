var _ = require("lodash");

var addcategory = function (req, res) {
  var categoryname = req.body.categoryname || req.query.categoryname;
  var id = req.body.id || req.query.id;

  var database = req.app.get("database");
  var category = new database.CategoryModel({
    categoryname: categoryname,
    id: id,
  });
  category.save(function (err) {
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

module.exports.addcategory = addcategory;
