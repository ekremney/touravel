from .. import db
from datetime import datetime, date
from ..exceptions import ValidationError
import re
from werkzeug.security import generate_password_hash, check_password_hash
from flask import request, current_app
from itsdangerous import TimedJSONWebSignatureSerializer as Serializer
from .follow import Follow
from .post import Post
from .timeline import Timeline

class User(db.Model):
	__tablename__ = 'users'
	id = db.Column(db.Integer, primary_key=True)
	username = db.Column(db.String(64), unique=True, index=True)
	email = db.Column(db.String(64), unique=True, index=True)
	password_hash = db.Column(db.String(128))
	confirmed = db.Column(db.Boolean, default=False)
	birthdate = db.Column(db.Date)
	name = db.Column(db.String(64), default=" ")
	location = db.Column(db.String(64), default=" ")
	about_me = db.Column(db.Text(), default=" ")
	member_since = db.Column(db.DateTime(), default=datetime.utcnow)
	last_seen = db.Column(db.DateTime(), default=datetime.utcnow)
	avatar = db.Column(db.Text(), default="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")
	avatar_thumb = db.Column(db.Text(), default="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")
	followed = db.relationship('Follow', foreign_keys=[Follow.follower_id], backref=db.backref('follower', lazy='joined'), lazy='dynamic', cascade='all, delete-orphan')
	followers = db.relationship('Follow', foreign_keys=[Follow.followed_id], backref=db.backref('followed', lazy='joined'), lazy='dynamic', cascade='all, delete-orphan')
	posts = db.relationship('Post', backref='author', lazy='dynamic')
	timeline_objects = db.relationship('Timeline', backref='author', lazy='dynamic')

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
		if headers.get('password') is None:
			raise ValidationError('Please fill password field.')
		result = {}
		if user is not None and user.verify_password(headers.get('password')):
			result = {'auth-key': user.generate_auth_token(), 'username': user.username, 'name': user.name, 'location': user.location, 'about_me': user.about_me, 'avatar': user.avatar, 'avatar_thumb': user.avatar_thumb}
			return result
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
			return_val[i] = {'username': getattr(val, 'username'), 'name': getattr(val, 'name'), 'location': getattr(val, 'location'), 'about_me': getattr(val, 'about_me'), 'avatar_thumb': getattr(val, 'avatar_thumb')}
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
		if json_post.get('data') is None:
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
		if json_post.get('day') is None or json_post.get('route') is None or json_post.get('stops') is None:
			raise ValidationError('JSON should have all fields')
		day = json_post.get('day')
		route = json_post.get('route')
		stops = json_post.get('stops')
		post = Post(route=route, day=day, stops=stops, author_id=self.id)
		db.session.add(post)
		return True

	def fetch_route(self, headers):
		route = Post.query.filter_by(author_id=self.id).all()
		return_val = {}
		for i, val in enumerate(route):
			user = User.query.filter_by(id=getattr(val, 'author_id')).first()
			return_val[i] = {'day': getattr(val, 'day'), 'route': getattr(val, 'route'), 'stops': getattr(val, 'stops')}
		return return_val

	def get_info(self, headers):
		email = headers.get('email')
		if email is None:
			raise ValidationError('You should specify an email')
		user = User.query.filter_by(email=email).first()
		if user is None:
			raise ValidationError('There\'s no such user')
		following_count = len(self.followed.all())
		follower_count = len(self.followers.all())
		route_count = len(self.posts.all())
		info = {'follower_count': follower_count, 'following_count': following_count, 'route_count': route_count, 'username': user.username, 'name': user.name, 'location': user.location, 'about_me': user.about_me, 'avatar': user.avatar, 'avatar_thumb': user.avatar_thumb}
		return info

	def post_timeline(self, json_post):
		if json_post.get('post_type') is None or json_post.get('data') is None:
			raise ValidationError('JSON should have all fields')
		try:
			post_type = int(json_post.get('post_type'))
		except Exception, e:
			raise ValidationError('Post_type should be an integer')
		if post_type != 0 and post_type != 1:
			raise ValidationError('Post_type should be either 0 or 1')
		data = json_post.get('data')
		t = Timeline(author_id=self.id, post_type=post_type, data=data, like_amount=1)
		db.session.add(t)
		return True

	def fetch_timeline(self):
		t_o = db.session.query(Timeline).select_from(Follow).filter_by(follower_id=self.id).join(Timeline, Follow.followed_id == Timeline.author_id).all()
		return_val = {}
		for i, val in enumerate(t_o):
			user = User.query.filter_by(id=getattr(val, 'author_id')).first()
			return_val[i] = {'post_id': getattr(val, 'id'), 'post_type': getattr(val, 'post_type'), 'name': user.name, 'username': user.username, 'data': getattr(val, 'data'), 'like_amount': getattr(val, 'like_amount'), 'avatar_thumb': user.avatar_thumb}
		return return_val

	def like_timeline(self, headers):
		if headers.get('post_id') is None:
			raise ValidationError('JSON should have all fields')
		try:
			post_id = int(headers.get('post_id'))
		except Exception, e:
			raise ValidationError('Post_type should be an integer')
		t_o = Timeline.query.filter_by(id=post_id).first()
		if t_o is None:
			raise ValidationError('There\'s no such a post')
		t_o.like_amount += 1
		db.session.commit()
		return t_o.like_amount

	def unlike_timeline(self, headers):
		if headers.get('post_id') is None:
			raise ValidationError('JSON should have all fields')
		try:
			post_id = int(headers.get('post_id'))
		except Exception, e:
			raise ValidationError('Post_type should be an integer')
		t_o = Timeline.query.filter_by(id=post_id).first()
		if t_o is None:
			raise ValidationError('There\'s no such a post')
		t_o.like_amount -= 1
		db.session.commit()
		return t_o.like_amount

	def return_followed_users(self):
		# Followerlari donuyor
		t_o = self.followed.all()
		user_list = []
		# Followerlar id'lerini listeye aliyor
		for i, val in enumerate(t_o):
			user_list.append(getattr(val, 'followed_id'))
		# Follower id'lerinden user'lari cekiyor
		users = User.query.filter(User.id.in_(user_list)).all()
		return_val = {}
		for i, val in enumerate(users):
			return_val[i] = {'username': getattr(val, 'username'), 'avatar': getattr(val, 'avatar'), 'location': getattr(val, 'location'), 'name': getattr(val, 'name'), 'avatar_thumb': getattr(val, 'avatar_thumb'), 'about_me': getattr(val, 'about_me') }
		return return_val

	def return_follower_users(self):
		# Followerlari donuyor
		t_o = self.followers.all()
		user_list = []
		# Followerlar id'lerini listeye aliyor
		for i, val in enumerate(t_o):
			user_list.append(getattr(val, 'follower_id'))
		# Follower id'lerinden user'lari cekiyor
		users = User.query.filter(User.id.in_(user_list)).all()
		return_val = {}
		for i, val in enumerate(users):
			return_val[i] = {'username': getattr(val, 'username'), 'avatar': getattr(val, 'avatar'), 'location': getattr(val, 'location'), 'name': getattr(val, 'name'), 'avatar_thumb': getattr(val, 'avatar_thumb'), 'about_me': getattr(val, 'about_me') }
		return return_val




