from .. import db
from datetime import datetime, date
from ..exceptions import ValidationError
import re
from werkzeug.security import generate_password_hash, check_password_hash
from flask import request, current_app
from itsdangerous import TimedJSONWebSignatureSerializer as Serializer
from .follow import Follow
from .post import Post


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
	avatar = db.Column(db.Text())
	avatar_thumb = db.Column(db.Text())
	followed = db.relationship('Follow', foreign_keys=[Follow.follower_id], backref=db.backref('follower', lazy='joined'), lazy='dynamic', cascade='all, delete-orphan')
	followers = db.relationship('Follow', foreign_keys=[Follow.followed_id], backref=db.backref('followed', lazy='joined'), lazy='dynamic', cascade='all, delete-orphan')
	posts = db.relationship('Post', backref='author', lazy='dynamic')

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
			raise ValidationError('Please fill all fields.')
		if password != password_again:
			raise ValidationError('Passwords do not match.')
		if not re.match(r"^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\.[a-zA-Z0-9-.]+$", email):
			raise ValidationError('Please enter a valid email.')
		#if User.date_validator(birthdate) is None:
		#	raise ValidationError('Please submit a valid birthdate in YYYY-MM-DD.')
		if len(username) < 3:
			raise ValidationError('Length of the username must be longer than 3 characters.')
		if len(password) < 3:
			raise ValidationError('Length of the password must be longer than 3 characters.')
		if User.query.filter_by(email=email).first() is not None:
			raise ValidationError('This email is already registered.')
		if User.query.filter_by(username=username).first() is not None:
			raise ValidationError('This username is taken.')
		if not re.match(r"\d{4}[-/]\d{2}[-/]\d{2}", birthdate) or not User.is_birthday_valid(birthdate):
			raise ValidationError('Please enter correct birthdate format')
		bdate = date(int(birthdate[:4]), int(birthdate[5:7]), int(birthdate[8:]))
		user = User(username=username, email=email, password_hash=generate_password_hash(password), birthdate=bdate)
		db.session.add(user)
		return True

	def change_email(self, json_post):
		if json_post is None or json_post == '':
			raise ValidationError('This request should have contain a proper JSON')
		email = json_post.get('email')
		email_again = json_post.get('email_again')
		if email is None or email_again is None:
			raise ValidationError('Please fill all fields.')
		if not re.match(r"^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\.[a-zA-Z0-9-.]+$", email):
			raise ValidationError('Please enter a valid email in email field.')
		if not re.match(r"^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\.[a-zA-Z0-9-.]+$", email_again):
			raise ValidationError('Please enter a valid email in email_again field.')
		if email != email_again:
			raise ValidationError('Emails do not match.')
		self.email = email
		db.session.add(self)
		return True

	def change_password(self, json_post):
		if json_post is None or json_post == '':
			raise ValidationError('This request should have contain a proper JSON')
		old_password = json_post.get('old_password')
		new_password = json_post.get('new_password')
		new_password_again = json_post.get('new_password_again')
		if old_password is None or new_password is None or new_password_again is None:
			raise ValidationError('Please fill all fields.')
		if new_password != new_password_again:
			raise ValidationError('Passwords do not match.')
		if not self.verify_password(old_password):
			raise ValidationError('Old password is wrong.')
		self.password_hash = generate_password_hash(new_password)
		db.session.add(self)
		return True

	def generate_auth_token(self, expiration=9000):
		s = Serializer(current_app.config['SECRET_KEY'], expires_in=expiration)
		return s.dumps({'id': self.id}).decode('ascii')

	def verify_password(self, password):
		return check_password_hash(self.password_hash, password)

	@staticmethod
	def verify_auth_token(token):
		s = Serializer(current_app.config['SECRET_KEY'])
		try:
			data = s.loads(token)
		except:
			return None
		return User.query.get(data['id'])

	@staticmethod
	def login(headers):
		s = Serializer(current_app.config['SECRET_KEY'])
		user = User.query.filter_by(email=headers.get('email')).first()
		if user is not None and user.verify_password(headers.get('password')):
			return user.generate_auth_token()
		return None

	def user_search(self, json_post):
		if json_post is None or json_post == '':
			raise ValidationError('This request should have contain a proper JSON')
		username = json_post.get('username')
		if username is None or len(username) < 3:
			raise ValidationError('Query must have at least 3 characters')
		result = User.query.filter(User.username.ilike('%' + username + '%')).all()
		return_val = {}
		for i, val in enumerate(result):
			return_val[i] = getattr(val, 'username')
		return return_val

	def follow(self, headers):
		user = User.query.filter_by(username=headers.get('username')).first()
		if user is None:
			raise ValidationError('No such user exists: ' + headers.get('username'))
		if self.is_following(user):
			raise ValidationError('You are already following: ' + user.username)
		f = Follow(follower=self, followed=user)
		db.session.add(f)
		return True

	def unfollow(self, headers):
		followed_user = User.query.filter_by(username=headers.get('username')).first()
		if self.id == followed_user.id:
			raise ValidationError('You cannot unfollow yourself')
		if followed_user is None:
			raise ValidationError('No such user exists: ' + headers.get('username'))
		if followed_user.is_followed_by(self) is False:
			raise ValidationError('You cannot unfollow a person you are not following in the first place!')
		f = self.followed.filter_by(followed_id=followed_user.id).first()
		if f:
			db.session.delete(f)
			return True
		return False

	def is_following(self, user):
		return self.followed.filter_by(followed_id=user.id).first() is not None

	def is_followed_by(self, user):
		return self.followers.filter_by(follower_id=user.id).first() is not None

	def edit_profile(self, json_post):
		if json_post.get('name') is None or json_post.get('location') is None or json_post.get('about_me') is None:
			raise ValidationError('JSON should have all fields')
		if len(json_post.get('name')) > 64:
			raise ValidationError('Name should be smaller than 64 characters')
		self.name = json_post.get('name')
		if len(json_post.get('location')) > 64:
			raise ValidationError('Location should be smaller than 64 characters')
		self.location = json_post.get('location')
		self.about_me = json_post.get('about_me')
		db.session.add(self)
	
	@staticmethod
	def is_birthday_valid(birthdate):
		if int(birthdate[:4]) < 1900 or int(birthdate[:4]) > 2015 or int(birthdate[5:7]) < 1 or int(birthdate[5:7]) > 12 or int(birthdate[8:]) < 1 or int(birthdate[8:]) > 31:
			return False
		return True

	def upload_avatar(self, json_post):
		if json_post.get('avatar') is None:
			raise ValidationError('JSON should have all fields')
		self.avatar = json_post.get('avatar')
		db.session.add(self)

	def fetch_avatar(self, headers):
		username = headers.get('username')
		if username is None:
			raise ValidationError('You should specify a username')
		user = User.query.filter_by(username=username).first()
		if user is None:
			raise ValidationError('There\'s no such user')
		return user.avatar

	def post_route(self, json_post):
		if json_post.get('day') is None or json_post.get('data') is None:
			raise ValidationError('JSON should have all fields')
		day = json_post.get('day')
		data = json_post.get('data')
		post = Post(data=data, day=day, author_id=self.id)
		db.session.add(post)
		return True

	def fetch_route(self, headers):
		day = headers.get('day')
		if day is None:
			raise ValidationError('You should specify a day')
		route = Post.query.filter_by(day=day).first()
		if route is None:
			raise ValidationError('There\'s no such route')
		return route.data

	def get_info(self, headers):
		username = headers.get('username')
		if username is None:
			raise ValidationError('You should specify a username')
		user = User.query.filter_by(username=username).first()
		if user is None:
			raise ValidationError('There\'s no such user')
		info = {'username': user.username, 'name': user.name, 'location': user.location, 'about_me': user.about_me, 'avatar': user.avatar, 'avatar_thumb': user.avatar_thumb}
		return info





