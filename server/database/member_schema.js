var Schema = {};

Schema.createSchema = function (mongoose) {
  // 스키마 정의
  var MemberSchema = mongoose.Schema({
    id: { type: String, default: "" },
    password: { type: String, required: true, default: "" },

    name: { type: String, index: "hashed", default: "" },
    photoname: { type: String, default: "" },
    budget: { type: Number, default: 0 },
  });

  // password를 virtual 메소드로 정의 : MongoDB에 저장되지 않는 편리한 속성임. 특정 속성을 지정하고 set, get 메소드를 정의함
  /*	RegisterSchema
	  .virtual('password')
	  .set(function(password) {
	    this._password = password;
	    this.salt = this.makeSalt();
	    this.hashed_password = this.encryptPassword(password);
	    console.log('virtual password 호출됨 : ' + this.hashed_password);
	  })
	  .get(function() { return this._password });
	
	// 스키마에 모델 인스턴스에서 사용할 수 있는 메소드 추가
	// 비밀번호 암호화 메소드
	RegisterSchema.method('encryptPassword', function(plainText, inSalt) {
		if (inSalt) {
			return crypto.createHmac('sha1', inSalt).update(plainText).digest('hex');
		} else {
			return crypto.createHmac('sha1', this.salt).update(plainText).digest('hex');
		}
	});
	
	// salt 값 만들기 메소드
	RegisterSchema.method('makeSalt', function() {
		return Math.round((new Date().valueOf() * Math.random())) + '';
	});
	
	// 인증 메소드 - 입력된 비밀번호와 비교 (true/false 리턴)
	RegisterSchema.method('authenticate', function(plainText, inSalt, hashed_password) {
		if (inSalt) {
			console.log('authenticate 호출됨 : %s -> %s : %s', plainText, this.encryptPassword(plainText, inSalt), hashed_password);
			return this.encryptPassword(plainText, inSalt) === hashed_password;
		} else {
			console.log('authenticate 호출됨 : %s -> %s : %s', plainText, this.encryptPassword(plainText), this.hashed_password);
			return this.encryptPassword(plainText) === this.hashed_password;
		}
	});*/

  // 값이 유효한지 확인하는 함수 정의
  var validatePresenceOf = function (value) {
    return value && value.length;
  };

  // 저장 시의 트리거 함수 정의 (password 필드가 유효하지 않으면 에러 발생)
  MemberSchema.pre("save", function (next) {
    if (!this.isNew) return next();

    if (!validatePresenceOf(this.password)) {
      next(new Error("유효하지 않은 password 필드입니다."));
    } else {
      next();
    }
  });

  // 필수 속성에 대한 유효성 확인 (길이값 체크)
  MemberSchema.path("id").validate(function (id) {
    return id.length;
  }, "id칼럼의 값이 없습니다");

  /*	RegisterSchema.path('hashed_password').validate(function (hashed_password) {
		return hashed_password.length;
	}, 'hashed_password 칼럼의 값이 없습니다.');*/

  // 스키마에 static 메소드 추가
  MemberSchema.static("findById", function (id, callback) {
    return this.find({ id: id }, callback);
  });

  MemberSchema.static("findAll", function (callback) {
    return this.find({}, callback);
  });

  console.log("RegisterSchema 정의함.");

  return MemberSchema;
};

// module.exports에 UserSchema 객체 직접 할당
module.exports = Schema;
