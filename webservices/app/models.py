from . import db
from datetime import datetime
from app.exceptions import ValidationError

class User(db.Model):
	__tablename__ = 'users'
	id = db.Column(db.Integer, primary_key=True)
	username = db.Column(db.String(64), unique=True, index=True)
	email = db.Column(db.String(64), unique=True, index=True)
	password_hash = db.Column(db.String(128))
	confirmed = db.Column(db.Boolean, default=False)
	name = db.Column(db.String(64))
	location = db.Column(db.String(64))
	about_me = db.Column(db.Text())
	member_since = db.Column(db.DateTime(), default=datetime.utcnow)
	last_seen = db.Column(db.DateTime(), default=datetime.utcnow)

	def __init__(self, username, email, password):
		self.username = username
		self.email = email
		self.password_hash = password

	def __repr__(self):
		return '<User %r>' % self.username


'''
registration JSON sample
{
  "username": "ekrem"
  "email": "ekremney@gmail.com"
  "password": "123"
  "password_again": "123"
  "birthdate": "19/04/1991"
}
'''

	@staticmethod
	def from_json(json_post):
		if json_post is None or body == '':
			raise ValidationError('This request should have contain a proper JSON')
		return json_post