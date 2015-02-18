from .. import db
from datetime import datetime, date
from ..exceptions import ValidationError
import re
from werkzeug.security import generate_password_hash, check_password_hash
from flask import request, current_app
from itsdangerous import TimedJSONWebSignatureSerializer as Serializer


class User(db.Model):
	__tablename__ = 'users'
	id = db.Column(db.Integer, primary_key=True)
	username = db.Column(db.String(64), unique=True, index=True)
	email = db.Column(db.String(64), unique=True, index=True)
	password_hash = db.Column(db.String(128))
	confirmed = db.Column(db.Boolean, default=False)
	birthdate = db.Column(db.Date)
	name = db.Column(db.String(64))
	location = db.Column(db.String(64))
	about_me = db.Column(db.Text())
	member_since = db.Column(db.DateTime(), default=datetime.utcnow)
	last_seen = db.Column(db.DateTime(), default=datetime.utcnow)

	def __init__(self, username, email, password_hash, birthdate):
		self.username = username
		self.email = email
		self.password_hash = password_hash
		self.birthdate = birthdate

	def __repr__(self):
		return '<User %r>' % self.username

	@staticmethod
	def from_json(json_post):
		if json_post is None or json_post == '':
			raise ValidationError('This request should have contain a proper JSON')
		username = json_post.get('username')
		email = json_post.get('email')
		password = json_post.get('password')
		password_again = json_post.get('password_again')
		birthdate = "" + json_post.get('birthdate')
		if username is None or email is None or password is None or password_again is None or birthdate is None:
			raise ValidationError('Please fill all the blanks.')
		if password != password_again:
			raise ValidationError('Passwords do not match.')
		if not re.match(r"^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\.[a-zA-Z0-9-.]+$", email):
			raise ValidationError('Please enter a valid email.')
		#if User.date_validator(birthdate) is None:
		#	raise ValidationError('Please submit a valid birthdate in YYYY-MM-DD.')
		if len(username) < 3:
			raise ValidationError('Length of the username must be longer than 3 characters.')
		if User.query.filter_by(email=email).first() is not None:
			raise ValidationError('This email is already registered.')
		if User.query.filter_by(username=username).first() is not None:
			raise ValidationError('This username is taken.')
		bdate = date(int(birthdate[:4]), int(birthdate[5:7]), int(birthdate[8:]))
		user = User(username=username, email=email, password_hash=generate_password_hash(password), birthdate=bdate)
		db.session.add(user)
		return True

	def generate_auth_token(self, expiration=900):
		s = Serializer(current_app.config['SECRET_KEY'], expires_in=expiration)
		return s.dumps({'id': self.id}).decode('ascii')

	def verify_password(self, password):
		return check_password_hash(self.password_hash, password)

	@staticmethod
	def login(headers):
		s = Serializer(current_app.config['SECRET_KEY'])
		user = User.query.filter_by(email=headers.get('email')).first()
		if user is not None and user.verify_password(headers.get('password')):
			return user.generate_auth_token()
		return None

