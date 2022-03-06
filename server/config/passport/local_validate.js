var LocalStrategy = require('passport-local').Strategy;

module.exports = new LocalStrategy({
		usernameField : 'id',
		
		passReqToCallback : true    // 이 옵션을 설정하면 아래 콜백 함수의 첫번째 파라미터로 req 객체 전달됨
	}, function(req, id, done) {
       var database = req.app.get('database');
        database.RegisterModel.findOne({ 'id' :  id }, function(err, user) {
	    	if (err) { return done(err); }

	    	// 이미 아이디가 사용된 경우
	    	if (user) {
	    		console.log('이미 존재하는 아이디가 있음');
	    		return done(null, false, req.flash('loginMessage', '이미 존재하는 아이디가 있음'));  // 검증 콜백에서 두 번째 파라미터의 값을 false로 하여 인증 실패한 것으로 처리
	    	}
	    	
	    	
			console.log('아이디가 중복되지 않음');
			return done(null, user);  // 검증 콜백에서 두 번째 파라미터의 값을 user 객체로 넣어 인증 성공한 것으로 처리
	    });
		
	   
	});