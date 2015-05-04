from .. import db
from datetime import datetime, date

class Follow(db.Model):
    __tablename__ = 'follows'
    follower_id = db.Column(db.Integer, db.ForeignKey('users.id'),
                            primary_key=True)
    followed_id = db.Column(db.Integer, db.ForeignKey('users.id'),
                            primary_key=True)
    timestamp = db.Column(db.DateTime, default=datetime.utcnow)
#
#    def __init__(self, follower_id, followed_id, timestamp):
#    	self.follower_id = follower_id
#    	self.followed_id = followed_id
#    	self.timestamp = timestamp
# 

