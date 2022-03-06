var Schema = {};

Schema.createSchema = function (mongoose) {
  // 스키마 정의
  var CategorySchema = mongoose.Schema({
    categoryname: {
      type: Array,
      default: [
        "식비",
        "문화생활",
        "패션/미용",
        "수입",
        "교육",
        "교통/차량",
        "마트/편의점",
        "건강",
        "기타",
      ],
    },
    id: { type: String, default: "" },
  });
  return CategorySchema;
};
// module.exports에 UserSchema 객체 직접 할당
module.exports = Schema;
