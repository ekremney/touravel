from .. import db
from datetime import datetime, date

class Timeline(db.Model):
	__tablename__ = 'timeline'
	id = db.Column(db.Integer, primary_key=True)
	post_type = db.Column(db.Integer)
	data = db.Column(db.Text)
	like_amount = db.Column(db.Integer)
	timestamp = db.Column(db.DateTime, index=True, default=datetime.utcnow)
	author_id = db.Column(db.Integer, db.ForeignKey('users.id'))
